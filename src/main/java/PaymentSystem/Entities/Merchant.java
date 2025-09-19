package PaymentSystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "merchants")
public class Merchant {
    @Id
    private Long id;
    private String merchantName;

    public Merchant() {}

    public Merchant(String merchantName) {
        this.merchantName = merchantName;
    }

    public Merchant(long id) {
        this.id = id;
    }

    public Merchant(Long id, String merchantName) {
        this.id = id;
        this.merchantName = merchantName;
    }
}
