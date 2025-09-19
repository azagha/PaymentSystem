package PaymentSystem.Validators;

import PaymentSystem.ConfigReader;
import PaymentSystem.PaymentValidation;
import PaymentSystem.Entities.Payment;
import PaymentSystem.InvalidPaymentException;

import java.math.BigDecimal;
import java.util.List;

public class BankPaymentValidator implements PaymentValidation {
    private final List<String> allowedCurrencies = ConfigReader.getCurrencies();

    @Override
    public void validatePayment(Payment p) throws InvalidPaymentException {
        if(p == null) {
            throw new InvalidPaymentException("Payment cannot be null");
        }
        if (p.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new InvalidPaymentException("Amount must be greater than or equal to 1");
        }
        if (!allowedCurrencies.contains(p.getCurrency()) || p.getCurrency() == null) {
            throw new InvalidPaymentException("This Currency is not allowed for Bank Payment");
        }
    }
}
