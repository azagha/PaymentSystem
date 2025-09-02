package PaymentSystem;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private final Map<String, Payment> payments;

    public PaymentService() {
        this.payments = new HashMap<>();
    }


}


