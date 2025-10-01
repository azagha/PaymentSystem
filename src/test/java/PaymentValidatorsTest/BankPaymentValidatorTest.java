package PaymentValidatorsTest;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Exceptions.InvalidPaymentException;
import PaymentSystem.Validators.BankPaymentValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankPaymentValidatorTest {
    private final BankPaymentValidator validator = new BankPaymentValidator();

    Customer customer = new Customer("23", "test@example.com", "Test User");
    Merchant merchant = new Merchant("12", "Test Merchant");

    //Valid Case
    @Test
    void testValidBankPayment() {
        Payment payment = new Payment(new BigDecimal("1"), "USD", PaymentType.BANK, customer, merchant);
        assertDoesNotThrow(() -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidBankPaymentWithAmount() {
        Payment payment = new Payment(new BigDecimal("-1"), "USD", PaymentType.BANK, customer, merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }

    //Invalid Case
    @Test
    void testInvalidBankPaymentWithCurrency() {
        Payment payment = new Payment(new BigDecimal("100"), "AED", PaymentType.BANK, customer, merchant);
        assertThrows(InvalidPaymentException.class, () -> validator.validatePayment(payment));
    }
}
