package com.inventree.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads API configuration from config.properties file.
 * Supports system property overrides for CI/CD environments.
 */
public class ApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    /**
     * Loads properties from config.properties file.
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ApiConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Unable to find {}", CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
            logger.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Error loading configuration: {}", e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Gets a string property value.
     * Checks system properties first for CI overrides.
     *
     * @param key Property key
     * @return Property value
     */
    public static String getString(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        if (value == null) {
            logger.warn("Property '{}' not found", key);
        }
        return value;
    }

    /**
     * Gets an integer property value.
     *
     * @param key Property key
     * @return Property value as integer
     */
    public static int getInt(String key) {
        String value = getString(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for '{}': {}", key, value);
            throw new IllegalArgumentException("Invalid integer property: " + key, e);
        }
    }

    /**
     * Gets a boolean property value.
     *
     * @param key Property key
     * @return Property value as boolean
     */
    public static boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found");
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Gets a string property value with a default.
     *
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default
     */
    public static String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Builds the full base URI from base URL and API base path.
     *
     * @return Full base URI (e.g., https://demo.inventree.org/api)
     */
    public static String getBaseUri() {
        String baseUrl = getString("base.url");
        String apiBasePath = getString("api.base.path");
        String fullUri = baseUrl + apiBasePath;
        logger.debug("Base URI: {}", fullUri);
        return fullUri;
    }

    /**
     * Gets base URL.
     *
     * @return Base URL
     */
    public static String getBaseUrl() {
        return getString("base.url");
    }

    /**
     * Gets API base path.
     *
     * @return API base path
     */
    public static String getApiBasePath() {
        return getString("api.base.path");
    }

    /**
     * Gets username for a specific user role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return Username
     */
    public static String getUsername(String role) {
        return getString(role + ".username");
    }

    /**
     * Gets password for a specific user role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return Password
     */
    public static String getPassword(String role) {
        return getString(role + ".password");
    }

    /**
     * Checks if request logging is enabled.
     *
     * @return True if request logging is enabled
     */
    public static boolean isRequestLoggingEnabled() {
        return getBoolean("log.request");
    }

    /**
     * Checks if response logging is enabled.
     *
     * @return True if response logging is enabled
     */
    public static boolean isResponseLoggingEnabled() {
        return getBoolean("log.response");
    }
}
