package Bai06;

public class POSDiscount implements DiscountStrategy {
    public double applyDiscount(double total) {
        System.out.println("Áp dụng giảm giá 5% cho khách thành viên");
        return total * 0.95;
    }
}
