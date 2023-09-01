package org.cleverbank.servlets.AccountsServlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.User;
import org.cleverbank.service.AccountService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AllAccounts")
public class AllAcountsServlet extends HttpServlet {

    AccountService accountService = new AccountService();

    /**
     *
     * Getting all Account from db
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        List<Account> list;
        try {
            list = accountService.getAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println(list);

    }
}
