package PaymentSystem;

import PaymentSystem.AccessLayer.PaymentRepositoryPort;

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

            // Ensure customer and merchant exist
            repository.createOrGetCustomer(payment.getCustomer().getId());
            repository.createOrGetMerchant(payment.getMerchant().getId());

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

    public Optional<Refund> getRefund(String paymentId) {
        Payment payment = repository.findById(paymentId);
        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS) {
            return Optional.empty();
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        repository.save(payment);

        Refund refund = new Refund(paymentId, payment.getAmount());
        return Optional.of(refund);
    }
}
