package org.cleverbank.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Transaction;
import org.cleverbank.service.TransactionService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    TransactionService transactionService = new TransactionService();

    /**
     *
     * Getting info about transaction by UUID;
     *
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        String transaction_id = json.getString("transaction_id");

        UUID uuid = UUID.fromString(transaction_id);
        Transaction transaction = null;
        try {
            transaction = transactionService.FindTransactionById(uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (transaction != null)
        {
            out.println(transaction);
        } else {
            out.println("transaction not found");
        }
    }

    /**
     *
     * Creating transfer between two accounts
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int accountTo = json.getInt("AccountToId");
        int accountFrom = json.getInt("AccountFromId");
        double amount = json.getDouble("amount");

        try {
            transactionService.transfer(accountTo,accountFrom,amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
