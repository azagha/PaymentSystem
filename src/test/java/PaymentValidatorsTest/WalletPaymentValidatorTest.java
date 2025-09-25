package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Validators.WalletPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WalletPaymentValidatorTest {
    private final WalletPaymentValidator validator = new WalletPaymentValidator();

    Customer customer = new Customer(1L, "test@example.com", "Test User");
    Merchant merchant = new Merchant(1L, "Test Merchant");

    //Valid Case
    @Test
    void testValidWalletPayment() {
        Payment payment = new Payment(new BigDecimal("1"),"EUR", PaymentType.WALLET, customer, merchant);
        assertDoesNotThrow (() -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidWalletPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("-10"), "JOD", PaymentType.WALLET, customer , merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidWalletPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("500"), "SAR", PaymentType.WALLET, customer, merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
