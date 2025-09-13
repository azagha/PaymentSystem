package PaymentSystem;

import PaymentSystem.Validators.BankPaymentValidator;
import PaymentSystem.Validators.CardPaymentValidator;
import PaymentSystem.Validators.WalletPaymentValidator;

import java.math.BigDecimal;
import java.util.*;

public class PaymentFactory {
    public static Payment CreateCardPayment(BigDecimal amount, String currency) throws InvalidPaymentException {
        Payment payment = new Payment(amount, currency, PaymentType.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        new CardPaymentValidator().validatePayment(payment);
        return payment;
    }

    public static Payment CreateBankPayment(BigDecimal amount, String currency) throws InvalidPaymentException {
        Payment payment = new Payment(amount, currency, PaymentType.BANK);
        payment.setStatus(PaymentStatus.PENDING);
        new BankPaymentValidator().validatePayment(payment);
        return payment;
    }

    public static Payment CreateWalletPayment(BigDecimal amount, String currency) throws InvalidPaymentException {
        Payment payment = new Payment(amount, currency, PaymentType.WALLET);
        payment.setStatus(PaymentStatus.PENDING);
        new WalletPaymentValidator().validatePayment(payment);
        return payment;
    }
}
