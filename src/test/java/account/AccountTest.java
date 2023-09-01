package account;

import org.cleverbank.dto.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account(1, 100.0, 1, 1);
    }

    @Test
    public void testGetId() {
        // Проверяем, что метод getId() возвращает правильное значение
        Assertions.assertEquals(1, account.getId());
    }

    @Test
    public void testSetId() {
        // Устанавливаем новое значение для id
        account.setId(2);

        // Проверяем, что значение id было успешно изменено
        Assertions.assertEquals(2, account.getId());
    }

    @Test
    public void testGetBalance() {
        // Проверяем, что метод getBalance() возвращает правильное значение
        Assertions.assertEquals(100.0, account.getBalance());
    }

    @Test
    public void testSetBalance() {
        // Устанавливаем новое значение для balance
        account.setBalance(200.0);

        // Проверяем, что значение balance было успешно изменено
        Assertions.assertEquals(200.0, account.getBalance());
    }

    @Test
    public void testGetBankId() {
        // Проверяем, что метод getBankId() возвращает правильное значение
        Assertions.assertEquals(1, account.getBank_id());
    }

    @Test
    public void testSetBankId() {
        // Устанавливаем новое значение для bankId
        account.setBank_id(2);

        // Проверяем, что значение bankId было успешно изменено
        Assertions.assertEquals(2, account.getBank_id());
    }

    @Test
    public void testGetUserId() {
        // Проверяем, что метод getUserId() возвращает правильное значение
        Assertions.assertEquals(1, account.getUser_id());
    }

    @Test
    public void testSetUserId() {
        // Устанавливаем новое значение для userId
        account.setUser_id(2);

        // Проверяем, что значение userId было успешно изменено
        Assertions.assertEquals(2, account.getUser_id());
    }

    @Test
    public void testAllArgsConstructor() {
        // Проверяем, что значения полей инициализированы переданными значениями
        Assertions.assertEquals(1, account.getId());
        Assertions.assertEquals(100.0, account.getBalance());
        Assertions.assertEquals(1, account.getBank_id());
        Assertions.assertEquals(1, account.getUser_id());
    }

    @Test
    public void testNoArgsConstructor() {
        // Создаем объект Account с помощью конструктора без аргументов
        Account account = new Account();

        // Проверяем, что значения полей инициализированы значением по умолчанию
        Assertions.assertEquals(0, account.getId());
        Assertions.assertEquals(0.0, account.getBalance());
        Assertions.assertEquals(0, account.getBank_id());
        Assertions.assertEquals(0, account.getUser_id());
    }
}
