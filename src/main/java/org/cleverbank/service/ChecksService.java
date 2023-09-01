package org.cleverbank.service;

import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ChecksService {
    public static ConnFactory cf = ConnFactory.getInstance();

    /**
     *
     * Creates and saves checks in checks/ folder in user.dir
     * B
     */
    public void FundsCheck(UUID uuid) throws SQLException {

        Transaction t = FormTransaction(uuid);

        LinkedHashMap<String, String> Checkmap = new LinkedHashMap<>();
        Checkmap.put("Check Id:", String.valueOf(t.getUuid()));
        Checkmap.put(String.valueOf(t.getDatestamp()), String.valueOf(t.getTimestamp()));
        Checkmap.put("Type:", t.getType());
        if (t.getToAccount() == 0) {
            Checkmap.put("Bank:", t.getBankFrom());
            Checkmap.put("Account:", String.valueOf(t.getFromAccount()));
        } else {
            Checkmap.put("Sender Bank:", t.getBankFrom());
            Checkmap.put("Receiver:", t.getBankTo());
            Checkmap.put("Sender account:", String.valueOf(t.getFromAccount()));
            Checkmap.put("Receiver account:", String.valueOf(t.getToAccount()));
        }
        Checkmap.put("Amount:", String.valueOf(t.getAmount()));

        StringBuilder CheckString = CheckForming(Checkmap);

       //String filePath = ("checks/" + String.valueOf(t.getUuid()) + ".txt");
        String filePath = (System.getProperty("user.dir") + "/checks/" + t.getUuid() + ".txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))){
            writer.println(CheckString);
            System.out.println("Check was formed in: " + filePath);
        } catch (IOException e) {
            System.out.println("Error occurred when forming check: " + e.getMessage());
        }
    }

    /**
     *
     * Forms a transaction entity for checks and transactions
     *
     */
    private Transaction FormTransaction(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM transactions \n" +
                "LEFT JOIN accounts \n" +
                "ON accounts.account_id = transactions.account_id \n" +
                "LEFT JOIN banks \n" +
                "ON banks.id = accounts.banks_id WHERE uuid = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, uuid);
        ResultSet rs = ps.executeQuery();
        conn.close();
        Transaction t = null;
        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                t = new Transaction(((UUID) rs.getObject(1)), rs.getInt(2),
                        rs.getInt(5), rs.getDouble(4), rs.getString(3),
                        rs.getDate(6), rs.getTime(6), rs.getString(13));
            }
        }
        if (t.getToAccount() != 0) {
            AccountService accountService = new AccountService();
            BankService bankService = new BankService();
            t.setBankTo(bankService.getBankNameById(accountService.getAccountById(t.getToAccount()).getBank_id()));
        }
        return t;
    }

    /**
     *
     * Forms a check form to be filled with transaction information
     */
    private StringBuilder CheckForming(LinkedHashMap Checkmap) {
        Set set = Checkmap.entrySet();
        Iterator iterator = set.iterator();

        StringBuilder CheckString = new StringBuilder();

        CheckString.append("____________________________________________________ \n");
        CheckString.append("|                       Bank                       | \n");

        while (iterator.hasNext()) {
            StringBuilder x1 = new StringBuilder("|                                               |\n");
            Map.Entry item = (Map.Entry) iterator.next();

            String key = item.getKey().toString();
            String value = item.getValue().toString();

            x1.replace(1, key.length(), key);
            x1.replace((x1.length() - value.length()), (x1.length() - 2), value);
            CheckString.append(x1);
        }
        CheckString.append("|                                                  |\n");
        CheckString.append("----------------------------------------------------");

        return CheckString;
    }
}
