package org.cleverbank.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Transaction;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class StatementService {

    private static final ConnFactory cf = ConnFactory.getInstance();

    /**
     *
     * Function to get info for transaction and statement creation
     *
     */
    public ResultSet GetResultSet(int AccountId) throws SQLException {

        String sql = "SELECT * FROM accounts" +
                " LEFT JOIN users" +
                " ON accounts.users_id = users.id " +
                " LEFT JOIN banks" +
                " ON accounts.banks_id = banks.id" +
                " WHERE account_id = ?";

        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, AccountId);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**
     *
     * Creates a StringBuilder - "Header" of Statements
     *
     */
    public StringBuilder GetStringBuilder(ResultSet rs, int AccountId, String timePeriod) throws SQLException {
        rs.next();
        StringBuilder Statement = new StringBuilder();
        String a = null;
        Statement.append("                                Statement                          \n");
        Statement.append("                               " +  rs.getString(9) + "                       \n");
        Statement.append("Client:" + "                               | " + rs.getString(7) + "\n");
        Statement.append("Account:" + "                              | " + rs.getString(1) + "\n");
        Statement.append("Currency:" + "                             | " + "BYN" + "\n");
        Statement.append("Date of account creation:             | " + rs.getDate(5) +"\n" );
        Statement.append("Period: " + "                              | " + GetPeriod(timePeriod) + " - " + LocalDate.now() + "\n" );
        Statement.append("Date of statement creation:" + "           | " + LocalDate.now() + " " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "\n");
        Statement.append("Balance:" + "                              | " + rs.getDouble(2) + "\n");
        return Statement;
    }

    /**
     *
     * Creates a Statement or Check for a certain TimePeriod
     * Available TimePeriods: "Month", "Year", "Whole"
     * Saves to PDF and TXT
     */
    public void GetStatement(int AccountId, String timePeriod) throws SQLException {

        ResultSet rs = GetResultSet(AccountId);

        if(rs.isBeforeFirst())
        {
            StringBuilder Statement = GetStringBuilder(rs, AccountId, timePeriod);
            List<Transaction> transactions = GetTransactionsForPeriod(AccountId, timePeriod);
            for (int i = 0; i < transactions.size(); i++)
            {
                StringBuilder x1 = new StringBuilder("             |                                               |                                     \n");
                x1.replace(0, transactions.get(i).getDatestamp().toString().length(), transactions.get(i).getDatestamp().toString());
                switch(transactions.get(i).getType()){
                    case ("Deposit"):
                        x1.replace(15, transactions.get(i).getType().length()+15, transactions.get(i).getType());
                        x1.replace(63, String.valueOf(transactions.get(i).getAmount()).length()+62, String.valueOf(transactions.get(i).getAmount()));
                        break;
                    case ("Withdraw"):
                        x1.replace(15, transactions.get(i).getType().length()+15, transactions.get(i).getType());
                        String minus = ("-" + transactions.get(i).getAmount());
                        x1.replace(63, String.valueOf(transactions.get(i).getAmount()).length()+62, minus);
                        break;
                    case("Transaction"):
                        if(transactions.get(i).getFromAccount() != AccountId){
                            String transactionString = (transactions.get(i).getType() + " from account: " + transactions.get(i).getFromAccount());
                            x1.replace(15, transactionString.length() + 15, transactionString);
                            x1.replace(63, String.valueOf(transactions.get(i).getAmount()).length()+62, String.valueOf(transactions.get(i).getAmount()));
                        } else {
                            String transactionString = (transactions.get(i).getType() + " to account: " + transactions.get(i).getToAccount());
                            String minusTrans = ("-" + transactions.get(i).getAmount());
                            x1.replace(15, transactionString.length() + 15, transactionString);
                            x1.replace(63, String.valueOf(transactions.get(i).getAmount()).length()+62, minusTrans);
                        }
                        break;
                }
                Statement.append(x1);
            }
            UUID uuid = UUID.randomUUID();
            String filePath =("statements/" + uuid + ".txt");
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println(Statement.toString());
                System.out.println("Statement saved in: " + filePath);
            } catch (IOException e) {
                System.out.println("Error occurred when saving txt statement: " + e.getMessage());
            }
            filePath =("statements/" + uuid + ".pdf");
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("Error occurred when forming pdf statement: " + e.getMessage());
            }

            document.open();
            try {
                Paragraph ToPdfText = new Paragraph(Statement.toString());
                ToPdfText.setFont(FontFactory.getFont(FontFactory.COURIER, 13));
                document.add(ToPdfText);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            document.close();
        }else {
            System.out.println("Account not found");
        }
    }

    /**
     *
     * Function to get TimePeriod for transactions
     *
     */
    private LocalDate GetPeriod(String timePeriod)
    {
        LocalDate StartPeriod = LocalDate.now();
        switch (timePeriod) {
            case("Month"):
                StartPeriod = StartPeriod.minusMonths(1);
                break;
            case ("Year"):
                StartPeriod = StartPeriod.minusYears(1);
                break;
            case("Whole"):
                StartPeriod = LocalDate.of(1970,1,1);
                break;
        }
        return StartPeriod;
    }

    /**
     *
     * Gets all Transactions from db for a certain TimePeriod
     *
     */
    private List<Transaction> GetTransactionsForPeriod(int AccountId, String timePeriod) throws SQLException {
        LocalDate StartPeriod = GetPeriod(timePeriod);
        String sql = "SELECT * FROM transactions" +
                " WHERE cast(created_at as date)" +
                " BETWEEN ? AND ?" +
                " AND account_id = ? OR counterparty_account_id = ?";
        Connection conn = cf.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(StartPeriod));
        ps.setDate(2, Date.valueOf(LocalDate.now()));
        ps.setInt(3, AccountId);
        ps.setInt(4, AccountId);
        ResultSet rs = ps.executeQuery();
        List<Transaction> transactions = new LinkedList<>();
        Transaction transaction = null;
        if(rs.isBeforeFirst())
        {
            while(rs.next())
            {
                transaction = new Transaction(rs.getDate(6),rs.getString(3),
                        rs.getDouble(4), rs.getInt(2), rs.getInt(5));
                transactions.add(transaction);
            }
        }else {
            System.out.println("Operation for set period is not found");
        }
        return transactions;
    }


}
