package PaymentSystem.Entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String fullName;
    private LocalDateTime createdDate;

    public Customer(){}

    public Customer(String id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public Customer(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.createdDate = LocalDateTime.now();
    }

    // For Testing
    public Customer(String id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.createdDate = LocalDateTime.now();
    }

}
