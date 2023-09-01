package org.cleverbank.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.cleverbank.service.InterestService;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet
public class InterestServlet extends HttpServlet {

    /**
     * Servlet that works on start of the program
     * calls to a method .CalculateInterest() method, to continually check the accounts
     * to see if interest should be added
     * Works in its own Thread from executorService
     */
    public InterestServlet()
    {
        InterestService interestService = new InterestService();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            try {
                interestService.CalculateInterest();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        };
        executor.scheduleAtFixedRate(
                task,
                0,
                30,
                TimeUnit.SECONDS
        );
    }

}
