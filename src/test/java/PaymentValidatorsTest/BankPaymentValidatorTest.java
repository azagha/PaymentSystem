package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Exceptions.InvalidPaymentException;
import PaymentSystem.Validators.BankPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BankPaymentValidatorTest {

    private final BankPaymentValidator validator = new BankPaymentValidator();

    Customer customer = new Customer("23", "test@example.com", "Test User");
    Merchant merchant = new Merchant("12", "Test Merchant");

    // Valid Case
    @Test
    void testValidBankPayment() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("1"))
                .currency("USD")
                .paymentType(PaymentType.BANK)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    // Invalid Case: Negative amount
    @Test
    void testInvalidBankPaymentWithAmount() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("-1"))
                .currency("USD")
                .paymentType(PaymentType.BANK)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    // Invalid Case: Unsupported currency
    @Test
    void testInvalidBankPaymentWithCurrency() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("100"))
                .currency("AED") // invalid for BankPaymentValidator
                .paymentType(PaymentType.BANK)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
