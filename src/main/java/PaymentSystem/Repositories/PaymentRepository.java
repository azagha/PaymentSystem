package PaymentSystem.Repositories;

import PaymentSystem.Entities.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findAllByCustomer_Id(String customerId);
}
