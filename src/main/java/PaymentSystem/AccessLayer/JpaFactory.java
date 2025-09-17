package PaymentSystem.AccessLayer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaFactory {
    public static EntityManager createEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PaymentPU");
        return emf.createEntityManager();
    }
}
