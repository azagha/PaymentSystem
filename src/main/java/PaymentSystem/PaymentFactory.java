package PaymentSystem;

import PaymentSystem.Validators.BankPaymentValidator;
import PaymentSystem.Validators.CardPaymentValidator;
import PaymentSystem.Validators.WalletPaymentValidator;

import java.math.BigDecimal;
import java.util.*;

public class PaymentFactory {
    public static Payment createPayment(PaymentType paymentType, BigDecimal amount, String currency) throws InvalidPaymentException {
        Payment payment = new Payment(amount, currency, paymentType);
        payment.setStatus(PaymentStatus.PENDING);

        PaymentValidation validator;
        switch (paymentType) {
            case CARD:
                validator = new CardPaymentValidator();
                break;
            case BANK:
                validator = new BankPaymentValidator();
                break;
            case WALLET:
                validator = new WalletPaymentValidator();
                break;
            default:
                throw new InvalidPaymentException("Unknown Payment Type");
        }
        validator.validatePayment(payment);
        return payment;
    }
}
