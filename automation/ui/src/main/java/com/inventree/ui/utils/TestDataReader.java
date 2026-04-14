package com.inventree.ui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading JSON test data files.
 */
public class TestDataReader {

    private static final Logger logger = LoggerFactory.getLogger(TestDataReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads JSON file and returns as Map.
     *
     * @param fileName JSON file name (relative to testdata/ directory)
     * @return Map representation of JSON
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readJsonAsMap(String fileName) {
        String filePath = "testdata/" + fileName;
        logger.info("Reading test data from: {}", filePath);

        try (InputStream input = TestDataReader.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (input == null) {
                logger.error("Test data file not found: {}", filePath);
                throw new RuntimeException("Test data file not found: " + filePath);
            }
            Map<String, Object> data = objectMapper.readValue(input, Map.class);
            logger.debug("Test data loaded successfully from {}", filePath);
            return data;
        } catch (IOException e) {
            logger.error("Error reading test data from {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read test data: " + filePath, e);
        }
    }

    /**
     * Reads JSON file and returns as List.
     *
     * @param fileName JSON file name (relative to testdata/ directory)
     * @return List representation of JSON
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> readJsonAsList(String fileName) {
        String filePath = "testdata/" + fileName;
        logger.info("Reading test data from: {}", filePath);

        try (InputStream input = TestDataReader.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (input == null) {
                logger.error("Test data file not found: {}", filePath);
                throw new RuntimeException("Test data file not found: " + filePath);
            }
            List<Map<String, Object>> data = objectMapper.readValue(input, List.class);
            logger.debug("Test data loaded successfully from {}", filePath);
            return data;
        } catch (IOException e) {
            logger.error("Error reading test data from {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read test data: " + filePath, e);
        }
    }

    /**
     * Reads JSON file and deserializes to specified type.
     *
     * @param fileName JSON file name (relative to testdata/ directory)
     * @param valueType Class type to deserialize to
     * @param <T> Type parameter
     * @return Deserialized object
     */
    public static <T> T readJson(String fileName, Class<T> valueType) {
        String filePath = "testdata/" + fileName;
        logger.info("Reading test data from: {}", filePath);

        try (InputStream input = TestDataReader.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (input == null) {
                logger.error("Test data file not found: {}", filePath);
                throw new RuntimeException("Test data file not found: " + filePath);
            }
            T data = objectMapper.readValue(input, valueType);
            logger.debug("Test data loaded successfully from {}", filePath);
            return data;
        } catch (IOException e) {
            logger.error("Error reading test data from {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read test data: " + filePath, e);
        }
    }
}
