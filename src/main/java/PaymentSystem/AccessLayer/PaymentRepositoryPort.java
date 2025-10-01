package PaymentSystem.AccessLayer;

import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Refund;

import java.util.List;
public interface PaymentRepositoryPort {
    void save(Payment payment);
    Payment findById(String id);
    List<Payment> findAll(int limit, int offset);
    List<Payment> findPaymentsByCustomerId(String customerId);

    Customer createCustomer(String customerId);
    Customer getCustomer(String customerId);

    Merchant createMerchant(String merchantId);
    Merchant getMerchant(String merchantId);

    void saveRefund(Refund refund);
    List<Refund> findRefundsByPaymentId(String paymentId);
}
