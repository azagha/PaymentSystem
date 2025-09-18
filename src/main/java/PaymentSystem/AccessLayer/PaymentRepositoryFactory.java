package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;

public class PaymentRepositoryFactory {
    public static PaymentRepositoryPort createPaymentRepository() {
        try {
            // Load properties
            InputStream in = PaymentRepositoryFactory.class
                    .getClassLoader()
                    .getResourceAsStream("config/persistence.properties");
            Properties props = new Properties();
            if (in != null) props.load(in);

            String impl = props.getProperty("persistence.impl", "jdbc");

            if ("jdbc".equalsIgnoreCase(impl)) {
                // Connect to H2 file-based database
                Connection connection = DriverManager.getConnection(
                        "jdbc:h2:~/paymentdb;AUTO_SERVER=TRUE", "sa", "");

                // Create tables if they don't exist
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                            "id BIGINT PRIMARY KEY)");
                    stmt.execute("CREATE TABLE IF NOT EXISTS merchants (" +
                            "id BIGINT PRIMARY KEY)");
                    stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                            "id VARCHAR(36) PRIMARY KEY," +
                            "amount DECIMAL(10,2)," +
                            "currency VARCHAR(3)," +
                            "status VARCHAR(20)," +
                            "paymentType VARCHAR(20)," +
                            "customer_id BIGINT," +
                            "merchant_id BIGINT," +
                            "created_at TIMESTAMP," +
                            "FOREIGN KEY (customer_id) REFERENCES customers(id)," +
                            "FOREIGN KEY (merchant_id) REFERENCES merchants(id))");
                }

                return new JdbcPaymentRepositoryAdapter(connection);

            } else {
                // JPA implementation
                EntityManager em = JpaFactory.createEntityManager();
                return new JpaPaymentRepositoryAdapter(em);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to create PaymentRepository", e);
        }
    }
}
