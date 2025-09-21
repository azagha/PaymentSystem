package PaymentSystem;

import PaymentSystem.AccessLayer.PaymentRepositoryPort;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Refund;

import java.util.List;
import java.util.Optional;

public class PaymentService {

    private final PaymentRepositoryPort repository;

    public PaymentService(PaymentRepositoryPort repository) {
        this.repository = repository;
    }

    // Add Payment
    public Optional<Payment> addPayment(Payment payment) {
        try {
            // Set initial status
            payment.setStatus(PaymentStatus.SUCCESS);

            Customer customer = repository.getCustomer(payment.getCustomer().getId());
            if (customer == null) {
                customer = repository.createCustomer(payment.getCustomer().getId());
            }
            payment.setCustomer(customer);

            Merchant merchant = repository.getMerchant(payment.getMerchant().getId());
            if (merchant == null) {
                merchant = repository.createMerchant(payment.getMerchant().getId());
            }
            payment.setMerchant(merchant);

            // Save payment in DB
            repository.save(payment);
            return Optional.of(payment);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // List Payments "Pagination"
    public List<Payment> listPayments(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return repository.findAll(pageSize, offset);
    }

    // Change Payment Status to Refunded
    public boolean refundPayment(String id) {
        Payment payment = repository.findById(id);
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            payment.setStatus(PaymentStatus.REFUNDED);
            repository.save(payment);
            return true;
        }
        return false;
    }

    // Create Refund (one per payment)
    public Optional<Refund> createRefundforPayment(String paymentId) {
        Payment payment = repository.findById(paymentId);

        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS || payment.getRefund() != null) {
            return Optional.empty(); // no refund if already refunded or payment invalid
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        Refund refund = new Refund(payment, payment.getAmount());
        payment.setRefund(refund);  // assign single refund
        repository.saveRefund(refund);

        repository.save(payment);   // save updated payment

        return Optional.of(refund);
    }
}
