package PaymentSystem;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PaymentService service = new PaymentService();
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Choose an Option :");
            System.out.println("1. Create new Payment");
            System.out.println("2. List all Payments");
            System.out.println("3. Refund a Payment");
            System.out.println("4. Exit the System");
            int choice = scanner.nextInt();

            switch (choice){
                case 1: {
                    System.out.println("Enter Amount :");
                    BigDecimal amount = scanner.nextBigDecimal();
                    System.out.println("Enter Currency :");
                    String currency = scanner.next();

                    service.addPayment(amount, currency).ifPresentOrElse(
                            payment -> System.out.println("Created :" + payment),
                            () -> System.out.println("Payment Rejected"));
                    break;
                }
                case 2: {
                    System.out.println("Payments :");
                    service.listPayments().forEach(System.out::println);
                    break;
                }
                case 3: {
                    System.out.println("Enter Payment Id to Refund :");
                    String id  = scanner.next();
                    if(service.refundPayment(id)){
                        System.out.println("Refund Successful");
                    }else{
                        System.out.println("Refund Failed, Check Id");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Exiting System...");
                    return;
                }
                default: {
                    System.out.println("Invalid Option");
                }
            }
        }
    }
}