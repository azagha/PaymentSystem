package PaymentSystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "merchants")
public class Merchant {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;
    @Column(nullable = true)
    private String merchantName;

    public Merchant(String merchantName) {
        this.merchantName = merchantName;
    }

    public Merchant(String id, boolean isId) {
        this.id = id;
        this.merchantName = "Default Merchant";
    }

    public Merchant(String id, String merchantName) {
        this.id = id;
        this.merchantName = merchantName;
    }
}
