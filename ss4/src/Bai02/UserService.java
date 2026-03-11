package Bai02;
public class UserService {
    public boolean checkAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Tuổi không hợp lệ");
        }
        if (age >= 18) {
            return true;
        }
        return false;
    }
}
