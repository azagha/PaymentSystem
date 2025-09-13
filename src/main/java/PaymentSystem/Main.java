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
                    System.out.println("Enter Currency (USD, EUR, JOD) :");
                    String currency = scanner.next().toUpperCase();
                    System.out.println("Enter Type of Payment (Card, Bank, Wallet) :");
                    String paymentType = scanner.next();

                    PaymentType type;
                    try{
                        type = PaymentType.valueOf(paymentType.toUpperCase());
                    }catch(IllegalArgumentException e){
                        System.out.println("Invalid Payment Type. Allowed Types are: Card, Bank, Wallet");
                        break;
                    }

                    try {
                        Payment payment = switch (type){
                            case CARD -> PaymentFactory.CreateCardPayment(amount, currency);
                            case BANK -> PaymentFactory.CreateBankPayment(amount, currency);
                            case WALLET -> PaymentFactory.CreateWalletPayment(amount, currency);
                        };


                        service.addPayment(payment.getAmount(), payment.getCurrency(), type).ifPresentOrElse(
                                p -> System.out.println("Created :" + p),
                                () -> System.out.println("Payment Rejected"));

                    }  catch (InvalidPaymentException e) {
                        System.out.println("Payment Creation Failed: "+e.getMessage());
                    }
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

                    service.getRefund(id).ifPresentOrElse(
                            refund -> System.out.println("Refund Created: "+refund),
                            () -> System.out.println("Refund Failed, Check Payment Id")
                    );
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