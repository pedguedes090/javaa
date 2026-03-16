package Bai06;

public class PushNotification implements NotificationService {
    public void send(String message) {
        System.out.println("Gửi push notification: " + message);
    }
}