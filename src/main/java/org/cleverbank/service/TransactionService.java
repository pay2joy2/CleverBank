package org.cleverbank.service;

import org.cleverbank.dao.TransactionDAO;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;


public class TransactionService implements TransactionDAO  {
    public static ConnFactory cf = ConnFactory.getInstance();
    ReentrantLock lock = new ReentrantLock();

    /**
     *
     * Creates transfer between two accounts
     *
     */
    @Override
    public void transfer(int accountTo, int accountFrom, double amount) throws SQLException {
        lock.lock();
        AccountService accountService = new AccountService();
        Account fromAccount = accountService.getAccountById(accountFrom);
        Account toAccount = accountService.getAccountById(accountTo);
        if (fromAccount != null)
        {
            if (toAccount != null)
            {
                fromAccount.lock();
                toAccount.lock();
                if(fromAccount.getBalance() >= amount){
                    fromAccount.setBalance(fromAccount.getBalance() - amount);
                    toAccount.setBalance(toAccount.getBalance() + amount);
                    accountService.updateAccount(fromAccount);
                    accountService.updateAccount(toAccount);
                    SaveTransaction(accountTo, accountFrom, amount, "Transaction");
                    System.out.println("Transfer is completed");
                } else {
                    System.out.println("Not enough funds for operation");
                }
                fromAccount.unlock();
                toAccount.unlock();
            } else {
                System.out.println("Receiver account not exist");
            }
        } else {
            System.out.println("Sender account not exist");
        }
       lock.unlock();
    }

    /**
     *
     * Adds funds to account and updates info in db
     *
     */
    @Override
    public void AddFunds(Account account, Double amount) throws SQLException {
        lock.lock();
        String sql = "UPDATE accounts SET balance =? WHERE account_id = ?";
        account.setBalance(account.getBalance() + amount);
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, account.getBalance());
        ps.setInt(2,account.getId());
        ps.executeUpdate();
        SaveTransactionFunds(account.getId(),amount,"Deposit");
        conn.close();
        lock.unlock();
    }

    /**
     *Withdraws funds from account and updates info in db
     *
     */
    @Override
    public void WithdrawFunds(Account account, Double amount) throws SQLException {
        if (account.getBalance() >= amount) {
            lock.lock();
            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            account.setBalance(account.getBalance() - amount);
            Connection conn = cf.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, account.getBalance());
            ps.setInt(2,account.getId());
            ps.executeUpdate();
            conn.close();
            SaveTransactionFunds(account.getId(), amount, "Withdraw");
            lock.unlock();
        }else {
            System.out.println("Not enough funds to withdraw");
        }
    }

    /**
     *
     * Saves transaction (transfer) in db, summons function to create a check
     *
     */
    @Override
    public void SaveTransaction(int accountTo, int accountFrom, double amount, String type) throws SQLException {

        UUID uuid = UUID.randomUUID();

        String sql = "INSERT INTO transactions (uuid, account_id ,type,amount,counterparty_account_id) VALUES (?,?,?,?,?)";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, uuid);
        ps.setInt(2,accountFrom);
        ps.setString(3,type);
        ps.setDouble(4,amount);
        ps.setInt(5,accountTo);
        ps.executeUpdate();
        conn.close();

        new ChecksService().FundsCheck(uuid);

    }

    /**
     *
     * Saves transaction(AddFunds, WithdrawFunds) in db, summons function to create a check
     *
     */
    private void SaveTransactionFunds(int accountFrom, double amount, String type) throws SQLException {

        UUID uuid = UUID.randomUUID();

        String sql = "INSERT INTO transactions (uuid, account_id, type, amount) VALUES (?,?,?,?)";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, uuid);
        ps.setInt(2,accountFrom);
        ps.setString(3,type);
        ps.setDouble(4,amount);
        ps.executeUpdate();
        conn.close();

        new ChecksService().FundsCheck(uuid);

    }

    /**
     *
     * Finds transaction from db by id
     *
     */

    @Override
    public Transaction FindTransactionById(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE uuid = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,uuid);
        ResultSet rs = ps.executeQuery();
        Transaction transaction = null;
        if(rs.isBeforeFirst())
        {
            while (rs.next()){
                transaction = new Transaction(rs.getDate(6),rs.getString(3),
                        rs.getDouble(4), rs.getInt(2), rs.getInt(5));

            }
        }
        return transaction;
    }
}
