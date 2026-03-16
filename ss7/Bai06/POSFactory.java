package Bai06;

public class POSFactory implements SalesChannelFactory {

    public DiscountStrategy createDiscount() {
        return new POSDiscount();
    }

    public PaymentMethod createPayment() {
        return new CashPayment();
    }

    public NotificationService createNotification() {
        return new PrintNotification();
    }
}