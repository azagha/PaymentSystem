package PaymentSystem.Domain;

import PaymentSystem.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentResponse {

    private String Id;
    private BigDecimal amount;
    private String currency;
    private PaymentType paymentType;
    private String status;
    private String customerId;
    private String merchantId;


}
