package PaymentSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "config/config.properties";

    public static List<String> getCurrencies() {
        Properties props = new Properties();

        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new RuntimeException("Cannot find " + CONFIG_FILE + " in classpath");
            }

            props.load(in);
            String currencies = props.getProperty("currencies");
            return Arrays.asList(currencies.split(","));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
