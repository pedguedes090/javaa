package Bai01;

public class OrderCalculator {
    public double calculateTotal(Order order) {
        double total = 0;
        for (Product p : order.getProducts()) {
            total += p.getPrice();
        }
        return total;
    }

}