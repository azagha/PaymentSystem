import PaymentSystem.InvalidPaymentException;
import PaymentSystem.Payment;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.BankPaymentValidator;
import PaymentSystem.Validators.CardPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardPaymentValidatorTest {
    private final CardPaymentValidator validator = new CardPaymentValidator();

    @Test
    void testValidCardPayment() {
        Payment payment = new Payment(new BigDecimal("10"),"JOD", PaymentType.CARD);
        CardPaymentValidator validator = new CardPaymentValidator();

        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    @Test
    void testInvalidCardPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("0"),"USD", PaymentType.CARD);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    @Test
    void testValidCardPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("10"),"TL", PaymentType.CARD);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
