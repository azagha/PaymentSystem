import PaymentSystem.*;
import PaymentSystem.AccessLayer.JpaPaymentRepositoryAdapter;
import PaymentSystem.AccessLayer.PaymentRepositoryPort;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Refund;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RefundFlowTest {
    private EntityManager em;
    private PaymentRepositoryPort repository;
    private PaymentService service;

    @BeforeAll
    void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PaymentPU");
        em = emf.createEntityManager();
        repository = new JpaPaymentRepositoryAdapter(em);
        service = new PaymentService(repository);
    }


    @AfterAll
    void end() {
        em.close();
    }

    @Test
    public void testRefundFlowHappyPath() throws InvalidPaymentException {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setFullName("Test User");

        Payment payment = new Payment(new BigDecimal("50"), "JOD", PaymentType.CARD, customer, merchant);
        payment.setStatus(PaymentStatus.SUCCESS);
        repository.save(payment);

        Payment persisted = repository.findById(payment.getId());
        assertNotNull(persisted);
        assertEquals(PaymentStatus.SUCCESS, persisted.getStatus());

        Optional<Refund> refundOptional = service.getRefund(payment.getId());
        assertTrue(refundOptional.isPresent());

        Payment refunded = repository.findById(persisted.getId());
        assertEquals(PaymentStatus.REFUNDED, refunded.getStatus());
    }

    @Test
    public void testRefundFailsForNonExistentPayment() {
        String notExistentPaymentId = "notExistentPaymentId";
        Optional<Refund> refundOptional = service.getRefund(notExistentPaymentId);
        assertTrue(refundOptional.isEmpty(), "Refund should fail for non-existent payment");
    }
}


