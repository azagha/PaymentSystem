package PaymentSystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Refund {
    private final String id;
    private final String paymentId;
    private final BigDecimal amount;
    private final LocalDateTime createdAt;

    public Refund(String paymentId, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.paymentId = paymentId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Refund {Id "+ id + ", PaymentId "
                + paymentId + ", Amount: " + amount +
                ", CreatedAt: " + createdAt + "}";
    }
}
