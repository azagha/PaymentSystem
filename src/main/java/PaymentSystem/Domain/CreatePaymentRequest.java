package PaymentSystem.Domain;

import PaymentSystem.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

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

    public BigDecimal getAmount() {return amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}

    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}

    public PaymentType getPaymentType() {return paymentType;}
    public void setPaymentType(PaymentType paymentType) {this.paymentType = paymentType;}

    public String getCustomerId() {return customerId;}
    public void setCustomerId(String customerId) {this.customerId = customerId;}

    public String getMerchantId() {return merchantId;}
    public void setMerchantId(String merchantId) {this.merchantId = merchantId;}


}
