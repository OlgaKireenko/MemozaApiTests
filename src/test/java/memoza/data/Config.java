package memoza.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "demo"); // "demo"по умолчанию test (SCDEV05)
        String fileName = "application-" + env + ".properties";

        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
