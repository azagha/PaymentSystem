package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;

import java.util.List;

public interface PaymentRepositoryPort {
    void save(Payment payment);
    Payment findById(String id);
    List<Payment> findAll();
}
