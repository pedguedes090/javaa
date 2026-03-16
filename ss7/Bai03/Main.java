package Bai03;

public class Main {

    public static void main(String[] args) {

        PaymentProcessor processor = new PaymentProcessor();
        // COD
        processor.setPaymentMethod(new CODPayment());
        processor.processPayment(500000);
        // Thẻ tín dụng
        processor.setPaymentMethod(new CreditCardPayment());
        processor.processPayment(1000000);
        // Momo
        processor.setPaymentMethod(new MomoPayment());
        processor.processPayment(750000);
        // KIểm tra
        processor.setPaymentMethod(new MomoPayment());
        processor.processPayment(1000000);
        System.out.println("Chương trình vẫn chạy đúng");
    }
}