package account;

import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.Account;
import org.cleverbank.dto.User;
import org.cleverbank.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
public class AccountServiceTest {

    @Mock
    private ConnFactory cf;

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        AccountService.cf = cf;
    }

    @Test
    public void testGetAllAccounts() throws SQLException {
        // Создаем список аккаунтов для возврата из ResultSet
        List<Account> expectedAccounts = new LinkedList<>();
        expectedAccounts.add(new Account(1, 100.0, 1, 1));
        expectedAccounts.add(new Account(2, 200.0, 2, 1));

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true, false);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt(1)).thenReturn(1, 2);
        when(rs.getDouble(2)).thenReturn(100.0, 200.0);
        when(rs.getInt(3)).thenReturn(1, 2);
        when(rs.getInt(4)).thenReturn(1);

        // Вызываем метод getAllAccounts()
        List<Account> actualAccounts = accountService.getAllAccounts();

        // Проверяем, что список аккаунтов совпадает с ожидаемым
        Assertions.assertEquals(expectedAccounts,actualAccounts);

        // Проверяем, что были вызваны нужные методы
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).isBeforeFirst();
        verify(rs, times(3)).next();
        verify(rs, times(2)).getInt(1);
        verify(rs, times(2)).getDouble(2);
        verify(rs, times(2)).getInt(3);
        verify(rs, times(2)).getInt(4);
        verify(conn, times(1)).close();
    }


    @Test
    public void testGetAccountById() throws SQLException {

        Account expectedAccount = new Account(1, 100.0, 1, 1);

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true,false);
        when(rs.next()).thenReturn(true,false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getDouble(2)).thenReturn(100.0);
        when(rs.getInt(3)).thenReturn(1);
        when(rs.getInt(4)).thenReturn(1);

        // Вызываем метод getAccountById()
        Account actualAccount = accountService.getAccountById(1);

        Assertions.assertEquals(expectedAccount, actualAccount);

        // Проверяем, что были вызваны нужные методы
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeQuery();
        verify(rs, times(2)).next();
        verify(rs, times(1)).getInt(1);
        verify(rs, times(1)).getDouble(2);
        verify(rs, times(1)).getInt(3);
        verify(rs, times(1)).getInt(4);
        verify(conn, times(1)).close();
    }

    @Test
    public void testCreateAccount() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        Account account = new Account(1, 100.0, 1, 1);

        // Вызываем метод createAccount()
        accountService.createAccount(account);

        // Проверяем, что были вызваны нужные методы
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).setDouble(2, 100.0);
        verify(ps, times(1)).setInt(3, 1);
        verify(ps, times(1)).setInt(4, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testDeleteAccountById() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод deleteAccountById()
        accountService.deleteAccountById(1);

        // Проверяем, что были вызваны нужные методы
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testUpdateAccount() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Создаем аккаунт для обновления
        Account account = new Account(1, 200.0, 2, 2);

        // Вызываем метод updateAccount()
        accountService.updateAccount(account);

        // Проверяем, что были вызваны нужные методы
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setDouble(1, 200.0);
        verify(ps, times(1)).setInt(2, 2);
        verify(ps, times(1)).setInt(3, 2);
        verify(ps, times(1)).setInt(4, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }
}
