package ss13.ex4;
/*
 Đầu vào: Không có tham số (lấy hết danh sách bệnh nhân cấp cứu). Đầu ra: List BenhNhanDTO gồm tên + danh sách dịch vụ.
 Có 2 hướng xử lý:
 Hướng 1 (N+1 Query): SELECT 500 bệnh nhân, rồi lặp 500 lần gọi thêm SELECT dịch vụ cho từng người.
 Hướng 2 (LEFT JOIN): Gộp tất cả trong 1 câu SELECT duy nhất với LEFT JOIN giữa BenhNhan và DichVuSuDung.
 Sau đó đưa kết quả lên bộ nhớ và dùng LinkedHashMap để nhóm dữ liệu theo từng bệnh nhân.
 Đánh giá:
 Về Network: Hướng 1 gửi tới 501 queries (rất chậm). Hướng 2 chỉ cần 1 query (nhanh hơn nhiều).
 Về xử lý Java: Hướng 1 code đơn giản hơn. Hướng 2 cần thêm logic gộp bằng Map nhưng không phức tạp.
 Kết luận: Chọn Hướng 2 (LEFT JOIN) để loại bỏ tình trạng chờ 15 giây.
 LEFT JOIN cũng giúp hiển thị cả bệnh nhân chưa sử dụng dịch vụ nào (Bẫy 2).
 */
import ss13.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ex4 {
    public void loadDashboard() {
        String sql = "SELECT b.maBenhNhan, b.ten, d.tenDichVu " +
                "FROM BenhNhan b " +
                "LEFT JOIN DichVuSuDung d ON b.maBenhNhan = d.maBenhNhan";

        Map<Integer, String> benhNhanMap = new LinkedHashMap<>();
        Map<Integer, List<String>> dichVuMap = new LinkedHashMap<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int maBN = rs.getInt("maBenhNhan");
                String tenBN = rs.getString("ten");
                String tenDV = rs.getString("tenDichVu");

                benhNhanMap.putIfAbsent(maBN, tenBN);
                dichVuMap.putIfAbsent(maBN, new ArrayList<>());

                if (tenDV != null) {
                    dichVuMap.get(maBN).add(tenDV);
                }
            }

            for (Integer maBN : benhNhanMap.keySet()) {
                System.out.println("Bệnh nhân: " + benhNhanMap.get(maBN));
                List<String> services = dichVuMap.get(maBN);
                if (services.isEmpty()) {
                    System.out.println("  -> Chưa sử dụng dịch vụ nào");
                } else {
                    for (String dv : services) {
                        System.out.println("  -> Dịch vụ: " + dv);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Ex4().loadDashboard();
    }
}
