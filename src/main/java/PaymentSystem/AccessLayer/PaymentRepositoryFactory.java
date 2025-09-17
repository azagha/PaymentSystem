package PaymentSystem.AccessLayer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class PaymentRepositoryFactory {
    public static PaymentRepositoryPort createPaymentRepository() {
        try{
            InputStream in = PaymentRepositoryFactory.class
                    .getClassLoader()
                    .getResourceAsStream("config/persistence.properties");
            Properties props = new Properties();
            props.load(in);

            String impl = props.getProperty("persistence.impl", "jdbc");

            if(("jdbc".equalsIgnoreCase(impl))){
                Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa","");
                return new JdbcPaymentRepositoryAdapter(connection);
            }else{
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("PaymentPU");
                EntityManager em = emf.createEntityManager();
                return new JpaPaymentRepositoryAdapter(em);
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to create PaymentRepository", e);
        }
    }
}
