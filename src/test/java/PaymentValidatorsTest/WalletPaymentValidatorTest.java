package PaymentValidatorsTest;

import PaymentSystem.Payment;
import PaymentSystem.InvalidPaymentException;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.WalletPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WalletPaymentValidatorTest {
    private final WalletPaymentValidator validator = new WalletPaymentValidator();

    //Valid Case
    @Test
    void testValidWalletPayment() {
        Payment payment = new Payment(new BigDecimal("1"),"EUR", PaymentType.WALLET);
        assertDoesNotThrow (() -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidWalletPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("-10"), "JOD", PaymentType.WALLET);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidWalletPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("500"), "SAR", PaymentType.WALLET);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
