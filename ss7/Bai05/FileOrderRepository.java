package Bai05;

import java.util.ArrayList;
import java.util.List;

public class FileOrderRepository implements OrderRepository {

    private List<Order> orders = new ArrayList<>();

    public void save(Order order) {
        orders.add(order);
        System.out.println("Lưu đơn hàng vào file: " + order.getId());
    }

    public List<Order> findAll() {
        return orders;
    }
}