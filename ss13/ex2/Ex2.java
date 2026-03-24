package ss13.ex2;
/*
 Sau khi tắt Auto-Commit, mọi thay đổi sẽ nằm ở trạng thái chờ xác nhận trên Database.
 Nếu dev chỉ bắt lỗi trong catch rồi in thông báo ra console
 mà không thực hiện conn.rollback(), đây là một lỗi nghiêm trọng.
 Thiếu Rollback sẽ khiến giao dịch bị treo (hanging transaction),
 các bản ghi liên quan đến ví và hóa đơn sẽ bị khóa (lock),
 ngăn cản toàn bộ các tiến trình thanh toán khác truy cập dữ liệu.
*/
import ss13.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Ex2 {
    public void thanhToanVienPhi(int patientId, int invoiceId, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlDeductWallet = "UPDATE Patient_Wallet SET balance = balance - ? WHERE patient_id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeductWallet)) {
                ps1.setDouble(1, amount);
                ps1.setInt(2, patientId);
                ps1.executeUpdate();
            }

            String sqlUpdateInvoice = "UPDATE Invoicesss SET status = 'PAID' WHERE invoice_id = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateInvoice)) {
                ps2.setInt(1, invoiceId);
                ps2.executeUpdate();
            }

            conn.commit();
            System.out.println("Thanh toán hoàn tất!");

        } catch (SQLException e) {
            System.out.println("Lỗi hệ thống: Không thể hoàn tất thanh toán. Chi tiết: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Đã hoàn tác (Rollback) toàn bộ giao dịch để giải phóng tài nguyên!");
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
        new Ex2().thanhToanVienPhi(1, 1, 50000);
    }
}
