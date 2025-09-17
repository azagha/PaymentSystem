package PaymentSystem;

import java.util.*;
import java.util.stream.Collectors;

public class PaymentService {
    private final Map<String, Payment> payments;
    private final Map<String, Refund> refunds;

    public PaymentService() {
        this.payments = new HashMap<>();
        this.refunds = new HashMap<>();
    }

    // Add Payment
        public Optional<Payment> addPayment(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
        payments.put(payment.getId(), payment);
        return Optional.of(payment);
    }

    // List Payments "Pagination"
    public List<Payment> listPayments(int page, int pageSize) {
        List<Payment> allPayments = payments.values().stream().sorted(Comparator.comparing(Payment::getCreatedAt)).collect(Collectors.toList());

        // Pagination Logic
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize,  allPayments.size());

        if(start >= allPayments.size() || start < 0) {
            return Collections.emptyList();
        }

        return allPayments.subList(start, end);
    }

    // Change Payment Status to Refunded
    public boolean refundPayment(String id) {
        Payment payment = payments.get(id);
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            payment.setStatus(PaymentStatus.REFUNDED);
            return true;
        }
        return false;
    }

    public Optional<Refund> getRefund(String paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS) {
            return Optional.empty();
        }

        Refund refund = new Refund(paymentId, payment.getAmount());
        refunds.put(refund.getId(), refund);
        payment.setStatus(PaymentStatus.REFUNDED);
        return Optional.of(refund);
    }
}
