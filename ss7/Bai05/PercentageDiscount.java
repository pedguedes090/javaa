package Bai05;

public class PercentageDiscount implements DiscountStrategy {

    private double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    public double applyDiscount(double totalAmount) {
        return totalAmount - totalAmount * percent / 100;
    }
}