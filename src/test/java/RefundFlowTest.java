import PaymentSystem.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RefundFlowTest {
    @Test
    public void testRefundFlowHappyPath() throws InvalidPaymentException {
        PaymentService service = new PaymentService();
        Merchant merchant = new Merchant(1L, "Test Merchant");

        Customer customer = new Customer("test@example.com", "Test User");
        customer.setId(1L);

        Payment payment = new Payment(new BigDecimal("50"), "JOD", PaymentType.CARD, customer, merchant);

        Optional<Payment> added = service.addPayment(payment);
        assertTrue(added.isPresent());
        Payment addedPayment = added.get();
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());

        Optional<Refund> refundOptional = service.getRefund(payment.getId());
        assertTrue(refundOptional.isPresent());
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }
}
