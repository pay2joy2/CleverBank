package org.cleverbank.service;

import java.sql.Connection;
import org.cleverbank.dao.UserDAO;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements UserDAO {

    public static ConnFactory cf = ConnFactory.getInstance();

    /**
     *
     * Function to return all users
     *
     */
    @Override
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<User> list = new ArrayList<>();
        if(rs.isBeforeFirst())
        {
            while (rs.next())
            {
                User u = new User(rs.getInt(1),rs.getString(2));
                list.add(u);
            }
        } else {
            System.out.println("List of Users is empty");
        }
        conn.close();
        return list;
    }

    /**
     *
     * Function to get user by id from db
     *
     */
    @Override
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        User u = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                u = new User(rs.getInt(1), rs.getString(2));
            }
        }else {
            System.out.println("User not found");
        }
        conn.close();
        return u;
    }

    /**
     *
     * Function to create user
     *
     */
    @Override
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users(id, user_name) VALUES (?,?)";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, user.getId());
        ps.setString(2, user.getName());
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to delete user from db
     *
     */
    @Override
    public void deleteUserById(int user_id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, user_id);
        ps.executeUpdate();
        conn.close();
    }

    /**
     *
     * Function to update user_name in db by id
     *
     */
    @Override
    public void updateUser (User user) throws SQLException {
        String sql = "UPDATE users SET user_name = ? WHERE id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setInt(2,user.getId());
        ps.executeUpdate();
        conn.close();
    }

}
