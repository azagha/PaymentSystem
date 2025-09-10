import PaymentSystem.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RefundFlowTest {
    @Test
    public void testRefundFlowHappyPath() throws InvalidPaymentException {
        PaymentService service = new PaymentService();

        Optional<Payment> added = service.addPayment(new BigDecimal("50"), "JOD", PaymentType.CARD);
        assertTrue(added.isPresent());
        Payment payment = added.get();
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());

        Optional<Refund> refundOptional = service.getRefund(payment.getId());
        assertTrue(refundOptional.isPresent());
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }
}
