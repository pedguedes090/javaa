package Bai04;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOrderRepository implements OrderRepository {

    private List<String> orders = new ArrayList<>();

    @Override
    public void save(String orderId) {
        orders.add(orderId);
        System.out.println("Lưu đơn hàng vào database: " + orderId);
    }

    @Override
    public List<String> findAll() {
        return orders;
    }

}