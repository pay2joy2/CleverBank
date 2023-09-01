package org.cleverbank.servlets.BanksServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Bank;
import org.cleverbank.dto.User;
import org.cleverbank.service.BankService;
import org.cleverbank.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AllBanks")
public class AllBanksServlet extends HttpServlet {

    BankService bankService = new BankService();

    /**
     * Getting all banks from db
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        List<Bank> list;
        try {
            list = bankService.getAllBanks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println(list);

    }
}
