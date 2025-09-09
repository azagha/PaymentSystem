package PaymentSystem.Validators;

import PaymentSystem.PaymentValidation;
import PaymentSystem.Payment;
import PaymentSystem.InvalidPaymentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BankPaymentValidator implements PaymentValidation {
    private final List<String> allowedCurrencies = Arrays.asList("USD", "EUR", "JOD");

    @Override
    public void validatePayment(Payment p) throws InvalidPaymentException {
        if (p.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new InvalidPaymentException("Amount must be greater than or equal to 1");
        }
        if (!allowedCurrencies.contains(p.getCurrency())) {
            throw new InvalidPaymentException("This Currency is not allowed for Bank Payment");
        }
    }
}
