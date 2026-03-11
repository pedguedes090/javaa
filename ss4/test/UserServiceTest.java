import Bai02.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class UserServiceTest {
    @Test
    void testAge18() {
        UserService service = new UserService();
        int age = 18;
        boolean result = service.checkAge(age);
        assertEquals(true, result);
    }
    @Test
    void testAge17() {
        UserService service = new UserService();
        int age = 17;
        boolean result = service.checkAge(age);
        assertEquals(false, result);
    }
    @Test
    void testAgeNegative() {
        UserService service = new UserService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.checkAge(-1);
        });
    }
}