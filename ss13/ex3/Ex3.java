package ss13.ex3;

/*
 Đầu vào: maBenhNhan (int) và tienVienPhi (double).
 Đầu ra: In thông báo kết quả ra Console (thành công hoặc mô tả lỗi chi tiết).

 Cách làm: Vô hiệu hóa Auto-Commit từ đầu. Truy vấn SELECT kiểm tra
 số dư ví trước khi trừ tiền (phòng Bẫy 1). Sau mỗi lệnh UPDATE, kiểm tra
 số dòng bị ảnh hưởng (int rows). Nếu rows == 0, throw new SQLException
 để chuyển luồng về catch và thực hiện rollback (phòng Bẫy 2).

 Luồng xử lý: (1) Tạo kết nối -> (2) Tắt Auto-Commit -> (3) Kiểm tra số dư ví ->
 (4) Trừ tiền viện phí -> (5) Giải phóng giường & kiểm tra kết quả -> (6)
 Cập nhật trạng thái bệnh nhân & kiểm tra kết quả -> (7) Xác nhận commit -> (8) Đóng kết nối trong finally.

 */

import ss13.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ex3 {
    public void xuatVienVaThanhToan(int patientId, double hospitalFee) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlCheckBalance = "SELECT balance FROM Patient_Wallet WHERE patient_id = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckBalance)) {
                psCheck.setInt(1, patientId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        double currentBalance = rs.getDouble("balance");
                        // BẪY 1: Bẫy logic nghiệp vụ - Kiểm tra số dư trước khi trừ tiền
                        if (currentBalance < hospitalFee) {
                            throw new SQLException("Số dư không đủ để thanh toán viện phí!");
                        }
                    } else {
                        throw new SQLException("Không tìm thấy ví của bệnh nhân!");
                    }
                }
            }

            String sqlDeduct = "UPDATE Patient_Wallet SET balance = balance - ? WHERE patient_id = ?";
            try (PreparedStatement psDeduct = conn.prepareStatement(sqlDeduct)) {
                psDeduct.setDouble(1, hospitalFee);
                psDeduct.setInt(2, patientId);
                psDeduct.executeUpdate();
            }

            String sqlFreeBed = "UPDATE Beds SET status = 'Trống' WHERE patient_id = ?";
            try (PreparedStatement psBed = conn.prepareStatement(sqlFreeBed)) {
                psBed.setInt(1, patientId);
                int bedRows = psBed.executeUpdate();
                // BẪY 2: Bẫy dữ liệu ảo - Kiểm tra xem có thực sự giải phóng được giường nào không
                if (bedRows == 0) {
                    throw new SQLException("Lỗi: Mã bệnh nhân không tồn tại hoặc không có giường đang sử dụng!");
                }
            }

            String sqlUpdateStatus = "UPDATE Patients SET status = 'Đã xuất viện' WHERE patient_id = ?";
            try (PreparedStatement psPatient = conn.prepareStatement(sqlUpdateStatus)) {
                psPatient.setInt(1, patientId);
                int patientRows = psPatient.executeUpdate();
                if (patientRows == 0) {
                    throw new SQLException("Lỗi: Không thể cập nhật trạng thái bệnh nhân!");
                }
            }

            conn.commit();
            System.out.println("Xuất viện và thanh toán thành công cho bệnh nhân: " + patientId);

        } catch (SQLException e) {
            System.out.println("Giao dịch thất bại: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Hệ thống đã Rollback để bảo toàn dữ liệu.");
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
        Ex3 app = new Ex3();
        System.out.println("--- Test Case 1: Thành công ---");
        app.xuatVienVaThanhToan(101, 100000);

        System.out.println("\n--- Test Case 2: Bệnh nhân không tồn tại ---");
        app.xuatVienVaThanhToan(999, 50000);
    }
}
