package Bai06;

public class PrintNotification implements NotificationService {
    public void send(String message) {
        System.out.println("In hóa đơn giấy: " + message);
    }
}