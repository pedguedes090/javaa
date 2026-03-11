import Bai04.evaluatePasswordStrength;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class EvaluatePasswordStrengthTest {
    evaluatePasswordStrength checker = new evaluatePasswordStrength();
    @Test
    void testStrongPassword() {
        String result = checker.evaluatePasswordStrength("Abc123!@");
        assertEquals("Mạnh", result);
    }
    @Test
    void testMissingUppercase() {
        String result = checker.evaluatePasswordStrength("abc123!@");
        assertEquals("Trung bình", result);
    }
    @Test
    void testMissingLowercase() {
        String result = checker.evaluatePasswordStrength("ABC123!@");
        assertEquals("Trung bình", result);
    }
    @Test
    void testMissingNumber() {
        String result = checker.evaluatePasswordStrength("Abcdef!@");
        assertEquals("Trung bình", result);
    }
    @Test
    void testMissingSpecialCharacter() {
        String result = checker.evaluatePasswordStrength("Abc12345");
        assertEquals("Trung bình", result);
    }
    @Test
    void testTooShortPassword() {
        String result = checker.evaluatePasswordStrength("Ab1!");
        assertEquals("Yếu", result);
    }
    @Test
    void testOnlyLowercase() {
        String result = checker.evaluatePasswordStrength("password");
        assertEquals("Yếu", result);
    }
    @Test
    void testUppercaseAndNumbersOnly() {
        String result = checker.evaluatePasswordStrength("ABC12345");
        assertEquals("Yếu", result);
    }
}