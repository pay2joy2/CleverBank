package bank;

import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Bank;
import org.cleverbank.service.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BankServiceTest {

    @Mock
    private ConnFactory cf;

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    private BankService bankService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bankService = new BankService();
        BankService.cf = cf;
    }

    @Test
    public void testGetAllBanks() throws SQLException {
        // Создаем ожидаемый список банков
        List<Bank> expectedBanks = new LinkedList<>();
        expectedBanks.add(new Bank(1, "Bank 1"));
        expectedBanks.add(new Bank(2, "Bank 2"));

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true, false);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt(1)).thenReturn(1, 2);
        when(rs.getString(2)).thenReturn("Bank 1", "Bank 2");

        // Вызываем метод getAllBanks()
        List<Bank> actualBanks = bankService.getAllBanks();

        // Проверяем, что полученный список банков соответствует ожидаемому
        Assertions.assertEquals(expectedBanks, actualBanks);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).isBeforeFirst();
        verify(rs, times(3)).next();
        verify(rs, times(2)).getInt(1);
        verify(rs, times(2)).getString(2);
        verify(conn, times(1)).close();
    }

    @Test
    public void testGetBankById() throws SQLException {
        // Создаем ожидаемый банк
        Bank expectedBank = new Bank(1, "Bank 1");

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true, false);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("Bank 1");

        // Вызываем метод getBankById()
        Bank actualBank = bankService.getBankById(1);

        // Проверяем, что полученный банк соответствует ожидаемому
        Assertions.assertEquals(expectedBank, actualBank);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).isBeforeFirst();
        verify(rs, times(2)).next();
        verify(rs, times(1)).getInt(1);
        verify(rs, times(1)).getString(2);
        verify(conn, times(1)).close();
    }

    @Test
    public void testCreateBank() throws SQLException {
        // Создаем банк для создания
        Bank bank = new Bank(1, "Bank 1");

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод createBank()
        bankService.createBank(bank);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).setString(2, "Bank 1");
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testDeleteBankById() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод deleteBankById()
        bankService.deleteBankById(1);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testUpdateBank() throws SQLException {
        // Создаем банк для обновления
        Bank bank = new Bank(1, "Bank 1");

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод updateBank()
        bankService.updateBank(bank);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(1, "Bank 1");
        verify(ps, times(1)).setInt(2, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testGetBankNameById() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true, false);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString(2)).thenReturn("Bank 1");

        // Вызываем метод getBankNameById()
        String bankName = bankService.getBankNameById(1);

        // Проверяем, что полученное имя банка соответствует ожидаемому
        Assertions.assertEquals("Bank 1", bankName);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).isBeforeFirst();
        verify(rs, times(2)).next();
        verify(rs, times(1)).getString(2);
        verify(conn, times(1)).close();
    }
}