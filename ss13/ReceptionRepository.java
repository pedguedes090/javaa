package ss13;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceptionRepository {

    public List<Bed> getEmptyBeds() {
        List<Bed> list = new ArrayList<>();
        String sql = "SELECT * FROM RHMS_Beds WHERE status = 'Trống'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Bed(rs.getString("bed_code"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void admitPatient(String name, int age, String bedCode, double advancePayment) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String sqlPatient = "INSERT INTO RHMS_Patients (name, age) VALUES (?, ?)";
            int newPatientId = -1;
            try (PreparedStatement ps1 = conn.prepareStatement(sqlPatient, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, name);
                ps1.setInt(2, age);
                ps1.executeUpdate();
                try (ResultSet rs = ps1.getGeneratedKeys()) {
                    if (rs.next()) newPatientId = rs.getInt(1);
                }
            }

            if (newPatientId == -1) throw new Exception("Không thể tạo hồ sơ bệnh nhân!");

            String sqlBed = "UPDATE RHMS_Beds SET status = 'Đã có người' WHERE bed_code = ? AND status = 'Trống'";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlBed)) {
                ps2.setString(1, bedCode);
                int bedRows = ps2.executeUpdate();
                if (bedRows == 0) throw new Exception("Mã giường không hợp lệ hoặc đã có người nằm!");
            }

            String sqlFinance = "INSERT INTO RHMS_Finance (patient_id, advance_payment) VALUES (?, ?)";
            try (PreparedStatement ps3 = conn.prepareStatement(sqlFinance)) {
                ps3.setInt(1, newPatientId);
                ps3.setDouble(2, advancePayment);
                ps3.executeUpdate();
            }

            conn.commit();
            System.out.println("Tiếp nhận bệnh nhân thành công! Mã BN: " + newPatientId);

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }
}
