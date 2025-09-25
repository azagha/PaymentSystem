package PaymentSystem;

import PaymentSystem.AccessLayer.JpaPaymentRepositoryAdapter;
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
public class PaymentServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private JpaPaymentRepositoryAdapter repo;
    private PaymentService service;

    @BeforeAll
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("PaymentPU");
        em = emf.createEntityManager();
        repo = new JpaPaymentRepositoryAdapter(em);
        service = new PaymentService(repo);
    }

    @AfterAll
    public void tearDown() {
        em.close();
        emf.close();
    }

    @BeforeEach
    public void beginTx() {
        em.getTransaction().begin();
    }

    @AfterEach
    public void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Test
    public void testCreatePayment() {
        Customer customer = new Customer("john@example.com", "John Doe");
        Merchant merchant = new Merchant(1L, "TestMerchant");

        Payment payment = new Payment(
                BigDecimal.valueOf(100),
                "USD",
                PaymentType.CARD,
                customer,
                merchant
        );

        Optional<Payment> created = service.addPayment(payment);
        assertTrue(created.isPresent(), "Payment should be created");

        Payment saved = repo.findById(payment.getId());
        assertNotNull(saved);
        assertEquals(PaymentStatus.SUCCESS, saved.getStatus());
        assertEquals(payment.getAmount(), saved.getAmount());
    }

    @Test
    public void testRefundPaymentSuccessfully() {
        Customer customer = new Customer("jane@example.com", "Jane Doe");
        Merchant merchant = new Merchant(2L, "Merchant2");

        Payment payment = new Payment(
                BigDecimal.valueOf(50),
                "EUR",
                PaymentType.BANK,
                customer,
                merchant
        );

        service.addPayment(payment);

        Optional<Refund> refund = service.createRefundforPayment(payment.getId());
        assertTrue(refund.isPresent(), "Refund should be created");

        Payment updated = repo.findById(payment.getId());
        assertEquals(PaymentStatus.REFUNDED, updated.getStatus());
        assertEquals(payment.getAmount(), refund.get().getAmount());
        assertEquals(payment.getId(), refund.get().getPayment().getId());
    }

    @Test
    public void testRefundFailsForNonExistentPayment() {
        Optional<Refund> refund = service.createRefundforPayment("non-existent-id");
        assertTrue(refund.isEmpty(), "Refund should fail for non-existent payment");
    }

    @Test
    public void testRefundFailsIfAlreadyRefunded() {
        Customer customer = new Customer("alice@example.com", "Alice");
        Merchant merchant = new Merchant(3L, "Merchant3");

        Payment payment = new Payment(
                BigDecimal.valueOf(70),
                "USD",
                PaymentType.WALLET,
                customer,
                merchant
        );

        service.addPayment(payment);

        // First refund succeeds
        Optional<Refund> firstRefund = service.createRefundforPayment(payment.getId());
        assertTrue(firstRefund.isPresent(), "First refund should succeed");

        // Second refund should fail
        Optional<Refund> secondRefund = service.createRefundforPayment(payment.getId());
        assertTrue(secondRefund.isEmpty(), "Second refund should fail if already refunded");
    }
}
