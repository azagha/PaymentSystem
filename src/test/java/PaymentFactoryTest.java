import PaymentSystem.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentFactoryTest {
    Customer customer = new Customer(1L, "test@example.com", "Test User");
    Merchant merchant = new Merchant(1L, "Test Merchant");
    @Test
    void testCreateWalletPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.CreateWalletPayment(new BigDecimal("100"), "EUR", customer, merchant);
        assertEquals(PaymentType.WALLET, payment.getPaymentType(), "Payment type should be WALLET");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreateCardPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.CreateCardPayment(new BigDecimal("20"), "JOD", customer, merchant);
        assertEquals(PaymentType.CARD, payment.getPaymentType(), "Payment type should be CARD");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreateBankPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.CreateBankPayment(new BigDecimal("50"), "USD", customer, merchant);
        assertEquals(PaymentType.BANK, payment.getPaymentType(), "Payment type should be BANK");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreatePaymentWithInvalidAmount(){
        assertThrows(InvalidPaymentException.class, () ->
                PaymentFactory.CreateWalletPayment(new BigDecimal("-10"), "EUR", customer, merchant));
    }

    @Test
    void testCreatePaymentWithInvalidCurrency(){
        assertThrows(InvalidPaymentException.class, () ->
                PaymentFactory.CreateCardPayment(new BigDecimal("100"), "AED", customer, merchant));
    }
}
