package org.cleverbank.servlets.UserServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cleverbank.dto.User;
import org.cleverbank.service.UserService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    UserService userService = new UserService();

    /**
     *
     * Getting user from db by id
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);
        int user_id  = json.getInt("id");

        User user = null;
        try {
            user = userService.getUserById(user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.println(user.toString());
    }

    /**
     *
     * Creating user and uploading on db,
     *
     * Requests id and name
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader bufferedReader = request.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        int user_id = json.getInt("id");
        String name = json.getString("name");
        User user = new User(user_id,name);
        try {
            userService.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        out.println("User Created");

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

        int user_id = json.getInt("id");

        User user = null;
        try {
            user = userService.getUserById(user_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(user != null) {
            if (json.has("name")) {
                user.setName(json.getString("name"));
            }
            try {
                userService.updateUser(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            out.println("User updated");
        } else {
            out.println("No such user found");
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

        int user_id = json.getInt("id");

        try {
            userService.deleteUserById(user_id);
            out.println("User deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
