package org.cleverbank.servlets.StatementServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.service.StatementService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/statementTransactions")
public class StatementWithTransactions extends HttpServlet {

    /**
     *
     * Returns Statement with Transactions for a certain period.
     * Creates TXT and PDF files
     *
     * Requests: account_id, TimePeriod
     * Available periods: "Whole", "Year", "Month";
     *
     */
    @Override
    protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader bufferedReader = reqest.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);
        int account_id = json.getInt("account_id");
        String time_period = json.getString("TimePeriod");

        StatementService statementService = new StatementService();

        try {
            statementService.GetStatement(account_id,time_period);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
