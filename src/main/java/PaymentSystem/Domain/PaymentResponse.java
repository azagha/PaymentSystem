package PaymentSystem.Domain;

import PaymentSystem.PaymentType;

import java.math.BigDecimal;

public class PaymentResponse {

    private String Id;
    private BigDecimal amount;
    private String currency;
    private PaymentType paymentType;
    private String status;
    private String customerId;
    private String merchantId;

    public String getId() {return Id;}
    public void setId(String id) {this.Id = id;}

    public BigDecimal getAmount() {return amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}

    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}

    public PaymentType getPaymentType() {return paymentType;}
    public void setPaymentType(PaymentType paymentType) {this.paymentType = paymentType;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getCustomerId() {return customerId;}
    public void setCustomerId(String customerId) {this.customerId = customerId;}

    public String getMerchantId() {return merchantId;}
    public void setMerchantId(String merchantId) {this.merchantId = merchantId;}

}
