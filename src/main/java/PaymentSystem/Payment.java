package PaymentSystem;

import java.math.BigDecimal;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Payment {
    private final String id;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;

    public Payment(BigDecimal amount, String currency) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.PENDING;
    }

}
