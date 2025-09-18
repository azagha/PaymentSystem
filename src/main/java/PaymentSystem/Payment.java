package PaymentSystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private String id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Transient
    @ToString.Exclude
    private List<Refund> refunds;


    //Constructor For New Payment
    public Payment(BigDecimal amount, String currency, PaymentType paymentType, Customer customer, Merchant merchant) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.PENDING;
        this.paymentType = paymentType;
        this.customer = customer;
        this.merchant = merchant;
        this.createdAt = LocalDateTime.now();
    }

    //Constructor for database retrieval
    public Payment(String id, BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType, Customer customer, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.customer = customer;
        this.createdAt = createdAt;
    }

    //for JDBC/manual mapping
    public Payment(String id, BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.createdAt = createdAt;
        this.refunds = new ArrayList<>();
    }


    //Constructor for JPA, Ensures refunds is never null
    protected Payment(){
        this.refunds = new ArrayList<>();
    }

    public String toString() {
        return "Payment ( id = " + id +
                ", amount = " + amount +
                ", currency = " + currency +
                ", createdAt = " + createdAt +
                ", status = " + status +
                ", paymentType = " + paymentType +
                ", customerId = " + customer.getId() +
                ", merchantId = " + merchant.getId() +
                " )";
    }
}
