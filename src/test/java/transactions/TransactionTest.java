package transactions;

import org.junit.jupiter.api.Test;
import org.cleverbank.dto.Transaction;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {
    @Test
    public void testTransactionConstructor() {
        UUID uuid = UUID.randomUUID();
        int fromAccount = 12345;
        int toAccount = 54321;
        double amount = 100.0;
        String type = "transfer";
        Date datestamp = new Date();
        Time timestamp = new Time(System.currentTimeMillis());
        String bankFrom = "Bank A";


        Transaction transaction = new Transaction(uuid, fromAccount, toAccount, amount, type, datestamp, timestamp, bankFrom);

        assertEquals(uuid, transaction.getUuid());
        assertEquals(fromAccount, transaction.getFromAccount());
        assertEquals(toAccount, transaction.getToAccount());
        assertEquals(amount, transaction.getAmount(), 0.001);
        assertEquals(type, transaction.getType());
        assertEquals(datestamp, transaction.getDatestamp());
        assertEquals(timestamp, transaction.getTimestamp());
        assertEquals(bankFrom, transaction.getBankFrom());
    }

    @Test
    public void testTransactionListConstructor() {
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        String type = "transfer";
        double amount = 100.0;
        int fromAccount = 12345;
        int toAccount = 54321;

        Transaction transaction = new Transaction(date, type, amount, fromAccount, toAccount);

        assertEquals(date, transaction.getDatestamp());
        assertEquals(type, transaction.getType());
        assertEquals(amount, transaction.getAmount(), 0.001);
        assertEquals(fromAccount, transaction.getFromAccount());
        assertEquals(toAccount, transaction.getToAccount());
    }
}
