package PaymentSystem;

import PaymentSystem.Domain.CreatePaymentRequest;
import PaymentSystem.Entities.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String customerId;
    private String merchantId;

    @BeforeEach
    void setUp() {
        customerId = "customerId" + UUID.randomUUID();
        merchantId = "merchantId" + UUID.randomUUID();
    }

    private CreatePaymentRequest buildPaymentRequest() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setCustomerId(customerId);
        request.setMerchantId(merchantId);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setPaymentType(PaymentType.CARD);
        return request;
    }

    @ParameterizedTest
    @ValueSource(strings = {"jdbc", "jpa"})
    void testCreatePaymentAndList(String adapter) throws Exception {
        // create payment
        mockMvc.perform(post("/payments")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildPaymentRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        // list paginated
        mockMvc.perform(get("/payments?page=1&size=10")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                // check that at least one payment in the list matches the customerId
                .andExpect(jsonPath("$[*].customerId").value(org.hamcrest.Matchers.hasItem(customerId)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"jdbc", "jpa"})
    void testListPaymentsByCustomer(String adapter) throws Exception {
        // create payment
        mockMvc.perform(post("/payments")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildPaymentRequest())))
                .andExpect(status().isOk());

        // fetch by customer
        mockMvc.perform(get("/payments/customer/" + customerId)
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customerId));
    }

    @ParameterizedTest
    @ValueSource(strings = {"jdbc", "jpa"})
    void testRefundPayment(String adapter) throws Exception {
        // create payment
        String response = mockMvc.perform(post("/payments")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildPaymentRequest())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Payment created = objectMapper.readValue(response, Payment.class);

        // refund payment
        mockMvc.perform(post("/payments/" + created.getId() + "/refund")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(content().string("Refund successful"));
    }
}
