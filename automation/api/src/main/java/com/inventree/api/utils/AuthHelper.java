package com.inventree.api.utils;

import com.inventree.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for authentication.
 * Supports both Basic auth and Token-based auth.
 */
public class AuthHelper {

    private static final Logger logger = LoggerFactory.getLogger(AuthHelper.class);
    private static final Map<String, String> tokenCache = new HashMap<>();

    /**
     * Returns RequestSpecification with Basic Authentication.
     *
     * @param username Username
     * @param password Password
     * @return RequestSpecification with Basic auth
     */
    public static RequestSpecification getBasicAuthSpec(String username, String password) {
        logger.debug("Creating Basic auth spec for user: {}", username);
        return RestAssured.given()
                .auth()
                .preemptive()
                .basic(username, password);
    }

    /**
     * Returns RequestSpecification with Basic Authentication for a specific role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return RequestSpecification with Basic auth
     */
    public static RequestSpecification getBasicAuthSpec(String role) {
        String username = ApiConfig.getUsername(role);
        String password = ApiConfig.getPassword(role);
        return getBasicAuthSpec(username, password);
    }

    /**
     * Gets authentication token for a user.
     * Caches token to avoid repeated API calls.
     *
     * @param username Username
     * @param password Password
     * @return Authentication token
     */
    public static String getToken(String username, String password) {
        // Check cache first
        String cacheKey = username + ":" + password;
        if (tokenCache.containsKey(cacheKey)) {
            logger.debug("Returning cached token for user: {}", username);
            return tokenCache.get(cacheKey);
        }

        logger.info("Fetching new token for user: {}", username);

        // Call token endpoint
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        Response response = RestAssured.given()
                .baseUri(ApiConfig.getBaseUri())
                .contentType("application/json")
                .body(credentials)
                .post("/user/token/");

        if (response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("token");
            tokenCache.put(cacheKey, token);
            logger.debug("Token retrieved successfully for user: {}", username);
            return token;
        } else {
            logger.error("Failed to get token for user: {}. Status code: {}", username, response.getStatusCode());
            throw new RuntimeException("Failed to get authentication token: " + response.getBody().asString());
        }
    }

    /**
     * Gets authentication token for a specific role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return Authentication token
     */
    public static String getToken(String role) {
        String username = ApiConfig.getUsername(role);
        String password = ApiConfig.getPassword(role);
        return getToken(username, password);
    }

    /**
     * Returns RequestSpecification with Token Authentication.
     *
     * @param username Username
     * @param password Password
     * @return RequestSpecification with Token auth
     */
    public static RequestSpecification getTokenAuthSpec(String username, String password) {
        String token = getToken(username, password);
        logger.debug("Creating Token auth spec for user: {}", username);
        return RestAssured.given()
                .header("Authorization", "Token " + token);
    }

    /**
     * Returns RequestSpecification with Token Authentication for a specific role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return RequestSpecification with Token auth
     */
    public static RequestSpecification getTokenAuthSpec(String role) {
        String username = ApiConfig.getUsername(role);
        String password = ApiConfig.getPassword(role);
        return getTokenAuthSpec(username, password);
    }

    /**
     * Clears the token cache.
     * Useful for testing token expiration scenarios.
     */
    public static void clearTokenCache() {
        logger.info("Clearing token cache");
        tokenCache.clear();
    }
}
