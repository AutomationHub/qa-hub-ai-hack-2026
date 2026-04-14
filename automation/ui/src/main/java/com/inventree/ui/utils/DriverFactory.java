package com.inventree.ui.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating WebDriver instances using WebDriverManager.
 * Supports Chrome, Firefox, and Edge browsers with headless mode.
 * Thread-safe implementation for parallel test execution.
 */
public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Creates and returns a WebDriver instance based on the browser type.
     *
     * @param browser Browser type (chrome, firefox, edge)
     * @param headless Run in headless mode if true
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        WebDriver driver;

        logger.info("Creating {} driver (headless: {})", browser, headless);

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
        logger.info("Driver created successfully: {}", driver.getClass().getSimpleName());

        return driver;
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits the driver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting driver: {}", driver.getClass().getSimpleName());
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
