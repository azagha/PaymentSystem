package PaymentSystem;

import PaymentSystem.Entities.Payment;

//Strategy Pattern for Payment Types
public interface PaymentValidation {
    void validatePayment(Payment p) throws InvalidPaymentException;
}
