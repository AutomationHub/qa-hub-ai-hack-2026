package com.inventree.ui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration from config.properties file.
 * Supports system property overrides for CI/CD environments.
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
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
        try (InputStream input = ConfigReader.class.getClassLoader()
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
}
