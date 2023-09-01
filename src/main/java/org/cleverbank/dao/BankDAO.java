package org.cleverbank.dao;

import org.cleverbank.dto.Account;
import org.cleverbank.dto.Bank;

import java.sql.SQLException;
import java.util.List;

public interface BankDAO {

    List<Bank> getAllBanks() throws SQLException;
    Bank getBankById(int id) throws SQLException;
    void createBank(Bank bank) throws SQLException;
    void deleteBankById(int bank_id) throws SQLException;
    void updateBank(Bank bank) throws SQLException;
    String getBankNameById(int id ) throws SQLException;

}
