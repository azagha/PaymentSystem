package PaymentSystem;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentService {
    private final Map<String, Payment> payments;

    public PaymentService() {
        this.payments = new HashMap<>();
    }


    public Optional<Payment> addPayment(BigDecimal amount, String currency) {
        if(amount == null){
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ONE) < 0){
            Payment rejected = new Payment(amount, currency);
            rejected.setStatus(PaymentStatus.REJECTED);
            payments.put(rejected.getId(), rejected);
            return Optional.of(rejected);
        }
        Payment newPayment = new Payment(amount, currency);
        newPayment.setStatus(PaymentStatus.SUCCESS);
        payments.put(newPayment.getId(), newPayment);
        return Optional.of(newPayment);
    }

    public List<Payment> listPayments() {
        return payments.values().stream().collect(Collectors.toList());
    }

    public boolean refundPayment(String id){
        Payment payment = payments.get(id);
        if(payment != null && payment.getStatus() == PaymentStatus.SUCCESS){
            payment.setStatus(PaymentStatus.REFUNDED);
            return true;
        }
        return false;
    }

}


