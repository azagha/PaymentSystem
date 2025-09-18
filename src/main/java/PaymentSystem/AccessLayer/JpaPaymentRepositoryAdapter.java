package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;
import PaymentSystem.Customer;
import PaymentSystem.Merchant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final EntityManager entityManager;

    public JpaPaymentRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Payment payment) {
        entityManager.getTransaction().begin();

        Customer customer = createOrGetCustomer(payment.getCustomer().getId());
        payment.setCustomer(customer);

        Merchant merchant = createOrGetMerchant(payment.getMerchant().getId());
        payment.setMerchant(merchant);

        entityManager.persist(payment);

        entityManager.getTransaction().commit();
    }

    @Override
    public Payment findById(String id) {
        return entityManager.find(Payment.class, id);
    }

    @Override
    public List<Payment> findAll(int limit, int offset) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p ORDER BY p.createdAt DESC", Payment.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Payment> findPaymentsByCustomerId(long customerId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.customer.id = :custId ORDER BY p.createdAt DESC",
                Payment.class);
        query.setParameter("custId", customerId);
        return query.getResultList();
    }

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
}
