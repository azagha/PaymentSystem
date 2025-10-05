package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Exceptions.InvalidPaymentException;
import PaymentSystem.Validators.CardPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardPaymentValidatorTest {

    private final CardPaymentValidator validator = new CardPaymentValidator();

    Customer customer = new Customer("11", "test@example.com", "Test User");
    Merchant merchant = new Merchant("12", "Test Merchant");

    // Valid Case
    @Test
    void testValidCardPayment() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("1"))
                .currency("JOD")
                .paymentType(PaymentType.CARD)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    // Invalid Case: Amount
    @Test
    void testInvalidCardPaymentWithAmount() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("0"))
                .currency("USD")
                .paymentType(PaymentType.CARD)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    // Invalid Case: Currency
    @Test
    void testInvalidCardPaymentWithCurrency() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("10"))
                .currency("TL")
                .paymentType(PaymentType.CARD)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
