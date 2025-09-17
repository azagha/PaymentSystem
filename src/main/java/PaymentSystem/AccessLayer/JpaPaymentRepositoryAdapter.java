package PaymentSystem.AccessLayer;
import PaymentSystem.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort{
    private EntityManager entityManager;

    public JpaPaymentRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Payment payment) {
        entityManager.getTransaction().begin();
        entityManager.persist(payment);
        entityManager.getTransaction().commit();
    }

    @Override
    public Payment findById(String id) {
        return entityManager.find(Payment.class, id);
    }

    @Override
    public List<Payment> findAll(int limit, int offset) {
        TypedQuery<Payment> query = entityManager.createQuery("SELECT p FROM Payment p ORDER BY p.createdAt DESC", Payment.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Payment> findCustomerById(long customerId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.customer.id = :custId ORDER BY p.createdAt DESC",
                Payment.class);
        query.setParameter("custId", customerId);
        return query.getResultList();
    }
}
