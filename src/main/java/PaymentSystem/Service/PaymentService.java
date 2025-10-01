package PaymentSystem.Service;

import PaymentSystem.Configurations.ConfigReader;
import PaymentSystem.Domain.CreatePaymentRequest;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Refund;
import PaymentSystem.PaymentStatus;
import PaymentSystem.Repositories.CustomerRepository;
import PaymentSystem.Repositories.MerchantRepository;
import PaymentSystem.Repositories.PaymentRepository;
import PaymentSystem.Repositories.RefundRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;


    public PaymentService(PaymentRepository paymentRepository, RefundRepository refundRepository, CustomerRepository customerRepository, MerchantRepository merchantRepository) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    // Add Payment
    @Transactional
    public Optional<Payment> addPayment(Payment payment){
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Amount must be ≥ 1");
        }

        List<String> allowedCurrencies = ConfigReader.getCurrencies();
        if (!allowedCurrencies.contains(payment.getCurrency())) {
            throw new IllegalArgumentException("Currency not allowed");
        }

        Customer customer = customerRepository.findById(payment.getCustomer().getId())
                .orElseGet(() -> customerRepository.save(payment.getCustomer()));
        payment.setCustomer(customer);

        Merchant merchant = merchantRepository.findById(payment.getMerchant().getId())
                .orElseGet(() -> merchantRepository.save(payment.getMerchant()));
        payment.setMerchant(merchant);

        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.SUCCESS);
        }

        paymentRepository.save(payment);
        return Optional.of(payment);

    }

    // List Payments "Pagination"
    @Transactional
    public List<Payment> listPayments(int page, int pageSize) {
        return paymentRepository.findAll(PageRequest.of(page - 1, pageSize)).getContent();
    }

    @Transactional
    public boolean refundPayment(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("Payment cannot be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        // Create refund if not exists
        if (payment.getRefund() == null) {
            Refund refund = new Refund(payment, payment.getAmount());

            refund.setPayment(payment);
            payment.setRefund(refund);

        }
        paymentRepository.save(payment);

        return true;
    }

    @Transactional
    public Optional<Payment> createPaymentFromController(CreatePaymentRequest request){

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equal to 1");
        }

        List<String> allowedCurrencies = ConfigReader.getCurrencies();
        if (!allowedCurrencies.contains(request.getCurrency())) {
            throw new IllegalArgumentException("Currency not allowed");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setId(request.getCustomerId());
                    c.setCreatedDate(LocalDateTime.now());
                    return customerRepository.save(c);
                });

        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseGet(() -> {
                    Merchant m = new Merchant();
                    m.setId(request.getMerchantId());
                    return merchantRepository.save(m);
                });


        Payment payment = new Payment(
                request.getAmount(),
                request.getCurrency(),
                request.getPaymentType(),
                customer,
                merchant
        );

        payment.setStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);
        return Optional.of(payment);
    }

    @Transactional
    public List<Payment> findPaymentsByCustomerId(String customerId) {
        return paymentRepository.findAllByCustomer_Id(customerId);
    }



    // Create Refund (one per payment)
    @Transactional
    public Optional<Refund> createRefundforPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS || payment.getRefund() != null) {
            return Optional.empty();
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        Refund refund = new Refund(payment, payment.getAmount());
        payment.setRefund(refund);
        refundRepository.save(refund);

        paymentRepository.save(payment);
        return Optional.of(refund);
    }
}
