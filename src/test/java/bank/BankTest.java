package bank;

import org.cleverbank.dto.Bank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testToString() {
        // Создаем объект Bank для тестирования
        Bank bank = new Bank(1, "CleverBank");

        // Ожидаемая строка
        String expectedString = "Bank{id=1, name='CleverBank'}";

        // Получаем результат вызова метода toString()
        String actualString = bank.toString();

        // Проверяем, что полученная строка соответствует ожидаемой
        Assertions.assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetterAndSetter() {
        // Создаем объект Bank для тестирования
        Bank bank = new Bank();

        // Устанавливаем значения с помощью сеттеров
        bank.setId(1);
        bank.setName("CleverBank");

        // Проверяем, что значения получены с помощью геттеров
        Assertions.assertEquals(1, bank.getId());
        Assertions.assertEquals("CleverBank", bank.getName());
    }

    @Test
    public void testNoArgsConstructor() {
        // Создаем объект Bank с помощью конструктора без аргументов
        Bank bank = new Bank();

        // Проверяем, что значения полей инициализированы значением по умолчанию
        Assertions.assertEquals(0, bank.getId());
        Assertions.assertNull(bank.getName());
    }

    @Test
    public void testAllArgsConstructor() {
        // Создаем объект Bank с помощью конструктора со всеми аргументами
        Bank bank = new Bank(1, "CleverBank");

        // Проверяем, что значения полей инициализированы переданными значениями
        Assertions.assertEquals(1, bank.getId());
        Assertions.assertEquals("CleverBank", bank.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Создаем два объекта Bank с одинаковыми значениями полей
        Bank bank1 = new Bank(1, "CleverBank");
        Bank bank2 = new Bank(1, "CleverBank");

        // Проверяем, что объекты равны друг другу
        Assertions.assertEquals(bank1, bank2);
        Assertions.assertEquals(bank1.hashCode(), bank2.hashCode());

        // Создаем объект Bank с другими значениями полей
        Bank bank3 = new Bank(2, "OtherBank");

        // Проверяем, что объекты не равны друг другу
        Assertions.assertNotEquals(bank1, bank3);
        Assertions.assertNotEquals(bank1.hashCode(), bank3.hashCode());
    }
}