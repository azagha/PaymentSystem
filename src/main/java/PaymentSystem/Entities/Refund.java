package PaymentSystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "refunds")

public class Refund {
    @Id
    private String id;

    private BigDecimal amount;
    private  LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    public Refund() {}

    public Refund(Payment payment, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.payment = payment;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Refund {Id " + id + ", PaymentId " + payment.getId() +
                ", Amount: " + amount + ", CreatedAt: " + createdAt + "}";
    }

}
