package org.cleverbank.service;

import org.cleverbank.dao.BankDAO;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.Bank;
import org.cleverbank.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankService implements BankDAO {

    public static ConnFactory cf = ConnFactory.getInstance();

    /**
     *
     * Function to get list of all banks from db
     *
     */
    @Override
    public List<Bank> getAllBanks() throws SQLException {
        String sql = "SELECT * FROM banks";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Bank> list = new ArrayList<>();
        if(rs.isBeforeFirst())
        {
            while (rs.next())
            {
                Bank bank = new Bank(rs.getInt(1),rs.getString(2));
                list.add(bank);
            }
        } else {
            System.out.println("List of Banks is empty");
        }
        conn.close();
        return list;
    }

    /**
     *
     * Function to get bank from db by id
     */
    @Override
    public Bank getBankById(int id) throws SQLException {

        String sql = "SELECT * FROM banks WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        Bank bank = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                bank = new Bank(rs.getInt(1), rs.getString(2));
            }
        }else {
            System.out.println("Bank not found");
        }
        conn.close();
        return bank;
    }

    /**
     *
     * Function to create bank
     */
    @Override
    public void createBank(Bank bank) throws SQLException {
        String sql = "INSERT INTO banks(id, bank_name) VALUES (?,?)";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bank.getId());
        ps.setString(2, bank.getName());
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to delete bank from db by id
     *
     */
    @Override
    public void deleteBankById(int bank_id) throws SQLException {
        String sql = "DELETE FROM banks WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bank_id);
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to update bank_name in db
     *
     */
    @Override
    public void updateBank(Bank bank) throws SQLException {

        String sql = "UPDATE banks SET bank_name = ? WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, bank.getName());
        ps.setInt(2, bank.getId());
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to get BankName from db by id
     *
     */

    @Override
    public String getBankNameById(int id) throws SQLException {
        Connection conn = cf.getConnection();
        String sql = "SELECT * FROM banks WHERE id = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String BankName = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
               BankName = (rs.getString(2));
            }
        } else {
            System.out.println("Банк не найден");
        }
        conn.close();
        return BankName;
    }
}
