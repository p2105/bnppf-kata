package Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig 
{
    // execution starts from here when the class is first referenced, to load config values from the properties file
    private static final String CONFIG_FILE = "test-config.properties";
    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static String getBaseUri() {
        return require("api.baseUri");
    }

    public static String getAdminUsername() {
        return require("admin.username");
    }

    public static String getAdminPassword() {
        return require("admin.password");
    }

    public static String getAdminLoginPath() {
        return require("admin.loginPath");
    }

    public static String getBookingCreatePath() {
        return require("booking.createPath");
    }

    public static String getBookingDeletePathTemplate() {
        return require("booking.deletePathTemplate");
    }

    public static String getRoomDetailsPathTemplate() {
        return require("roomDetailsPathTemplate");
    }

    public static String getRetrieveBookingDetailsPath() {
        return require("retrieveBookingDetailsPath");
    }

    public static String getDeleteBookingDetailsPath() {
        return require("deleteBookingDetailsPath");
    }

    public static int getBookingCleanupMaxId() {
        String value = require("booking.cleanup.maxId");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Invalid integer for booking.cleanup.maxId: " + value, ex);
        }
    }

    // executed once when the class is loaded, to read config values from the properties file
    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Missing config file on test classpath: " + CONFIG_FILE);
            }
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load test config: " + CONFIG_FILE, ex);
        }
    }

    private static String require(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required test config key: " + key);
        }
        return value;
    }
}
