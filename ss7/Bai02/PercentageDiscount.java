package Bai02;

public class PercentageDiscount implements DiscountStrategy {
    private double percent;
    public PercentageDiscount() {
    }

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - (totalAmount * percent / 100);
    }
}