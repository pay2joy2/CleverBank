package org.cleverbank.dao;

import org.cleverbank.dto.Account;
import org.cleverbank.dto.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    // GET ALL USERS; BANKS
    List<User> getAllUsers() throws SQLException;
    User getUserById(int id) throws SQLException;
    void createUser(User user) throws SQLException;
    void deleteUserById(int user_id) throws SQLException;
    void updateUser(User user) throws SQLException;
}
