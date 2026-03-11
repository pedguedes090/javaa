package Bai03;
public class UserProcessor {
    public String emailTest (String email){
        if(email == null||!email.contains("@")){
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        String[] parts = email.split("@");
        if(parts.length != 2||parts[1].isEmpty()){
            throw new IllegalArgumentException("Email không có tên miền");
        }
        return email.toLowerCase();
    }
}
