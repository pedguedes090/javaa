package Bai04;

public class OrderService {

    private OrderRepository orderRepository;
    private NotificationService notificationService;

    public OrderService(OrderRepository orderRepository,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public void createOrder(String orderId) {
        orderRepository.save(orderId);
        notificationService.send(
                "Đơn hàng " + orderId + " đã được tạo", "customer");
    }
}