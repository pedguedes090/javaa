package Bai05;

public class SMSNotification implements NotificationService {
    public void send(String message, String recipient) {
        System.out.println("Gửi SMS đến " + recipient + ": " + message);
    }
}