package Bai03;

public class PaymentProcessor {

    private PaymentMethod paymentMethod;

    public PaymentProcessor() {
    }

    public PaymentProcessor(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void processPayment(double amount) {

        if (paymentMethod instanceof CODPayable) {
            ((CODPayable) paymentMethod).processCOD(amount);
        }

        else if (paymentMethod instanceof CardPayable) {
            ((CardPayable) paymentMethod).processCreditCard(amount);
        }

        else if (paymentMethod instanceof EWalletPayable) {
            ((EWalletPayable) paymentMethod).processMomo(amount);
        }

    }

}