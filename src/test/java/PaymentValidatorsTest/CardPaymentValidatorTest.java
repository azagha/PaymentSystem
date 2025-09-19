package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Validators.CardPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardPaymentValidatorTest {
    private final CardPaymentValidator validator = new CardPaymentValidator();

    Customer customer = new Customer(1L, "test@example.com", "Test User");
    Merchant merchant = new Merchant(1L, "Test Merchant");
    //Valid Case
    @Test
    void testValidCardPayment() {
        Payment payment = new Payment(new BigDecimal("1"),"JOD", PaymentType.CARD, customer ,merchant);
        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }
    //Invalid Case
    @Test
    void testInvalidCardPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("0"),"USD", PaymentType.CARD, customer, merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidCardPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("10"),"TL", PaymentType.CARD, customer, merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
