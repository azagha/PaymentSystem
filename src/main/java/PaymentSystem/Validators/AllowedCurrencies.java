package PaymentSystem.Validators;
import java.util.*;


public class AllowedCurrencies {
    private static final List<String> allowedCurrencies = List.of("USD", "EUR", "JOD");

    public static List<String> getAllowedCurrencies() {
        return allowedCurrencies;
    }
}
