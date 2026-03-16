package Bai05;

public class HolidayDiscount implements DiscountStrategy {
    public double applyDiscount(double totalAmount) {
        return totalAmount * 0.85;
    }
}