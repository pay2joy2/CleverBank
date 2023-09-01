package user;

import org.cleverbank.dto.User;
import org.cleverbank.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testToString() {
        User user = new User(1, "John Doe");
        String expectedString = "User{id=1, name='John Doe'}";
        String actualString = user.toString();
        Assertions.assertEquals(expectedString, actualString);
    }

    @Test
    void NoArgsConstructor(){
        User user = new User();
        Assertions.assertEquals(0, user.getId());
        Assertions.assertNull(user.getName());

    }

    @Test
    public void testGetterAndSetter() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("John Doe", user.getName());
    }

}
