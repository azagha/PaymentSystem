package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Exceptions.InvalidPaymentException;
import PaymentSystem.Validators.WalletPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WalletPaymentValidatorTest {

    private final WalletPaymentValidator validator = new WalletPaymentValidator();

    Customer customer = new Customer("12", "test@example.com", "Test User");
    Merchant merchant = new Merchant("23", "Test Merchant");

    // Valid Case
    @Test
    void testValidWalletPayment() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .paymentType(PaymentType.WALLET)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    // Invalid Case: Negative amount
    @Test
    void testInvalidWalletPaymentWithAmount() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("-10"))
                .currency("JOD")
                .paymentType(PaymentType.WALLET)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    // Invalid Case: Unsupported currency
    @Test
    void testInvalidWalletPaymentWithCurrency() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("500"))
                .currency("SAR")
                .paymentType(PaymentType.WALLET)
                .customer(customer)
                .merchant(merchant)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
