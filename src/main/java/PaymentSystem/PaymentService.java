package PaymentSystem;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentService {
    private final Map<String, Payment> payments;
    private final Map<String, Refund> refunds;

    public PaymentService() {
        this.payments = new HashMap<>();
        this.refunds = new HashMap<>();
    }

    //Add Payment
    public Optional<Payment> addPayment(BigDecimal amount, String currency,  PaymentType paymentType) {
        if(amount.compareTo(BigDecimal.ONE) < 0 && amount != null) {
            Payment rejected = new Payment(amount, currency,paymentType);
            rejected.setStatus(PaymentStatus.REJECTED);
            payments.put(rejected.getId(), rejected);
            return Optional.of(rejected);
        }
        Payment newPayment = new Payment(amount, currency,paymentType);
        newPayment.setStatus(PaymentStatus.SUCCESS);
        payments.put(newPayment.getId(), newPayment);
        return Optional.of(newPayment);
    }

    //List Payments
    public List<Payment> listPayments() {
        return payments.values().stream().collect(Collectors.toList());
    }

    //Change Payment Status to Refunded
    public boolean refundPayment(String id){
        Payment payment = payments.get(id);
        if(payment != null && payment.getStatus() == PaymentStatus.SUCCESS){
            payment.setStatus(PaymentStatus.REFUNDED);
            return true;
        }
        return false;
    }

    public Optional<Refund> getRefund(String paymentId) {
        Payment payment = payments.get(paymentId);
        if(payment == null || payment.getStatus() != PaymentStatus.SUCCESS){
            return Optional.empty();
        }

        Refund refund = new Refund(paymentId, payment.getAmount());
        refunds.put(refund.getId(), refund);
        payment.setStatus(PaymentStatus.REFUNDED);
        return Optional.of(refund);

    }

}


