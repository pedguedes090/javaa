package Bai04;

public class Main {

    public static void main(String[] args) {
        OrderRepository repo1 = new FileOrderRepository();
        NotificationService notify1 = new EmailService();

        OrderService service1 = new OrderService(repo1, notify1);

        service1.createOrder("ORD001");

        OrderRepository repo2 = new DatabaseOrderRepository();
        NotificationService notify2 = new SMSNotification();

        OrderService service2 = new OrderService(repo2, notify2);

        service2.createOrder("ORD002");

    }
}