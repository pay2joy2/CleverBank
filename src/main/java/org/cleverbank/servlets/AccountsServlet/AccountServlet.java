package org.cleverbank.servlets.AccountsServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Account;
import org.cleverbank.service.AccountService;
import org.cleverbank.service.InterestService;
import org.cleverbank.service.TransactionService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/accounts")
public class AccountServlet extends HttpServlet {

    AccountService accountService = new AccountService();

    /**
     *
     * Getting account from db by id
     *
     */
    @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int account_id = json.getInt("id");

        Account account = null;
        try {
           account = accountService.getAccountById(account_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println(account.toString());
    }

    /**
     *
     * Creating account and uploading on db,
     *
     * Requests id, balance, users_id, banks_id;
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int account_id = json.getInt("id");
        double balance = 0;
        if(json.has("balance")) {
            balance = json.getDouble("balance");
        }
        int users_id = json.getInt("users_id");
        int bank_id = json.getInt("banks_id");
        Account account = new Account(account_id, balance, users_id, bank_id);
        try {
            accountService.createAccount(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        out.println("Account Created");
    }

    /**
     *
     * Updates account from db
     *
     * Requests id
     * Not strict requests: balance, users_id, banks_id;
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int account_id = json.getInt("id");

        Account account = null;

        try {
            account = accountService.getAccountById(account_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (account != null) {
            if (json.has("balance")) {
                account.setBalance(json.getDouble("balance"));
            }
            if(json.has("users_id")){
                account.setUser_id(json.getInt("users_id"));
            }
            if (json.has("banks_id")){
                account.setBank_id(json.getInt("banks_id"));
            }

            try {
                accountService.updateAccount(account);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            out.println("Account updated");
        } else {
            out.println("There is no account with such id");
        }
    }

    /**
     *
     * Deletes account from db
     * Requests id
     *
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int account_id = json.getInt("id");

        try {
            accountService.deleteAccountById(account_id);
            out.println("Account deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}