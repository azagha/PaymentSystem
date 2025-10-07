package PaymentSystem.Factory;

import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Exceptions.InvalidPaymentException;
import PaymentSystem.PaymentStatus;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.BankPaymentValidator;
import PaymentSystem.Validators.CardPaymentValidator;
import PaymentSystem.Validators.WalletPaymentValidator;

import java.math.BigDecimal;

public class PaymentFactory {
    public static Payment CreateCardPayment(BigDecimal amount, String currency, Customer customer, Merchant merchant) throws InvalidPaymentException {
        Payment payment =  Payment.builder()
                .amount(amount).currency(currency)
                .paymentType(PaymentType.CARD)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .build();

        payment.setStatus(PaymentStatus.PENDING);
        new CardPaymentValidator().validatePayment(payment);
        return payment;
    }

    public static Payment CreateBankPayment(BigDecimal amount, String currency, Customer customer, Merchant merchant) throws InvalidPaymentException {
        Payment payment = Payment.builder()
                .amount(amount)
                .currency(currency)
                .paymentType(PaymentType.BANK)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .build();

        payment.setStatus(PaymentStatus.PENDING);
        new BankPaymentValidator().validatePayment(payment);
        return payment;
    }

    public static Payment CreateWalletPayment(BigDecimal amount, String currency, Customer customer, Merchant merchant) throws InvalidPaymentException {
        Payment payment = Payment.builder()
                .amount(amount)
                .currency(currency)
                .paymentType(PaymentType.WALLET)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .build();

        payment.setStatus(PaymentStatus.PENDING);
        new WalletPaymentValidator().validatePayment(payment);
        return payment;
    }
}
