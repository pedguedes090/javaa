import Bai03.UserProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class UserProcessorTest {
    private UserProcessor processor;
    @BeforeEach
    void setUp() {
        processor = new UserProcessor();
    }
    @Test
    void TC01() {
        String result = processor.emailTest("user@gmail.com");
        assertEquals("user@gmail.com", result);
    }
    @Test
    void TC02() {
        assertThrows(IllegalArgumentException.class, () -> {
            processor.emailTest("usergmail.com");
        });
    }
    @Test
    void TC03() {
        assertThrows(IllegalArgumentException.class, () -> {
            processor.emailTest("user@");
        });
    }
    @Test
    void TC04() {
        String result = processor.emailTest("Example@Gmail.com");
        assertEquals("example@gmail.com", result);
    }
}