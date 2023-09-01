package org.cleverbank.dao;

import org.cleverbank.dto.Account;
import org.cleverbank.dto.Transaction;

import java.sql.SQLException;
import java.util.UUID;

public interface TransactionDAO {

    void transfer(int accountTo, int accountFrom, double amount) throws SQLException;

    void AddFunds(Account account, Double amount) throws SQLException;

    void WithdrawFunds(Account account, Double amount) throws SQLException;

    Transaction FindTransactionById(UUID uuid) throws SQLException;

    void SaveTransaction(int accountTo, int accountFrom, double amount, String type) throws SQLException;
}
