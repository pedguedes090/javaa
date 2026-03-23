package BTTH;

import utils.DBUtility;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BTTHMain {

    public static void main(String[] args) {
        PrescriptionRevenueService service = new PrescriptionRevenueService();

        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DBUtility.getConnection()) {

            boolean running = true;
            while (running) {
                System.out.println("\n===== BTTH QUẢN LÝ ĐƠN THUỐC =====");
                System.out.println("1. Khởi tạo bảng và thủ tục");
                System.out.println("2. Cập nhật tồn kho thuốc");
                System.out.println("3. Tìm thuốc theo khoảng giá");
                System.out.println("4. Tính tổng tiền một đơn thuốc");
                System.out.println("5. Thống kê doanh thu theo ngày");
                System.out.println("6. Thoát");
                System.out.print("Chọn chức năng: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> initializeDatabase(service, connection);
                    case 2 -> updateStock(service, connection, scanner);
                    case 3 -> searchByPriceRange(service, connection, scanner);
                    case 4 -> calculatePrescriptionTotal(service, connection, scanner);
                    case 5 -> getRevenueByDate(service, connection, scanner);
                    case 6 -> running = false;
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            }

            System.out.println("Kết thúc chương trình.");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối hoặc truy vấn: " + e.getMessage());
        }
    }

    private static void initializeDatabase(PrescriptionRevenueService service, Connection connection) {
        try {
            service.initializeDatabaseObjects(connection);
            System.out.println("Đã tạo bảng và thủ tục thành công.");
        } catch (SQLException e) {
            System.out.println("Không thể khởi tạo dữ liệu: " + e.getMessage());
        }
    }

    private static void updateStock(PrescriptionRevenueService service, Connection connection, Scanner scanner) {
        try {
            System.out.print("Mã thuốc: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Số lượng thêm: ");
            int addedQuantity = Integer.parseInt(scanner.nextLine().trim());
            int affected = service.updateMedicineStock(connection, id, addedQuantity);

            if (affected > 0) {
                System.out.println("Cập nhật tồn kho thành công.");
            } else {
                System.out.println("Không tìm thấy thuốc để cập nhật.");
            }
        } catch (SQLException e) {
            System.out.println("Không thể cập nhật tồn kho: " + e.getMessage());
        }
    }

    private static void searchByPriceRange(PrescriptionRevenueService service, Connection connection, Scanner scanner) {
        try {
            System.out.print("Giá thấp nhất: ");
            double minPrice = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Giá cao nhất: ");
            double maxPrice = Double.parseDouble(scanner.nextLine().trim());

            if (minPrice > maxPrice) {
                double temp = minPrice;
                minPrice = maxPrice;
                maxPrice = temp;
            }

            List<Medicine> medicines = service.findMedicinesByPriceRange(connection, minPrice, maxPrice);
            if (medicines.isEmpty()) {
                System.out.println("Không có thuốc trong khoảng giá đã nhập.");
                return;
            }

            System.out.println("ID | Tên thuốc | Giá | Tồn kho");
            for (Medicine medicine : medicines) {
                System.out.println(
                        medicine.getId() + " | " + medicine.getName() + " | "
                                + medicine.getPrice() + " | "
                                + medicine.getStock());
            }
        } catch (SQLException e) {
            System.out.println("Không thể tìm thuốc: " + e.getMessage());
        }
    }

    private static void calculatePrescriptionTotal(PrescriptionRevenueService service, Connection connection,
            Scanner scanner) {
        try {
            System.out.print("Mã đơn thuốc: ");
            int prescriptionId = Integer.parseInt(scanner.nextLine().trim());
            BigDecimal total = service.calculatePrescriptionTotal(connection, prescriptionId);
            System.out.println("Tổng tiền đơn thuốc: " + total);
        } catch (SQLException e) {
            System.out.println("Không thể tính tổng tiền đơn thuốc: " + e.getMessage());
        }
    }

    private static void getRevenueByDate(PrescriptionRevenueService service, Connection connection, Scanner scanner) {
        try {
            System.out.print("Ngày bán (yyyy-MM-dd): ");
            String dateText = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(dateText);
            BigDecimal revenue = service.getDailyRevenue(connection, date);
            System.out.println("Doanh thu ngày " + date + ": " + revenue);
        } catch (DateTimeParseException e) {
            System.out.println("Ngày không đúng định dạng yyyy-MM-dd.");
        } catch (SQLException e) {
            System.out.println("Không thể thống kê doanh thu: " + e.getMessage());
        }
    }
}
