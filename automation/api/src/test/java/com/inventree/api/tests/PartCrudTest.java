package com.inventree.api.tests;

import com.inventree.api.base.BaseApi;
import com.inventree.api.endpoints.PartEndpoints;
import com.inventree.api.models.ErrorResponse;
import com.inventree.api.models.Part;
import com.inventree.api.utils.JsonSchemaHelper;
import com.inventree.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class for Part CRUD operations.
 * Demonstrates REST Assured usage patterns and test organization.
 */
@Epic("Part Management")
@Feature("Part CRUD Operations")
public class PartCrudTest extends BaseApiTest {

    /**
     * Test case: Create a new part with valid data.
     * Verifies successful part creation via POST endpoint.
     */
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("Create Part")
    @Description("Verify user can successfully create a new part with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePart() {
        logger.info("=== Test: Create Part ===");

        // Prepare test data
        Part newPart = TestDataGenerator.validPart();
        logger.info("Creating part: {}", newPart.getName());

        // Send POST request
        Response response = BaseApi.givenAdmin()
                .body(newPart)
                .post(PartEndpoints.PARTS);

        // Log response for debugging
        logResponse(response);

        // Verify response
        response.then()
                .statusCode(201)
                .body("name", equalTo(newPart.getName()))
                .body("description", equalTo(newPart.getDescription()))
                .body("category", equalTo(newPart.getCategory()))
                .body("active", equalTo(newPart.getActive()))
                .body("pk", notNullValue());

        // Extract and store part ID for cleanup
        int partId = response.jsonPath().getInt("pk");
        createdPartIds.add(partId);

        logger.info("Part created successfully with ID: {}", partId);
    }

    /**
     * Test case: Create a part with all fields populated.
     * Verifies all optional fields are correctly saved.
     */
    @Test(groups = {"regression"}, priority = 2)
    @Story("Create Part")
    @Description("Verify part creation with all fields populated")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePartWithAllFields() {
        logger.info("=== Test: Create Part with All Fields ===");

        Part newPart = TestDataGenerator.validPartWithAllFields();

        Response response = BaseApi.givenAdmin()
                .body(newPart)
                .post(PartEndpoints.PARTS);

        response.then()
                .statusCode(201)
                .body("name", equalTo(newPart.getName()))
                .body("description", equalTo(newPart.getDescription()))
                .body("IPN", equalTo(newPart.getIpn()))
                .body("revision", equalTo(newPart.getRevision()))
                .body("keywords", equalTo(newPart.getKeywords()))
                .body("units", equalTo(newPart.getUnits()))
                .body("component", equalTo(newPart.getComponent()))
                .body("purchaseable", equalTo(newPart.getPurchaseable()));

        int partId = response.jsonPath().getInt("pk");
        createdPartIds.add(partId);

        logger.info("Part with all fields created successfully: {}", partId);
    }

    /**
     * Test case: Get part details by ID.
     * Verifies GET endpoint returns correct part data.
     */
    @Test(groups = {"smoke", "regression"}, priority = 3)
    @Story("Get Part")
    @Description("Verify user can retrieve part details by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPartById() {
        logger.info("=== Test: Get Part by ID ===");

        // Create a part first
        Part newPart = TestDataGenerator.validPart();
        int partId = createTestPart(newPart);

        // Get the part
        Response response = BaseApi.givenAdmin()
                .pathParam("id", partId)
                .get(PartEndpoints.PART_BY_ID);

        logResponse(response);

        // Verify response
        response.then()
                .statusCode(200)
                .body("pk", equalTo(partId))
                .body("name", equalTo(newPart.getName()))
                .body("description", equalTo(newPart.getDescription()))
                .body("category", equalTo(newPart.getCategory()));

        // Deserialize to Part object
        Part retrievedPart = response.as(Part.class);
        assertThat(retrievedPart.getPk()).isEqualTo(partId);
        assertThat(retrievedPart.getName()).isEqualTo(newPart.getName());

        logger.info("Part retrieved successfully: {}", partId);
    }

    /**
     * Test case: Update part via PATCH.
     * Verifies partial update of part fields.
     */
    @Test(groups = {"regression"}, priority = 4)
    @Story("Update Part")
    @Description("Verify user can update part fields via PATCH")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdatePart() {
        logger.info("=== Test: Update Part ===");

        // Create a part
        Part originalPart = TestDataGenerator.validPart();
        int partId = createTestPart(originalPart);

        // Prepare update payload
        Part updateData = Part.builder()
                .description("Updated description via API test")
                .keywords("updated, keywords")
                .build();

        // Update the part
        Response response = BaseApi.givenAdmin()
                .pathParam("id", partId)
                .body(updateData)
                .patch(PartEndpoints.PART_BY_ID);

        logResponse(response);

        // Verify response
        response.then()
                .statusCode(200)
                .body("pk", equalTo(partId))
                .body("name", equalTo(originalPart.getName())) // Name unchanged
                .body("description", equalTo(updateData.getDescription())) // Description updated
                .body("keywords", equalTo(updateData.getKeywords())); // Keywords updated

        logger.info("Part updated successfully: {}", partId);
    }

    /**
     * Test case: Delete part.
     * Verifies DELETE endpoint removes the part.
     */
    @Test(groups = {"regression"}, priority = 5)
    @Story("Delete Part")
    @Description("Verify user can delete a part")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePart() {
        logger.info("=== Test: Delete Part ===");

        // Create a part
        Part newPart = TestDataGenerator.validPart();
        int partId = createTestPart(newPart);

        // Delete the part
        Response deleteResponse = BaseApi.givenAdmin()
                .pathParam("id", partId)
                .delete(PartEndpoints.PART_BY_ID);

        deleteResponse.then()
                .statusCode(204); // No content

        // Verify part is deleted (GET should return 404)
        Response getResponse = BaseApi.givenAdmin()
                .pathParam("id", partId)
                .get(PartEndpoints.PART_BY_ID);

        getResponse.then()
                .statusCode(404);

        // Remove from cleanup list as it's already deleted
        createdPartIds.remove(Integer.valueOf(partId));

        logger.info("Part deleted successfully: {}", partId);
    }

    /**
     * Test case: Create part with missing required field.
     * Verifies validation error is returned.
     */
    @Test(groups = {"negative", "regression"}, priority = 6)
    @Story("Create Part - Validation")
    @Description("Verify error is returned when creating part without required field")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePartMissingName() {
        logger.info("=== Test: Create Part Missing Name ===");

        Part invalidPart = TestDataGenerator.invalidPartMissingName();

        Response response = BaseApi.givenAdmin()
                .body(invalidPart)
                .post(PartEndpoints.PARTS);

        logResponse(response);

        // Verify error response
        response.then()
                .statusCode(400)
                .body("name", notNullValue()); // Error should mention 'name' field

        logger.info("Validation error returned as expected");
    }

    /**
     * Test case: Create part with missing category.
     * Verifies validation error for missing category.
     */
    @Test(groups = {"negative", "regression"}, priority = 7)
    @Story("Create Part - Validation")
    @Description("Verify error is returned when creating part without category")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePartMissingCategory() {
        logger.info("=== Test: Create Part Missing Category ===");

        Part invalidPart = TestDataGenerator.invalidPartMissingCategory();

        Response response = BaseApi.givenAdmin()
                .body(invalidPart)
                .post(PartEndpoints.PARTS);

        logResponse(response);

        response.then()
                .statusCode(400);

        // Deserialize to ErrorResponse
        ErrorResponse error = response.as(ErrorResponse.class);
        assertThat(error.hasFieldError("category")).isTrue();

        logger.info("Category validation error returned as expected");
    }

    /**
     * Test case: Get non-existent part.
     * Verifies 404 is returned for invalid ID.
     */
    @Test(groups = {"negative", "regression"}, priority = 8)
    @Story("Get Part - Not Found")
    @Description("Verify 404 is returned when getting non-existent part")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentPart() {
        logger.info("=== Test: Get Non-Existent Part ===");

        int nonExistentId = 999999;

        Response response = BaseApi.givenAdmin()
                .pathParam("id", nonExistentId)
                .get(PartEndpoints.PART_BY_ID);

        response.then()
                .statusCode(404);

        logger.info("404 returned as expected for non-existent part");
    }

    /**
     * Test case: List all parts with JSON schema validation.
     * Verifies GET list endpoint returns correct structure.
     */
    @Test(groups = {"regression"}, priority = 9)
    @Story("List Parts")
    @Description("Verify parts list endpoint returns correct structure")
    @Severity(SeverityLevel.NORMAL)
    public void testListParts() {
        logger.info("=== Test: List Parts ===");

        Response response = BaseApi.givenAdmin()
                .get(PartEndpoints.PARTS);

        logResponse(response);

        // Verify response structure
        response.then()
                .statusCode(200)
                .body("count", greaterThanOrEqualTo(0))
                .body("results", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/part-list-schema.json"));

        // Verify using AssertJ
        int count = response.jsonPath().getInt("count");
        assertThat(count).isGreaterThanOrEqualTo(0);

        logger.info("Parts list retrieved successfully. Count: {}", count);
    }

    /**
     * Test case: Unauthorized access without authentication.
     * Verifies 401 is returned when auth is missing.
     */
    @Test(groups = {"negative", "regression"}, priority = 10)
    @Story("Authentication")
    @Description("Verify 401 is returned for unauthenticated requests")
    @Severity(SeverityLevel.CRITICAL)
    public void testUnauthorizedAccess() {
        logger.info("=== Test: Unauthorized Access ===");

        Response response = BaseApi.givenNoAuth()
                .get(PartEndpoints.PARTS);

        response.then()
                .statusCode(401);

        logger.info("401 returned as expected for unauthenticated request");
    }

    /**
     * DataProvider for parameterized test.
     * Provides different part types to test.
     */
    @DataProvider(name = "partTypes")
    public Object[][] partTypesProvider() {
        return new Object[][]{
                {TestDataGenerator.componentPart(), "Component Part"},
                {TestDataGenerator.assemblyPart(), "Assembly Part"},
                {TestDataGenerator.templatePart(), "Template Part"}
        };
    }

    /**
     * Test case: Create different types of parts (parameterized).
     * Demonstrates DataProvider usage.
     */
    @Test(groups = {"regression"}, dataProvider = "partTypes", priority = 11)
    @Story("Create Part - Types")
    @Description("Verify creation of different part types")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateDifferentPartTypes(Part part, String partType) {
        logger.info("=== Test: Create {} ===", partType);

        Response response = BaseApi.givenAdmin()
                .body(part)
                .post(PartEndpoints.PARTS);

        response.then()
                .statusCode(201)
                .body("name", equalTo(part.getName()))
                .body("pk", notNullValue());

        int partId = response.jsonPath().getInt("pk");
        createdPartIds.add(partId);

        logger.info("{} created successfully: {}", partType, partId);
    }
}
