package PaymentSystem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private LocalDateTime createdDate;

    public Customer(){}

    public Customer(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.createdDate = LocalDateTime.now();
    }

    // For Testing
    public Customer(Long id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.createdDate = LocalDateTime.now();
    }

}
