package com.inventree.api.tests;

import com.inventree.api.base.BaseApi;
import com.inventree.api.config.ApiConfig;
import com.inventree.api.endpoints.PartEndpoints;
import com.inventree.api.models.Part;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;

/**
 * Base test class for API tests.
 * Provides common setup, teardown, and helper methods.
 */
public class BaseApiTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
    protected List<Integer> createdPartIds = new ArrayList<>();

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        logger.info("=== API Test Suite Setup ===");

        // Set base URI
        RestAssured.baseURI = ApiConfig.getBaseUri();
        logger.info("Base URI: {}", RestAssured.baseURI);

        // Configure REST Assured defaults
        RestAssured.config = RestAssured.config()
                .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation())
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 10000));

        // Enable logging if configured
        if (ApiConfig.isRequestLoggingEnabled()) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        }

        logger.info("=== API Test Suite Setup Complete ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest(ITestResult result) {
        logger.info("=== Starting Test: {} ===", result.getMethod().getMethodName());
        createdPartIds.clear();
    }

    @AfterMethod(alwaysRun = true)
    public void teardownTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test FAILED: {}", result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: {}", result.getName());
        }

        // Cleanup created parts
        cleanupCreatedParts();

        logger.info("=== Test Complete: {} ===", result.getMethod().getMethodName());
    }

    /**
     * Creates a test part via API and returns its ID.
     * Useful for tests that need existing data.
     *
     * @param part Part object to create
     * @return Created part ID
     */
    protected int createTestPart(Part part) {
        logger.info("Creating test part: {}", part.getName());

        Response response = BaseApi.givenAdmin()
                .body(part)
                .post(PartEndpoints.PARTS);

        if (response.getStatusCode() == 201) {
            int partId = response.jsonPath().getInt("pk");
            createdPartIds.add(partId);
            logger.info("Test part created with ID: {}", partId);
            return partId;
        } else {
            logger.error("Failed to create test part. Status: {}, Body: {}",
                    response.getStatusCode(), response.getBody().asString());
            throw new RuntimeException("Failed to create test part: " + response.getBody().asString());
        }
    }

    /**
     * Deletes a test part by ID.
     *
     * @param partId Part ID to delete
     */
    protected void deleteTestPart(int partId) {
        logger.info("Deleting test part with ID: {}", partId);

        Response response = BaseApi.givenAdmin()
                .pathParam("id", partId)
                .delete(PartEndpoints.PART_BY_ID);

        if (response.getStatusCode() == 204) {
            logger.info("Test part deleted successfully: {}", partId);
        } else {
            logger.warn("Failed to delete test part {}. Status: {}", partId, response.getStatusCode());
        }
    }

    /**
     * Cleans up all parts created during the test.
     */
    private void cleanupCreatedParts() {
        if (!createdPartIds.isEmpty()) {
            logger.info("Cleaning up {} created part(s)", createdPartIds.size());
            for (int partId : createdPartIds) {
                try {
                    deleteTestPart(partId);
                } catch (Exception e) {
                    logger.warn("Error cleaning up part {}: {}", partId, e.getMessage());
                }
            }
            createdPartIds.clear();
        }
    }

    /**
     * Logs the full request and response for debugging.
     *
     * @param response Response to log
     */
    protected void logResponse(Response response) {
        logger.debug("Response Status: {}", response.getStatusCode());
        logger.debug("Response Body: {}", response.getBody().asString());
    }
}
