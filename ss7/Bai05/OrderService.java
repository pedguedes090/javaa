package Bai05;

public class OrderService {

    private OrderRepository orderRepository;
    private NotificationService notificationService;

    public OrderService(OrderRepository repo,
                        NotificationService notification) {

        this.orderRepository = repo;
        this.notificationService = notification;
    }

    public void createOrder(Order order) {

        orderRepository.save(order);

        notificationService.send(
                "Đơn hàng " + order.getId() + " đã được tạo",
                order.getCustomer().getEmail()
        );
    }
}