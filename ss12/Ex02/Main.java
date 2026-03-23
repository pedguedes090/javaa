package Ex02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import utils.DB;

public class Main {
    //P1: Nối chuỗi -> lỗi do dấu , và .
    // PreparedStatement -> không phụ thuộc vào locale
    static void main() {
        Scanner sc = new Scanner(System.in);
        String sql = "UPDATE Vitals SET temperature = ?, heart_rate = ? WHERE p_id = ?";
        try(
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setDouble(1, sc.nextInt());
            ps.setInt(2, sc.nextInt());
            ps.setInt(3, sc.nextInt());
            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Cập nhật chỉ số thành công!");
            } else {
                System.out.println("Không tìm thấy bệnh nhân!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
