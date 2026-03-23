package Ex01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import utils.DB;

public class Main {
    //P1: PreparedStatement tách SQL và dữ liệu, nên input không thể thay đổi cấu trúc câu lệnh -> chống SQL Injection
    static void main() {
        String code = "doc01";
        String pass = "123456";

        String sql = "SELECT * FROM Doctors WHERE code = ? AND password = ?";
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Đăng nhập thành công");
            } else {
                System.out.println("Sai tài khoản");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
