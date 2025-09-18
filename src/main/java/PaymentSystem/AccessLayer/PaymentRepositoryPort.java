package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;
import PaymentSystem.Customer;
import PaymentSystem.Merchant;
import java.util.List;

public interface PaymentRepositoryPort {
    void save(Payment payment);
    Payment findById(String id);
    List<Payment> findAll(int limit, int offset);
    List<Payment> findPaymentsByCustomerId(long customerId);
    Customer createOrGetCustomer(long customerId);
    Merchant createOrGetMerchant(long merchantId);
}
