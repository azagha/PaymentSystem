package PaymentSystem.Domain;

import PaymentSystem.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull(message = "Amount is Required")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;

    @NotNull(message = "Currency is Required")
    private String currency;

    @NotNull(message = "Payment Type is Required")
    private PaymentType paymentType;

    @NotNull(message = "Customer Id is Required")
    private String customerId;

    @NotNull(message = "Merchant Id is Required")
    private String merchantId;

}
