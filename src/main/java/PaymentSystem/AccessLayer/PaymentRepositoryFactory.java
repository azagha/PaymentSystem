package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class PaymentRepositoryFactory {
    public static PaymentRepositoryPort createPaymentRepository() {
        try {
            InputStream in = PaymentRepositoryFactory.class
                    .getClassLoader()
                    .getResourceAsStream("config/persistence.properties");
            Properties props = new Properties();
            if (in != null) props.load(in);

            String impl = props.getProperty("persistence.impl", "jdbc");

            if ("jdbc".equalsIgnoreCase(impl)) {
                Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
                return new JdbcPaymentRepositoryAdapter(connection);
            } else {
                EntityManager em = JpaFactory.createEntityManager();
                return new JpaPaymentRepositoryAdapter(em);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PaymentRepository", e);
        }
    }
}
