package PaymentSystem.Validators;

import PaymentSystem.Entities.Payment;
import PaymentSystem.InvalidPaymentException;

//Strategy Pattern for Payment Types
public interface PaymentValidation {
    void validatePayment(Payment p) throws InvalidPaymentException;
}
