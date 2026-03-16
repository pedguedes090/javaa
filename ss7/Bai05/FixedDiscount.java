package Bai05;

public class FixedDiscount implements DiscountStrategy {
    private double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }
    public double applyDiscount(double totalAmount) {
        return totalAmount - amount;
    }
}
