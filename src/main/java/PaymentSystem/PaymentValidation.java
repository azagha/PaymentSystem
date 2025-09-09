package PaymentSystem;

public interface PaymentValidation {
    void validatePayment(Payment p) throws InvalidPaymentException;
}
