package org.cleverbank.servlets.UserServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.User;
import org.cleverbank.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AllUsers")
public class AllUsersServlet extends HttpServlet {

    /**
     * Getting all users from db
     */

    UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        List<User> list;
        try {
            list = userService.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println(list);

    }
}
