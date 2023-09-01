package user;

import org.cleverbank.db.ConnFactory;
import org.cleverbank.dto.User;
import org.cleverbank.service.UserService;
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
import java.util.List;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private ConnFactory cf;

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService();
        UserService.cf = cf;
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1, "John Doe"));
        expectedUsers.add(new User(2, "Jane Smith"));

        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt(1)).thenReturn(1, 2);
        when(rs.getString(2)).thenReturn("John Doe", "Jane Smith");

        List<User> actualUsers = userService.getAllUsers();

        Assertions.assertEquals(expectedUsers, actualUsers);

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
    public void testGetUserById() throws SQLException {
        User expectedUser = new User(1, "John Doe");

        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.isBeforeFirst()).thenReturn(true);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn("John Doe");

        User actualUser = userService.getUserById(1);

        Assertions.assertEquals(expectedUser, actualUser);

        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeQuery();
        verify(rs, times(1)).isBeforeFirst();
        verify(rs, times(2)).next();
        verify(rs, times(1)).getInt(1);
        verify(rs, times(1)).getString(2);
        verify(conn, times(1)).close();
    }

    @Test
    public void testCreateUser() throws SQLException {
        // Создаем пользователя для создания
        User user = new User(1, "John Doe");

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод createUser()
        userService.createUser(user);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).setString(2, "John Doe");
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testDeleteUserById() throws SQLException {
        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод deleteUserById()
        userService.deleteUserById(1);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setInt(1, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }

    @Test
    public void testUpdateUser() throws SQLException {
        // Создаем пользователя для обновления
        User user = new User(1, "John Doe");

        // Устанавливаем поведение моков
        when(cf.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        // Вызываем метод updateUser()
        userService.updateUser(user);

        // Проверяем, что методы моков были вызваны правильное количество раз
        verify(cf, times(1)).getConnection();
        verify(conn, times(1)).prepareStatement(anyString());
        verify(ps, times(1)).setString(1, "John Doe");
        verify(ps, times(1)).setInt(2, 1);
        verify(ps, times(1)).executeUpdate();
        verify(conn, times(1)).close();
    }
}