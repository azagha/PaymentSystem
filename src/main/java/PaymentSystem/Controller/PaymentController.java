package PaymentSystem.Controller;


import PaymentSystem.Domain.CreatePaymentRequest;
import PaymentSystem.Domain.PaymentResponse;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }



    @GetMapping
    public List<PaymentResponse> getPayments() {
        return service.listPayments(1, 10)
                .stream()
                .map(payment -> {
                    PaymentResponse response = new PaymentResponse();
                    response.setId(payment.getId());
                    response.setAmount(payment.getAmount());
                    response.setCurrency(payment.getCurrency());
                    response.setPaymentType(payment.getPaymentType());
                    response.setStatus(payment.getStatus().name());
                    response.setCustomerId(payment.getCustomer().getId());
                    response.setMerchantId(payment.getMerchant().getId());
                    return response;
                })
                .collect(Collectors.toList());
    }


    @PostMapping
    public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request){
        try {
            Payment savedPayment = service.createPaymentFromController(request)
                    .orElseThrow(() -> new RuntimeException("Failed to save payment"));

            PaymentResponse response = new PaymentResponse();
            response.setId(savedPayment.getId());
            response.setAmount(savedPayment.getAmount());
            response.setCurrency(savedPayment.getCurrency());
            response.setPaymentType(savedPayment.getPaymentType());
            response.setStatus(savedPayment.getStatus().name());
            response.setCustomerId(savedPayment.getCustomer().getId());
            response.setMerchantId(savedPayment.getMerchant().getId());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public String refundPayment(@PathVariable String id){
        boolean refund = service.refundPayment(id);

        return "Refund successful";
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getPaymentsByCustomer(@PathVariable String customerId) {
        List<Payment> payments = service.findPaymentsByCustomerId(customerId);

        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No payments found for this Customer");
        }

        List<PaymentResponse> responseList = payments.stream().map(payment -> {
            PaymentResponse response = new PaymentResponse();
            response.setId(payment.getId());
            response.setAmount(payment.getAmount());
            response.setCurrency(payment.getCurrency());
            response.setPaymentType(payment.getPaymentType());
            response.setStatus(payment.getStatus().name());
            response.setCustomerId(payment.getCustomer().getId());
            response.setMerchantId(payment.getMerchant().getId());
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

}
