package Bai04;
public class evaluatePasswordStrength {
    public String evaluatePasswordStrength(String password) {
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        if (password.length() >= 8 && hasLower && hasUpper && hasNumber && hasSpecial) {
            return "Mạnh";
        }
        else if (password.length() >= 8 && ((hasLower && hasUpper && hasNumber) || (hasLower && hasUpper && hasSpecial) || (hasLower && hasNumber && hasSpecial) || (hasUpper && hasNumber && hasSpecial))) {
            return "Trung bình";
        }
        else {
            return "Yếu";
        }
    }
}