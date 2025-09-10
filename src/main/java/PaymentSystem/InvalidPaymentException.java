package PaymentSystem;

//Error that happens when a payment is invalid.
public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
