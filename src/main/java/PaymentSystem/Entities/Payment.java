package PaymentSystem.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import PaymentSystem.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Refund refund;

    // Constructor for new payment
    public Payment(BigDecimal amount, String currency, PaymentType paymentType, Customer customer, Merchant merchant) {
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.PENDING;
        this.paymentType = paymentType;
        this.customer = customer;
        this.merchant = merchant;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for database retrieval
    public Payment(String id, BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType, Customer customer, LocalDateTime createdAt) {
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.customer = customer;
        this.createdAt = createdAt;
    }

    // For JDBC/manual mapping without customer and merchant
    public Payment(BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType, LocalDateTime createdAt) {
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
    }

    // Constructor for JDBC / database retrieval including id
    public Payment(String id, BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType,
                   Customer customer, Merchant merchant, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.customer = customer;
        this.merchant = merchant;
        this.createdAt = createdAt;
    }



    public Payment(String id) {
        this.id = id;
    }

    public Payment(BigDecimal amount, String currency, PaymentStatus status, PaymentType paymentType, Customer customer, Merchant merchant, LocalDateTime createdAt) {
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentType = paymentType;
        this.customer = customer;
        this.merchant = merchant;
        this.createdAt = createdAt;
    }

    // Constructor for JPA
    protected Payment() {}

    @Override
    public String toString() {
        return "Payment ( id = " + id +
                ", amount = " + amount +
                ", currency = " + currency +
                ", createdAt = " + createdAt +
                ", status = " + status +
                ", paymentType = " + paymentType +
                ", customerId = " + (customer != null ? customer.getId() : null) +
                ", merchantId = " + (merchant != null ? merchant.getId() : null) +
                " )";
    }
}
