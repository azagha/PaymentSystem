package PaymentSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "config/config.properties";

    public static List<String> getCurrencies() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            String Currencies = props.getProperty("currencies");
            return Arrays.asList(Currencies.split(","));
        }catch (IOException e){
            e.printStackTrace();
            return List.of();
        }
    }
}
