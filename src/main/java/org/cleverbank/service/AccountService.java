package org.cleverbank.service;

import org.cleverbank.dao.AccountDAO;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.Bank;
import org.cleverbank.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AccountService implements AccountDAO {

    public static ConnFactory cf = ConnFactory.getInstance();

    /**
     *
     * Function to get all accounts from db
     *
     */
    @Override
    public List<Account> getAllAccounts() throws SQLException {
        Connection conn = cf.getConnection();
        String sql = "SELECT * FROM accounts";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Account> list = new LinkedList<>();
        Account a = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                a = new Account(rs.getInt(1), rs.getDouble(2), rs.getInt(3), rs.getInt(4));
                list.add(a);
            }
        }
        conn.close();
        return list;
    }

    /**
     *
     * Function to get account info by id
     */
    @Override
    public Account getAccountById(int id) throws SQLException {
        Connection conn = cf.getConnection();
        String sql = "SELECT * FROM accounts WHERE account_id = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Account a = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                a = new Account(rs.getInt(1), rs.getDouble(2),rs.getInt(3),rs.getInt(4));
            }
        } else {
            System.out.println("Account not found");
        }
        conn.close();
        return a;
    }

    /**
     *
     * Function to create account
     *
     */

    @Override
    public void createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_id, balance, users_id, banks_id) VALUES (?,?,?,?)";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, account.getId());
        ps.setDouble(2, account.getBalance());
        ps.setInt(3, account.getUser_id());
        ps.setInt(4, account.getBank_id());
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to delete account by id from db
     *
     */
    @Override
    public void deleteAccountById(int account_id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, account_id);
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to update account balance/users_id/banks_id
     */
    @Override
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, users_id = ?, banks_id = ? WHERE account_id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, account.getBalance());
        ps.setInt(2, account.getUser_id());
        ps.setInt(3, account.getBank_id());
        ps.setInt(4, account.getId());
        ps.executeUpdate();
        conn.close();
    }

}
