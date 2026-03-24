package ss13.ex1;
/*
 Mặc định, JDBC sử dụng Auto-Commit = true cho mọi kết nối.
 Điều này có nghĩa là sau khi executeUpdate() hoàn tất,
 dữ liệu sẽ được ghi thẳng vào Database mà không cần gọi commit() thủ công.
 Khi gặp phép chia 10 / 0 gây ra ArithmeticException,
 chương trình nhảy thẳng vào catch và bỏ qua phần ghi lịch sử phía sau.
 Nhưng vì lệnh trừ kho thuốc đã được Auto-Commit từ trước,
 nên kết quả là thuốc đã bị trừ nhưng không hề có bản ghi lịch sử nào.
 */
import ss13.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Ex1 {
    public void capPhatThuoc(int medicineId, int patientId) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlUpdateInventory = "UPDATE Medicine_Inventory SET quantity = quantity - 1 WHERE medicine_id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateInventory)) {
                ps1.setInt(1, medicineId);
                ps1.executeUpdate();
            }

            int x = 10 / 0;

            String sqlInsertHistory = "INSERT INTO Prescription_History (patient_id, medicine_id, date) VALUES (?, ?, CURDATE())";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlInsertHistory)) {
                ps2.setInt(1, patientId);
                ps2.setInt(2, medicineId);
                ps2.executeUpdate();
            }

            conn.commit();
            System.out.println("Cấp phát thuốc thành công!");

        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Đã Rollback an toàn!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Ex1().capPhatThuoc(1, 101);
    }
}
