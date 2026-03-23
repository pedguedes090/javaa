package Ex05;

import utils.DB;

import java.sql.*;

public class PatientDAO {
    public void showAll() {
        String sql = "SELECT * FROM Patients";
        try (
                Connection con = DB.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("patient_id") + " | " +
                                rs.getString("full_name") + " | " +
                                rs.getInt("age") + " | " +
                                rs.getString("department")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. Thêm bệnh nhân (PreparedStatement chống SQL Injection)
    public void add(String name, int age, String dept, String diagnosis, int days) {
        String sql = "INSERT INTO Patients(full_name, age, department, diagnosis, admission_days) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, dept);
            ps.setString(4, diagnosis);
            ps.setInt(5, days);

            ps.executeUpdate();
            System.out.println("Thêm bệnh nhân thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDiagnosis(int id, String diagnosis) {
        String sql = "UPDATE Patients SET diagnosis = ? WHERE patient_id = ?";

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, diagnosis);
            ps.setInt(2, id);
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Cập nhật thành công!");
            } else {
                System.out.println("Không tìm thấy bệnh nhân!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void discharge(int id) {
        String sql = "{call CALCULATE_DISCHARGE_FEE(?, ?)}";
        try (
                Connection con = DB.getConnection();
                CallableStatement cs = con.prepareCall(sql);
        ) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.DOUBLE);
            cs.execute();
            double fee = cs.getDouble(2);
            System.out.println("Tổng viện phí: " + fee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}