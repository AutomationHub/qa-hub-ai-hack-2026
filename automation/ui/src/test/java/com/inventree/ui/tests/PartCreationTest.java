package com.inventree.ui.tests;

import com.inventree.ui.pages.CreatePartPage;
import com.inventree.ui.pages.DashboardPage;
import com.inventree.ui.pages.PartDetailPage;
import com.inventree.ui.pages.PartsListPage;
import com.inventree.ui.utils.TestDataReader;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Part Creation functionality.
 * Demonstrates Page Object Model usage pattern.
 */
@Epic("Part Management")
@Feature("Part Creation")
public class PartCreationTest extends BaseTest {

    private DashboardPage dashboardPage;
    private PartsListPage partsListPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        // Login as admin user before each test
        loginAsAdmin();
        dashboardPage = new DashboardPage(driver);
    }

    /**
     * Test case: Create a new part with all required fields.
     * This is a happy path scenario demonstrating successful part creation.
     *
     * Test Steps:
     * 1. Navigate to Parts page
     * 2. Click Add Part button
     * 3. Fill in all required fields
     * 4. Save the part
     * 5. Verify part is created successfully
     */
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("Create Part - Happy Path")
    @Description("Verify user can successfully create a new part with all required fields")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePartWithAllFields() {
        logger.info("=== Starting Test: Create Part with All Fields ===");

        // Load test data from JSON file
        List<Map<String, Object>> partData = TestDataReader.readJsonAsList("parts.json");
        Map<String, Object> testPart = partData.get(0); // First part: Resistor 10K Ohm

        // Navigate to Parts page
        partsListPage = dashboardPage.navigateToParts();
        assertThat(partsListPage.isPartsTableDisplayed())
                .as("Parts list page should be displayed")
                .isTrue();

        // Click Add Part button
        CreatePartPage createPartPage = partsListPage.clickAddPart();

        // Fill in part details using fluent API
        PartDetailPage partDetailPage = createPartPage
                .enterName((String) testPart.get("name"))
                .enterDescription((String) testPart.get("description"))
                .enterIPN((String) testPart.get("ipn"))
                .enterRevision((String) testPart.get("revision"))
                .enterKeywords((String) testPart.get("keywords"))
                .enterUnits((String) testPart.get("units"))
                .enterLink((String) testPart.get("link"))
                .setActive((Boolean) testPart.get("active"))
                .setComponent((Boolean) testPart.get("component"))
                .setPurchaseable((Boolean) testPart.get("purchaseable"))
                .clickSave();

        // Verify part was created successfully
        assertThat(partDetailPage.getPartName())
                .as("Created part name should match input")
                .isEqualTo(testPart.get("name"));

        assertThat(partDetailPage.getPartDescription())
                .as("Created part description should match input")
                .contains((String) testPart.get("description"));

        logger.info("=== Test Passed: Part created successfully ===");
    }

    /**
     * Test case: Attempt to create a part without required fields.
     * This tests validation error handling.
     *
     * Test Steps:
     * 1. Navigate to Parts page
     * 2. Click Add Part button
     * 3. Leave required fields empty
     * 4. Attempt to save
     * 5. Verify validation error is displayed
     */
    @Test(groups = {"regression"}, priority = 2)
    @Story("Create Part - Validation")
    @Description("Verify validation error is shown when required fields are missing")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePartWithMissingRequiredFields() {
        logger.info("=== Starting Test: Create Part with Missing Required Fields ===");

        // Navigate to Parts page
        partsListPage = dashboardPage.navigateToParts();

        // Click Add Part button
        CreatePartPage createPartPage = partsListPage.clickAddPart();

        // Try to save without filling required fields
        createPartPage
                .enterDescription("Test description without name")
                .clickSaveExpectingError();

        // Verify validation error is displayed
        assertThat(createPartPage.isValidationErrorDisplayed())
                .as("Validation error should be displayed when required fields are missing")
                .isTrue();

        logger.info("=== Test Passed: Validation error displayed correctly ===");
    }

    /**
     * Test case: Attempt to create a part with duplicate name.
     * This tests duplicate detection functionality.
     *
     * Test Steps:
     * 1. Create a part with a unique name
     * 2. Attempt to create another part with the same name
     * 3. Verify error message is displayed
     *
     * Note: This test assumes duplicate names are not allowed.
     * Adjust based on actual application behavior.
     */
    @Test(groups = {"regression"}, priority = 3, enabled = false)
    @Story("Create Part - Duplicate Detection")
    @Description("Verify error is shown when attempting to create a part with duplicate name")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePartWithDuplicateName() {
        logger.info("=== Starting Test: Create Part with Duplicate Name ===");

        // Load test data
        List<Map<String, Object>> partData = TestDataReader.readJsonAsList("parts.json");
        Map<String, Object> testPart = partData.get(1); // Second part: Arduino Uno

        // Navigate to Parts page and create first part
        partsListPage = dashboardPage.navigateToParts();
        CreatePartPage createPartPage = partsListPage.clickAddPart();

        String partName = testPart.get("name") + " - Test " + System.currentTimeMillis();

        // Create first part
        createPartPage
                .enterName(partName)
                .enterDescription((String) testPart.get("description"))
                .clickSave();

        // Navigate back to parts list
        partsListPage = dashboardPage.navigateToParts();

        // Attempt to create duplicate part
        createPartPage = partsListPage.clickAddPart();
        createPartPage
                .enterName(partName) // Same name as first part
                .enterDescription("Duplicate part")
                .clickSaveExpectingError();

        // Verify error message is displayed
        assertThat(createPartPage.isErrorMessageDisplayed())
                .as("Error message should be displayed for duplicate part name")
                .isTrue();

        logger.info("=== Test Passed: Duplicate name error displayed correctly ===");
    }

    /**
     * Example test using template part from test data.
     * This demonstrates creating a template part (used as blueprint for other parts).
     *
     * Note: Disabled by default - enable based on actual test requirements.
     */
    @Test(groups = {"regression"}, enabled = false)
    @Story("Create Part - Template")
    @Description("Verify user can create a template part")
    @Severity(SeverityLevel.MINOR)
    public void testCreateTemplatePart() {
        logger.info("=== Starting Test: Create Template Part ===");

        // Load template part from test data
        List<Map<String, Object>> partData = TestDataReader.readJsonAsList("parts.json");
        Map<String, Object> templatePart = partData.get(2); // Third part: Generic PCB Template

        partsListPage = dashboardPage.navigateToParts();
        CreatePartPage createPartPage = partsListPage.clickAddPart();

        // Create template part
        PartDetailPage partDetailPage = createPartPage
                .enterName((String) templatePart.get("name"))
                .enterDescription((String) templatePart.get("description"))
                .enterIPN((String) templatePart.get("ipn"))
                .setActive((Boolean) templatePart.get("active"))
                .setTemplate((Boolean) templatePart.get("template"))
                .setAssembly((Boolean) templatePart.get("assembly"))
                .clickSave();

        // Verify template part was created
        assertThat(partDetailPage.getPartName())
                .as("Template part name should match input")
                .isEqualTo(templatePart.get("name"));

        logger.info("=== Test Passed: Template part created successfully ===");
    }
}
