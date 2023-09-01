package org.cleverbank.servlets.FundsServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Account;
import org.cleverbank.service.AccountService;
import org.cleverbank.service.TransactionService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;


@WebServlet("/WithdrawFunds")
public class WithdrawFundsServlet extends HttpServlet {

    AccountService accountService = new AccountService();
    TransactionService transactionService = new TransactionService();

    /**
     * Withdraws funds from account
     * Requests account_id, amount
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);
        int account_id  = json.getInt("account_id");
        double amount = json.getDouble("amount");

        Account account = null;
        try {
            account = accountService.getAccountById(account_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (account != null) {
            try {
                transactionService.WithdrawFunds(account, amount);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
