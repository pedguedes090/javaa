package ss13.ex5;
/*
 Nhận diện rủi ro:
 Sai kiểu dữ liệu: Khi người dùng gõ chữ (ví dụ: "Năm trăm") thay vì số vào ô tuổi hoặc tiền,
 nếu thiếu try-catch bọc Double.parseDouble() thì app sẽ crash ngay.
 Dữ liệu không hợp lệ: Mã giường sai hoặc giường đã được sử dụng.
 Nếu bỏ qua việc kiểm tra Row Affected == 0, hệ thống thu tiền xong nhưng bệnh nhân lại không được gán giường.
 Mất kết nối giữa chừng: INSERT bệnh nhân xong nhưng mạng đứt trước khi UPDATE giường.
 Phải dùng Transaction + Rollback để tránh tạo ra dữ liệu không nhất quán.

 Quy trình xử lý:
 B1: Nhận dữ liệu từ người dùng (Tên, Tuổi, Mã giường, Tiền tạm ứng).
 B2: Thiết lập kết nối DB, tắt Auto-Commit bằng conn.setAutoCommit(false).
 B3: INSERT hồ sơ bệnh nhân vào RHMS_Patients, lấy patient_id tự sinh qua getGeneratedKeys().
 B4: UPDATE trạng thái giường trong RHMS_Beds sang 'Đã có người'.
 Kiểm tra: WHERE bed_code = ? AND status = 'Trống'. Nếu RowAffected == 0 thì giường không khả dụng, ném Exception chuyển sang B7.
 B5: INSERT thông tin tài chính vào RHMS_Finance.
 B6 (OK): Tất cả thành công -> commit() lưu vĩnh viễn và thông báo kết quả.
 B7 (Lỗi): Bất kỳ bước nào thất bại -> catch bắt lỗi, gọi rollback() hủy toàn bộ thao tác dở dang. Đóng connection.

 Cấu trúc bảng:
 RHMS_Patients: id (INT, PK, AUTO_INCREMENT) - mã bệnh nhân; name (VARCHAR) - họ tên; age (INT) - tuổi.
 RHMS_Beds: bed_code (VARCHAR, PK) - mã giường; status (VARCHAR) - 'Trống' hoặc 'Đã có người'.
 RHMS_Finance: patient_id (INT, FK) - liên kết với RHMS_Patients; advance_payment (DOUBLE) - tiền tạm ứng.
 */

import ss13.Bed;
import ss13.ReceptionRepository;
import java.util.Scanner;

public class Ex5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ReceptionRepository repo = new ReceptionRepository();

        while (true) {
            System.out.println("\n=== HỆ THỐNG TIẾP NHẬN 1 CHẠM ===");
            System.out.println("1. Xem giường trống");
            System.out.println("2. Tiếp nhận bệnh nhân");
            System.out.println("3. Thoát");
            System.out.print("Chọn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    for (Bed b : repo.getEmptyBeds()) {
                        System.out.println("Giường: " + b.getBedCode() + " - " + b.getStatus());
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Tên bệnh nhân: ");
                        String name = sc.nextLine();
                        System.out.print("Tuổi: ");
                        int age = Integer.parseInt(sc.nextLine());
                        System.out.print("Mã giường: ");
                        String bedCode = sc.nextLine();
                        System.out.print("Tiền tạm ứng: ");
                        double payment = Double.parseDouble(sc.nextLine());

                        repo.admitPatient(name, age, bedCode, payment);
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi: Dữ liệu tuổi hoặc tiền phải là số!");
                    } catch (Exception e) {
                        System.out.println("Giao dịch thất bại: " + e.getMessage());
                    }
                    break;
                case "3":
                    return;
            }
        }
    }
}
