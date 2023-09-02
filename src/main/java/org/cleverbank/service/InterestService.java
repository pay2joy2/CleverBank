package org.cleverbank.service;

import lombok.Getter;
import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Getter
public class InterestService {

    private static final ConnFactory cf = ConnFactory.getInstance();

    private int previousMonth;

    /**
     *
     * Gets interest rate from config.yaml file in form of double
     *
     */
    public double getInterestRateFromConfig()
    {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config.yaml");
        Map<String, Object> map = yaml.load(inputStream);
        return (double) map.get("InterestRate");
    }

    /**
     *
     * Function to check if today is last day of the Month
     */

    private boolean isEndOfMonth() {
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        return LocalDate.now().isEqual(endOfMonth);
    }

    /**
     *
     * Function to check if interest was added in this month
     *
     */

    private boolean NotTheSameMonth()
    {
        int currentMonth = LocalDate.now().getMonthValue();
        return currentMonth != previousMonth;
    }

    /**
     *
     * Main function to check for need to add interest to all accounts
     */
    public void CalculateInterest() throws SQLException {
        if(isEndOfMonth() && NotTheSameMonth()) {
            AccountService accountService = new AccountService();
            List<Account> list = accountService.getAllAccounts();
            double interest = getInterestRateFromConfig();
            for (Account account : list) {
                account.setBalance((account.getBalance()*interest) + account.getBalance());
                accountService.updateAccount(account);
            }
            previousMonth = LocalDate.now().getMonthValue();
            System.out.println("INTEREST CHECKED, UPDATED");
        }
        System.out.println("INTEREST CHECKED, NOT UPDATED");
    }
}
