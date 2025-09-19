package PaymentSystem.AccessLayer;

import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Refund;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final EntityManager entityManager;

    public JpaPaymentRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Save or update a Payment
    @Override
    public void save(Payment payment) {
        entityManager.getTransaction().begin();

        // Handle Customer
        Customer customer = payment.getCustomer();
        if (customer.getId() == 0 || entityManager.find(Customer.class, customer.getId()) == null) {
            customer.setCreatedDate(java.time.LocalDateTime.now());
            entityManager.persist(customer);
        } else {
            customer = entityManager.merge(customer);
        }
        payment.setCustomer(customer);

        // Handle Merchant
        Merchant merchant = payment.getMerchant();
        if (merchant.getId() == 0 || entityManager.find(Merchant.class, merchant.getId()) == null) {
            entityManager.persist(merchant);
        } else {
            merchant = entityManager.merge(merchant);
        }
        payment.setMerchant(merchant);

        // Ensure ID
        if (payment.getId() == null || payment.getId().isEmpty()) {
            payment.setId(java.util.UUID.randomUUID().toString());
        }

        entityManager.merge(payment);
        entityManager.getTransaction().commit();
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
    public Customer createOrGetCustomer(long customerId) {
        Customer customer = entityManager.find(Customer.class, customerId);
        if (customer == null) {
            customer = new Customer();
            customer.setId(customerId);
            customer.setCreatedDate(java.time.LocalDateTime.now());
            entityManager.persist(customer);
        }
        return customer;
    }

    // Create or get existing Merchant
    @Override
    public Merchant createOrGetMerchant(long merchantId) {
        Merchant merchant = entityManager.find(Merchant.class, merchantId);
        if (merchant == null) {
            merchant = new Merchant();
            merchant.setId(merchantId);
            entityManager.persist(merchant);
        }
        return merchant;
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
