package com.inventree.api.base;

import com.inventree.api.config.ApiConfig;
import com.inventree.api.utils.AuthHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for building REST Assured RequestSpecifications.
 * Provides shared configuration and authentication helpers.
 */
public class BaseApi {

    protected static final Logger logger = LoggerFactory.getLogger(BaseApi.class);

    /**
     * Returns a base RequestSpecification with common settings.
     * Includes: base URI, content type, Allure filter, logging.
     *
     * @return RequestSpecification
     */
    public static RequestSpecification getBaseSpec() {
        RequestSpecification spec = RestAssured.given()
                .baseUri(ApiConfig.getBaseUri())
                .contentType("application/json")
                .filter(new AllureRestAssured());

        // Add logging based on configuration
        if (ApiConfig.isRequestLoggingEnabled()) {
            spec = spec.log().ifValidationFails(LogDetail.ALL);
        }

        return spec;
    }

    /**
     * Returns a RequestSpecification with authentication for a specific user role.
     * Uses Token authentication by default.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return RequestSpecification with authentication
     */
    public static RequestSpecification givenAuth(String role) {
        logger.debug("Creating authenticated request spec for role: {}", role);
        return getBaseSpec()
                .spec(AuthHelper.getTokenAuthSpec(role));
    }

    /**
     * Returns a RequestSpecification with Basic Authentication for a specific user role.
     *
     * @param role User role (admin, allaccess, engineer, reader)
     * @return RequestSpecification with Basic auth
     */
    public static RequestSpecification givenBasicAuth(String role) {
        logger.debug("Creating Basic auth request spec for role: {}", role);
        return getBaseSpec()
                .spec(AuthHelper.getBasicAuthSpec(role));
    }

    /**
     * Returns a RequestSpecification without authentication.
     * Useful for testing 401 unauthorized scenarios.
     *
     * @return RequestSpecification without auth
     */
    public static RequestSpecification givenNoAuth() {
        logger.debug("Creating unauthenticated request spec");
        return getBaseSpec();
    }

    /**
     * Returns a RequestSpecification with admin authentication.
     * Convenience method for admin user.
     *
     * @return RequestSpecification with admin auth
     */
    public static RequestSpecification givenAdmin() {
        return givenAuth("admin");
    }

    /**
     * Returns a RequestSpecification with allaccess authentication.
     * Convenience method for allaccess user.
     *
     * @return RequestSpecification with allaccess auth
     */
    public static RequestSpecification givenAllAccess() {
        return givenAuth("allaccess");
    }

    /**
     * Returns a RequestSpecification with engineer authentication.
     * Convenience method for engineer user.
     *
     * @return RequestSpecification with engineer auth
     */
    public static RequestSpecification givenEngineer() {
        return givenAuth("engineer");
    }

    /**
     * Returns a RequestSpecification with reader authentication.
     * Convenience method for reader user.
     *
     * @return RequestSpecification with reader auth
     */
    public static RequestSpecification givenReader() {
        return givenAuth("reader");
    }
}
