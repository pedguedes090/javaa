package Bai05;

public class InvoiceGenerator {

    public void generate(Order order) {

        System.out.println("\n=== HÓA ĐƠN ===");
        System.out.println("Khách: " + order.getCustomer().getName());
        for (OrderItem item : order.getItems()) {
            System.out.println(
                    item.getProduct().getName()
                            + " - SL: "
                            + item.getQuantity()
                            + " - Giá: "
                            + item.getProduct().getPrice()
                            + " - Thành tiền: "
                            + item.getTotal()
            );
        }
        System.out.println("Tổng tiền: " + order.getTotal());
    }
}