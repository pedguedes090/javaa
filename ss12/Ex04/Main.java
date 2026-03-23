package Ex04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DB;

public class Main {
    //Statement → parse + plan nhiều lần
    //Lãng phí tài nguyên
    static void main() {
        String sql = "INSERT INTO Results(data) VALUES(?)";
        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            con.setAutoCommit(false);
            List<TestResult> list = new ArrayList<>();
            for (TestResult tr : list) {
                ps.setString(1, tr.getData());
                ps.addBatch();

                if (list.indexOf(tr) % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
