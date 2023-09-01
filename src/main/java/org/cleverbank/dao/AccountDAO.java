package org.cleverbank.dao;

import org.cleverbank.dto.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {

    List<Account> getAllAccounts() throws SQLException;
    Account getAccountById(int id) throws SQLException;
    void createAccount(Account account) throws SQLException;
    void deleteAccountById(int account_id) throws SQLException;
    void updateAccount(Account account) throws SQLException;

}
