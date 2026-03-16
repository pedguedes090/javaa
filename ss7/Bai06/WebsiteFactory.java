package Bai06;

public class WebsiteFactory implements SalesChannelFactory {

    public DiscountStrategy createDiscount() {
        return new WebsiteDiscount();
    }

    public PaymentMethod createPayment() {
        return new CreditCardPayment();
    }

    public NotificationService createNotification() {
        return new EmailNotification();
    }
}