package PaymentSystem.Entities;

import
        java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import PaymentSystem.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Refund refund;

    public Payment(String transactionId, BigDecimal amount, String currency,
                   LocalDateTime paymentDate, PaymentStatus status,
                   PaymentType type, Customer customer,
                   Merchant merchant, Refund refund) {
        this.id = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = paymentDate;
        this.status = status;
        this.paymentType = type;
        this.customer = customer;
        this.merchant = merchant;
        this.refund = refund;
    }

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
