package com.inventree.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for loading JSON schema files.
 * Used for response validation in tests.
 */
public class JsonSchemaHelper {

    private static final Logger logger = LoggerFactory.getLogger(JsonSchemaHelper.class);
    private static final String SCHEMA_PATH = "schemas/";

    /**
     * Loads a JSON schema file from resources/schemas/ directory.
     *
     * @param schemaFileName Schema file name (e.g., "part-schema.json")
     * @return Schema content as String
     */
    public static String getSchema(String schemaFileName) {
        String schemaPath = SCHEMA_PATH + schemaFileName;
        logger.debug("Loading schema: {}", schemaPath);

        try (InputStream input = JsonSchemaHelper.class.getClassLoader()
                .getResourceAsStream(schemaPath)) {
            if (input == null) {
                logger.error("Schema file not found: {}", schemaPath);
                throw new RuntimeException("Schema file not found: " + schemaPath);
            }
            String schema = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            logger.debug("Schema loaded successfully: {}", schemaFileName);
            return schema;
        } catch (Exception e) {
            logger.error("Error loading schema {}: {}", schemaPath, e.getMessage());
            throw new RuntimeException("Failed to load schema: " + schemaPath, e);
        }
    }

    /**
     * Loads the part-schema.json file.
     *
     * @return Part schema as String
     */
    public static String getPartSchema() {
        return getSchema("part-schema.json");
    }

    /**
     * Loads the part-list-schema.json file.
     *
     * @return Part list schema as String
     */
    public static String getPartListSchema() {
        return getSchema("part-list-schema.json");
    }
}
