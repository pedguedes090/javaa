package Bai02;

public class FixedDiscount implements DiscountStrategy {
    private double amount;

    public FixedDiscount() {
    }

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - amount;
    }
}