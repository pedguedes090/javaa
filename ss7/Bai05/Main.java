package Bai05;

import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static List<Product> products = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();

    static Map<Integer, DiscountStrategy> discounts = new HashMap<>();
    static Map<Integer, PaymentMethod> payments = new HashMap<>();

    static OrderRepository repo = new FileOrderRepository();
    static NotificationService notify = new EmailNotification();
    static OrderService service = new OrderService(repo, notify);

    static InvoiceGenerator invoice = new InvoiceGenerator();

    public static void main(String[] args) {

        discounts.put(1, new PercentageDiscount(10));
        discounts.put(2, new FixedDiscount(50000));

        payments.put(1, new CODPayment());
        payments.put(2, new CreditCardPayment());
        payments.put(3, new MomoPayment());

        while (true) {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Thêm khách hàng");
            System.out.println("3. Tạo đơn hàng");
            System.out.println("4. Xem danh sách đơn hàng");
            System.out.println("5. Tính tổng doanh thu");
            System.out.println("6. Thêm phương thức thanh toán");
            System.out.println("7. Thêm chiến lược giảm giá");
            System.out.println("0. Thoát");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addProduct();
                    break;

                case 2:
                    addCustomer();
                    break;

                case 3:
                    createOrder();
                    break;

                case 4:
                    showOrders();
                    break;

                case 5:
                    revenue();
                    break;

                case 6:
                    addPayment();
                    break;

                case 7:
                    addDiscount();
                    break;

                case 0:
                    return;
            }
        }
    }

    static void addProduct() {
        System.out.print("Mã: ");
        String id = sc.nextLine();

        System.out.print("Tên: ");
        String name = sc.nextLine();

        System.out.print("Giá: ");
        double price = sc.nextDouble();
        sc.nextLine();

        Product p = new Product(id, name, price);
        products.add(p);
        System.out.println("Đã thêm sản phẩm " + id);
    }

    static void addCustomer() {
        System.out.print("Tên: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        Customer c = new Customer(name, email);
        customers.add(c);
        System.out.println("Đã thêm khách hàng");
    }

    static void createOrder() {
        if (products.isEmpty() || customers.isEmpty()) {
            System.out.println("Chưa có dữ liệu");
            return;
        }
        System.out.println("Chọn khách hàng:");

        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getName());
        }

        int c = sc.nextInt() - 1;

        Customer customer = customers.get(c);

        System.out.println("Chọn sản phẩm:");

        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i).getName());
        }

        int p = sc.nextInt() - 1;

        Product product = products.get(p);

        System.out.print("Số lượng: ");
        int quantity = sc.nextInt();

        Order order = new Order("ORD00" + (orders.size() + 1), customer);

        order.addItem(product, quantity);

        double total = product.getPrice() * quantity;

        System.out.println("Chọn giảm giá:");

        for (Map.Entry<Integer, DiscountStrategy> entry : discounts.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getClass().getSimpleName());
        }

        int d = sc.nextInt();

        DiscountStrategy discount = discounts.get(d);

        double finalTotal = discount.applyDiscount(total);

        System.out.println("Chọn thanh toán:");

        for (Map.Entry<Integer, PaymentMethod> entry : payments.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getClass().getSimpleName());
        }

        int pay = sc.nextInt();

        PaymentMethod payment = payments.get(pay);

        payment.pay(finalTotal);

        order.setTotal(finalTotal);

        service.createOrder(order);

        invoice.generate(order);

        orders.add(order);
    }

    static void showOrders() {
        for (Order o : orders) {
            System.out.println(
                    o.getId() + " - "
                            + o.getCustomer().getName()
                            + " - "
                            + o.getTotal());
        }
    }

    static void revenue() {
        double sum = 0;
        for (Order o : orders) {
            sum += o.getTotal();
        }
        System.out.println("Tổng doanh thu: " + sum);
    }

    static void addPayment() {
        sc.nextLine();
        System.out.print("Tên phương thức: ");
        String name = sc.nextLine();
        payments.put(payments.size() + 1,
                amount -> System.out.println("Thanh toán " + name + ": " + amount));

        System.out.println("Đã thêm phương thức thanh toán " + name);
    }

    static void addDiscount() {
        System.out.print("Nhập % giảm: ");
        double percent = sc.nextDouble();

        discounts.put(discounts.size() + 1,
                new PercentageDiscount(percent));

        System.out.println("Đã thêm chiến lược giảm giá " + percent + "%");
    }
}