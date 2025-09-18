package PaymentSystem;

import PaymentSystem.AccessLayer.JpaPaymentRepositoryAdapter;
import PaymentSystem.AccessLayer.PaymentRepositoryFactory;
import PaymentSystem.AccessLayer.PaymentRepositoryPort;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        // Hide Hibernate logs temp
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.type").setLevel(Level.SEVERE);

        PaymentRepositoryPort repository = PaymentRepositoryFactory.createPaymentRepository();
        PaymentService service = new PaymentService(repository);
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Choose an Option :");
            System.out.println("1. Create new Payment");
            System.out.println("2. List all Payments");
            System.out.println("3. Refund a Payment");
            System.out.println("4. Exit the System");
            System.out.println("5. list Payments by Customer");
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

                    System.out.println("Enter Customer ID :");
                    long customerId = scanner.nextLong();

                    System.out.println("Enter Merchant ID:");
                    long merchantId = scanner.nextLong();

                    Customer customer = repository.createOrGetCustomer(customerId);
                    Merchant merchant = repository.createOrGetMerchant(merchantId);



                    try {
                        Payment payment = switch (type){
                            case CARD -> PaymentFactory.CreateCardPayment(amount, currency, customer, merchant);
                            case BANK -> PaymentFactory.CreateBankPayment(amount, currency, customer, merchant);
                            case WALLET -> PaymentFactory.CreateWalletPayment(amount, currency, customer, merchant);
                        };

                        Optional<Payment> createdPayment = service.addPayment(payment);
                        if(createdPayment.isPresent()){
                            System.out.println("Payment Created Successfully");
                            System.out.println(
                                    "ID: " + payment.getId()
                                            + " | Amount: " + payment.getAmount()
                                            + " | Currency: " + payment.getCurrency()
                                            + " | PaymentType: " + payment.getPaymentType()
                                            + " | Customer Id: " + payment.getCustomer().getId()
                                            + " | Merchant Id: " + payment.getMerchant().getId()
                                            + " | Status: " + payment.getStatus()
                            );
                        }else{
                            System.out.println("Payment Not Created Successfully");
                        }

                    }  catch (InvalidPaymentException e) {
                        System.out.println("Payment Creation Failed: "+e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter Page Number (Starting from 1) :");
                    int page  = scanner.nextInt();
                    System.out.println("Enter Page Size :");
                    int pageSize = scanner.nextInt();

                    List<Payment> payments = service.listPayments(page, pageSize);
                    if(payments.isEmpty()){
                        System.out.println("No payments found");
                    }else {
                        for(Payment payment : payments){
                            System.out.println(
                                    "ID: " + payment.getId()
                                    + " | Amount: " + payment.getAmount()
                                    + " | Currency: " + payment.getCurrency()
                                    + " | PaymentType: " + payment.getPaymentType()
                                    + " | Type: " + payment.getPaymentType()
                                    + " | Customer Id: " + payment.getCustomer().getId()
                                    + " | Merchant Id: " + payment.getMerchant().getId()
                            );
                        }

                    }
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
                case 5: {
                    System.out.println("Enter Customer ID : ");
                    long customerId = scanner.nextLong();
                    scanner.nextLine();

                    List<Payment> payments = repository.findPaymentsByCustomerId(customerId);

                    if(payments.isEmpty()){
                        System.out.println("No payments found for this Customer");
                    }else{
                        for(Payment payment : payments){
                            System.out.println(
                                    "ID: " + payment.getId()
                                    + " | Amount: " + payment.getAmount()
                                    + " | Currency: " + payment.getCurrency()
                                    + " | PaymentType: " + payment.getPaymentType()
                                    + " | Type: " + payment.getPaymentType()
                                    + " | Customer Id: " + payment.getCustomer().getId()
                                    + " | Merchant Id: " + payment.getMerchant().getId()
                            );
                        }
                    }
                    break;
                }
                default: {
                    System.out.println("Invalid Option");
                }
            }
        }
    }
}