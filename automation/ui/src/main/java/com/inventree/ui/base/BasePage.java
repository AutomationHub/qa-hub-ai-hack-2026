package com.inventree.ui.base;

import com.inventree.ui.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Page Objects.
 * Provides common reusable methods for interacting with web elements.
 */
public abstract class BasePage {

    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected static final int DEFAULT_TIMEOUT = 15;

    /**
     * Constructor to initialize the WebDriver.
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     *
     * @param locator Element locator
     */
    protected void click(By locator) {
        logger.info("Clicking element: {}", locator);
        WebElement element = WaitUtils.waitForClickable(driver, locator, DEFAULT_TIMEOUT);
        element.click();
    }

    /**
     * Types text into an element after clearing it.
     *
     * @param locator Element locator
     * @param text Text to type
     */
    protected void type(By locator, String text) {
        logger.info("Typing '{}' into element: {}", text, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets text from an element.
     *
     * @param locator Element locator
     * @return Element text
     */
    protected String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
        return element.getText();
    }

    /**
     * Checks if an element is displayed.
     *
     * @param locator Element locator
     * @return True if element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
            boolean displayed = element.isDisplayed();
            logger.debug("Element {} displayed: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element {} not displayed", locator);
            return false;
        }
    }

    /**
     * Waits for an element to be visible.
     *
     * @param locator Element locator
     * @return WebElement when visible
     */
    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param locator Element locator
     * @return WebElement when clickable
     */
    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Selects a dropdown option by visible text.
     *
     * @param locator Dropdown locator
     * @param optionText Option text to select
     */
    protected void selectDropdown(By locator, String optionText) {
        logger.info("Selecting '{}' from dropdown: {}", optionText, locator);
        WebElement element = WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
        Select select = new Select(element);
        select.selectByVisibleText(optionText);
    }

    /**
     * Checks if an element is enabled.
     *
     * @param locator Element locator
     * @return True if element is enabled
     */
    protected boolean isEnabled(By locator) {
        WebElement element = WaitUtils.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
        boolean enabled = element.isEnabled();
        logger.debug("Element {} enabled: {}", locator, enabled);
        return enabled;
    }

    /**
     * Gets the current page title.
     *
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current page URL.
     *
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
