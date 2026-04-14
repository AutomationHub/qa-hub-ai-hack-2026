package com.inventree.ui.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Utility class providing explicit wait helpers for Selenium operations.
 */
public class WaitUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    /**
     * Waits for an element to be visible.
     *
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     * @return WebElement when visible
     */
    public static WebElement waitForVisible(WebDriver driver, By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be visible: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     * @return WebElement when clickable
     */
    public static WebElement waitForClickable(WebDriver driver, By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be invisible.
     *
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     * @return True if element becomes invisible
     */
    public static boolean waitForInvisible(WebDriver driver, By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be invisible: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for text to be present in an element.
     *
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param text Expected text
     * @param timeoutSeconds Maximum wait time in seconds
     * @return True if text is present
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text, int timeoutSeconds) {
        logger.debug("Waiting for text '{}' in element: {}", text, locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits for an element to be present in the DOM.
     *
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     * @return WebElement when present
     */
    public static WebElement waitForPresent(WebDriver driver, By locator, int timeoutSeconds) {
        logger.debug("Waiting for element to be present: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
