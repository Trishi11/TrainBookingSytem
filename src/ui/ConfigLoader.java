package ui;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("⚠ config.properties not found. Using defaults.");
            try (FileInputStream fis = new FileInputStream("config.sample.properties")) {
                props.load(fis);
            } catch (IOException ex) {
                throw new RuntimeException("No config file found!", ex);
            }
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
