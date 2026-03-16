package Bai02;

public class Main {

    public static void main(String[] args) {

        double total = 1000000;

        // PercentageDiscount 10%
        OrderCalculator calculator1 = new OrderCalculator(new PercentageDiscount(10));
        double result1 = calculator1.calculate(total);
        System.out.println("Số tiền sau giảm: " + result1);

        // FixedDiscount 50.000
        OrderCalculator calculator2 = new OrderCalculator(new FixedDiscount(50000));
        double result2 = calculator2.calculate(total);
        System.out.println("Số tiền sau giảm: " + result2);

        // NoDiscount
        OrderCalculator calculator3 = new OrderCalculator(new NoDiscount());
        double result3 = calculator3.calculate(total);
        System.out.println("Số tiền sau giảm: " + result3);

        // HolidayDiscount 15%
        OrderCalculator calculator4 = new OrderCalculator(new HolidayDiscount());
        double result4 = calculator4.calculate(total);
        System.out.println("Số tiền sau giảm: " + result4);

    }
}