package PaymentValidatorsTest;

import PaymentSystem.InvalidPaymentException;
import PaymentSystem.Payment;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.CardPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardPaymentValidatorTest {
    private final CardPaymentValidator validator = new CardPaymentValidator();

    //Valid Case
    @Test
    void testValidCardPayment() {
        Payment payment = new Payment(new BigDecimal("1"),"JOD", PaymentType.CARD);
        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }
    //Invalid Case
    @Test
    void testInvalidCardPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("0"),"USD", PaymentType.CARD);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidCardPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("10"),"TL", PaymentType.CARD);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
