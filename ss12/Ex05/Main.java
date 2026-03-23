package Ex05;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PatientDAO dao = new PatientDAO();

        while (true) {
            System.out.println("\n===== RHMS MENU =====");
            System.out.println("1. Danh sách bệnh nhân");
            System.out.println("2. Thêm bệnh nhân");
            System.out.println("3. Cập nhật bệnh án");
            System.out.println("4. Xuất viện & tính phí");
            System.out.println("5. Thoát");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    dao.showAll();
                    break;

                case 2:
                    System.out.print("Tên: ");
                    String name = sc.nextLine();

                    System.out.print("Tuổi: ");
                    int age = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Khoa: ");
                    String dept = sc.nextLine();

                    System.out.print("Bệnh: ");
                    String dis = sc.nextLine();

                    System.out.print("Số ngày: ");
                    int days = sc.nextInt();

                    dao.add(name, age, dept, dis, days);
                    break;

                case 3:
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Chẩn đoán mới: ");
                    String newDis = sc.nextLine();

                    dao.updateDiagnosis(id, newDis);
                    break;

                case 4:
                    System.out.print("ID: ");
                    int pid = sc.nextInt();
                    dao.discharge(pid);
                    break;

                case 5:
                    System.out.println("Thoát!");
                    return;
            }
        }
    }
}