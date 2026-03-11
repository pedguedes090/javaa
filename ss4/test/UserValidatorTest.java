import Bai01.UserValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTest {
    @Test
     void TC01_validUsername() {
        String username = "user123";
        boolean result = UserValidator.isValidUsername(username);
        assertTrue(result);
    }
    @Test
    void TC02_tooShortUsername() {
        String username = "abc";
        boolean result = UserValidator.isValidUsername(username);
        assertFalse(result);
    }
    @Test
    void TC03_containsWhitespace() {
        String username = "user name";
        boolean result = UserValidator.isValidUsername(username);
        assertFalse(result);
    }
}

