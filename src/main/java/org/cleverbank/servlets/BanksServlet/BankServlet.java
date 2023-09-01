package org.cleverbank.servlets.BanksServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.Bank;
import org.cleverbank.dto.User;
import org.cleverbank.service.BankService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;


@WebServlet("/banks")
public class BankServlet extends HttpServlet {

    BankService bankService = new BankService();

    /**
     *
     * Getting bank from db by id
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);
        int bank_id  = json.getInt("id");

        Bank bank = null;
        try {
            bank = bankService.getBankById(bank_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        out.println(bank.toString());
    }

    /**
     *
     * Creating bank and uploading on db,
     *
     * Requests id and bank_name
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int bank_id = json.getInt("id");
        String bank_name = json.getString("bank_name");
        Bank bank = new Bank(bank_id, bank_name);
        try {
            bankService.createBank(bank);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        out.println("Bank Created");

    }

    /**
     *
     * Updates users name from db
     *
     * Requests id and name
     */

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int bank_id = json.getInt("id");

        Bank bank = null;
        try {
            bank = bankService.getBankById(bank_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(bank != null) {
            if (json.has("bank_name")) {
                bank.setName(json.getString("bank_name"));
            }
            try {
                bankService.updateBank(bank);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            out.println("Bank updated");
        } else {
            out.println("No such bank found");
        }

    }

    /**
     *
     * Deletes user from db
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

        int bank_id = json.getInt("id");

        try {
            bankService.deleteBankById(bank_id);
            out.println("Bank deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
