import PaymentSystem.Payment;
import PaymentSystem.PaymentFactory;
import PaymentSystem.PaymentType;
import PaymentSystem.PaymentStatus;
import PaymentSystem.InvalidPaymentException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentFactoryTest {
    @Test
    void testCreateWalletPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.createPayment(PaymentType.WALLET, new BigDecimal("100"), "EUR");
        assertEquals(PaymentType.WALLET, payment.getType(), "Payment type should be WALLET");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreateCardPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.createPayment(PaymentType.CARD, new BigDecimal("20"), "JOD");
        assertEquals(PaymentType.CARD, payment.getType(), "Payment type should be CARD");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreateBankPayment() throws InvalidPaymentException {
        Payment payment = PaymentFactory.createPayment(PaymentType.BANK, new BigDecimal("50"), "USD");
        assertEquals(PaymentType.BANK, payment.getType(), "Payment type should be BANK");
        assertEquals(PaymentStatus.PENDING, payment.getStatus(), "Initial status should be PENDING");
    }

    @Test
    void testCreatePaymentWithInvalidAmount(){
        assertThrows(InvalidPaymentException.class, () ->
                PaymentFactory.createPayment(PaymentType.WALLET, new BigDecimal("-10"), "EUR"));
    }

    @Test
    void testCreatePaymentWithInvalidCurrency(){
        assertThrows(InvalidPaymentException.class, () ->
                PaymentFactory.createPayment(PaymentType.WALLET, new BigDecimal("100"), "AED"));
    }
}
