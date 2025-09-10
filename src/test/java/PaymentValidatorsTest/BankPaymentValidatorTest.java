package PaymentValidatorsTest;

import PaymentSystem.Payment;
import PaymentSystem.InvalidPaymentException;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.BankPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankPaymentValidatorTest {
    private final BankPaymentValidator validator = new BankPaymentValidator();

    //Valid Case
    @Test
    void testValidBankPayment() {
        Payment payment = new Payment(new BigDecimal("1"), "USD", PaymentType.BANK);
        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidBankPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("-1"), "USD", PaymentType.BANK);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidBankPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("100"), "AED", PaymentType.BANK);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
