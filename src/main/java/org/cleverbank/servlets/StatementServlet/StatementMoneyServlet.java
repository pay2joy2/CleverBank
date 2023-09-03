package org.cleverbank.servlets.StatementServlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.service.StatementService;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;
@WebServlet("/statement")
public class StatementMoneyServlet  extends HttpServlet  {
    private static final ConnFactory cf = ConnFactory.getInstance();
    @Override

    /**
     *
     * Returns Statement for a certain period.
     * Creates PDF file.
     *
     * Requests: account_id, TimePeriod
     * Available periods: "Whole", "Year", "Month";
     *
     */

    protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader bufferedReader = reqest.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);
        int account_id = json.getInt("account_id");
        String time_period = json.getString("TimePeriod");


        StatementService statementService = new StatementService();

        ResultSet rs = null;

        try {
            rs = statementService.GetResultSet(account_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (rs != null) {
            StringBuilder statement = null;
            try {
                statement = statementService.GetStringBuilder(rs, account_id ,time_period);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String sql = "SELECT \n" +
                    "SUM(CASE \n" +
                    "            WHEN type = 'Withdraw' THEN -amount \n" +
                    "            WHEN type = 'Transaction' AND account_id = 220993 THEN -amount \n" +
                    "            ELSE 0 \n" +
                    "        END) AS LOSS,\n" +
                    "    SUM(CASE \n" +
                    "            WHEN type = 'Deposit' THEN amount \n" +
                    "            WHEN type = 'Transaction' AND account_id <> 220993 THEN amount \n" +
                    "            ELSE 0 \n" +
                    "        END) AS INCOME\n" +
                    "FROM transactions\n" +
                    "WHERE account_id = ?;";

            Connection conn = cf.getConnection();
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet otherrs = ps.executeQuery();
            otherrs.next();
            conn.close();

            statement.append("                          Income  | Loss             \n");
            statement.append("---------------------------------------------------------------------- \n");
            StringBuilder x1 = new StringBuilder("                                  |                                 ");
            String plus = String.valueOf(otherrs.getDouble(2));
            String minus = String.valueOf(otherrs.getDouble(1));
            x1.replace(25, plus.length()+25, plus);
            x1.replace(36,minus.length()+36, minus);
            statement.append(x1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            UUID uuid = UUID.randomUUID();

            //Dir problems
            String filePath = (System.getProperty("user.dir") + "/" + "statement-money/" + uuid + ".pdf");

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                System.out.println("Statement formed in: " + filePath);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("Error occurred when forming statement: " + e.getMessage());
            }

            document.open();
            try {
                Paragraph ToPdfText = new Paragraph(statement.toString());
                ToPdfText.setFont(FontFactory.getFont(FontFactory.COURIER, 13));
                document.add(ToPdfText);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            document.close();


        } else {

            System.out.println("Account not found");

        }


    }
}
