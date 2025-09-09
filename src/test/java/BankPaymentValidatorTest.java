import PaymentSystem.Payment;
import PaymentSystem.InvalidPaymentException;
import PaymentSystem.PaymentType;
import PaymentSystem.Validators.BankPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BankPaymentValidatorTest {
    private final BankPaymentValidator validator = new BankPaymentValidator();

    @Test
    void testValidBankPayment() {
        Payment payment = new Payment(new BigDecimal("10"), "USD", PaymentType.BANK);
        BankPaymentValidator validator = new BankPaymentValidator();

        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }
}
