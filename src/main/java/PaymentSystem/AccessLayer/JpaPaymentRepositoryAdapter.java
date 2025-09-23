package PaymentSystem.AccessLayer;

import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Refund;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final EntityManager entityManager;

    public JpaPaymentRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Save or update a Payment
    @Override
    public void save(Payment payment) {
        entityManager.getTransaction().begin();
        try {
            if (payment.getId() == null || payment.getId().isEmpty()) {
                payment.setId(UUID.randomUUID().toString());
            }

            entityManager.merge(payment);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error saving payment", e);
        }
    }

    // Find Payment by ID
    @Override
    public Payment findById(String id) {
        return entityManager.find(Payment.class, id);
    }

    // List all Payments with pagination
    @Override
    public List<Payment> findAll(int limit, int offset) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p ORDER BY p.createdAt DESC", Payment.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    // Find Payments by Customer ID
    @Override
    public List<Payment> findPaymentsByCustomerId(long customerId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.customer.id = :custId ORDER BY p.createdAt DESC",
                Payment.class);
        query.setParameter("custId", customerId);
        return query.getResultList();
    }

    // Create or get existing Customer
    @Override
    public Customer createCustomer(long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setCreatedDate(java.time.LocalDateTime.now());
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
        return customer;
    }

    @Override
    public Customer getCustomer(long customerId) {
        return entityManager.find(Customer.class, customerId);
    }

    @Override
    public Merchant createMerchant(long merchantId) {
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        entityManager.getTransaction().begin();
        entityManager.persist(merchant);
        entityManager.getTransaction().commit();
        return merchant;
    }

    @Override
    public Merchant getMerchant(long merchantId) {
        return entityManager.find(Merchant.class, merchantId);
    }

    // Save a Refund
    @Override
    public void saveRefund(Refund refund) {
        entityManager.persist(refund);
    }

    @Override
    public List<Refund> findRefundsByPaymentId(String paymentId) {
        TypedQuery<Refund> query = entityManager.createQuery(
                "SELECT r FROM Refund r WHERE r.payment.id = :paymentId ORDER BY r.createdAt DESC",
                Refund.class
        );
        query.setParameter("paymentId", paymentId);
        return query.getResultList();
    }

}
