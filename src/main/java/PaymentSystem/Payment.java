package PaymentSystem;

import java.math.BigDecimal;
import java.util.*;

public class Payment {
    private final String id;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private final List<Refund> refunds;
    private final PaymentType paymentType;

    public Payment(BigDecimal amount, String currency, PaymentType paymentType) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.PENDING;
        this.refunds = new ArrayList<>();
        this.paymentType = paymentType;
    }

    public String getId() {
        return id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getCurrency() {
        return currency;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public PaymentType getType() { return paymentType; }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Payment { Id: " + id +
                ", Amount: " + amount +
                ", Currency: " + currency +
                ", Type: " + paymentType +
                ", Status: " + status + "}";
    }



}
