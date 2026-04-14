package com.inventree.ui.tests;

import com.inventree.ui.config.ConfigReader;
import com.inventree.ui.pages.LoginPage;
import com.inventree.ui.utils.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.time.Duration;

/**
 * Base test class providing driver lifecycle and common test setup/teardown.
 * All test classes should extend this class.
 */
public class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("=== Test Setup Starting ===");

        // Load configuration
        String browser = ConfigReader.getString("browser", "chrome");
        boolean headless = ConfigReader.getBoolean("headless");
        baseUrl = ConfigReader.getString("base.url");
        int implicitWait = ConfigReader.getInt("implicit.wait");

        logger.info("Browser: {}, Headless: {}, Base URL: {}", browser, headless, baseUrl);

        // Create driver
        driver = DriverFactory.createDriver(browser, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Navigate to base URL
        driver.get(baseUrl);
        logger.info("Navigated to: {}", baseUrl);

        logger.info("=== Test Setup Complete ===");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logger.info("=== Test Teardown Starting ===");

        // Capture screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test FAILED: {}", result.getName());
            captureScreenshot(result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: {}", result.getName());
        }

        // Quit driver
        DriverFactory.quitDriver();
        logger.info("=== Test Teardown Complete ===");
    }

    /**
     * Captures screenshot and attaches to Allure report.
     *
     * @param screenshotName Name for the screenshot
     */
    protected void captureScreenshot(String screenshotName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(screenshotName, new ByteArrayInputStream(screenshot));
            logger.info("Screenshot captured: {}", screenshotName);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    /**
     * Performs login with admin credentials.
     *
     * @return DashboardPage after successful login
     */
    protected void loginAsAdmin() {
        String username = ConfigReader.getString("admin.username");
        String password = ConfigReader.getString("admin.password");
        logger.info("Logging in as admin user: {}", username);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    /**
     * Performs login with allaccess credentials.
     */
    protected void loginAsAllAccess() {
        String username = ConfigReader.getString("allaccess.username");
        String password = ConfigReader.getString("allaccess.password");
        logger.info("Logging in as allaccess user: {}", username);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    /**
     * Performs login with reader credentials.
     */
    protected void loginAsReader() {
        String username = ConfigReader.getString("reader.username");
        String password = ConfigReader.getString("reader.password");
        logger.info("Logging in as reader user: {}", username);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }
}
