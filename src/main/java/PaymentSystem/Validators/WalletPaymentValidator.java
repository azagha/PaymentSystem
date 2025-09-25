package PaymentSystem.Validators;

import PaymentSystem.Configurations.ConfigReader;
import PaymentSystem.Entities.Payment;
import PaymentSystem.InvalidPaymentException;

import java.math.BigDecimal;
import java.util.*;

public class WalletPaymentValidator implements PaymentValidation {
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
            throw new InvalidPaymentException("This  Currency is not allowed for Wallet Payment");
        }
    }
}
