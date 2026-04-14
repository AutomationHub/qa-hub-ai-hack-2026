package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Login Page.
 */
public class LoginPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By USERNAME_FIELD = By.id("username");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".alert-danger");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enters username.
     *
     * @param username Username to enter
     * @return LoginPage for method chaining
     */
    public LoginPage enterUsername(String username) {
        type(USERNAME_FIELD, username);
        return this;
    }

    /**
     * Enters password.
     *
     * @param password Password to enter
     * @return LoginPage for method chaining
     */
    public LoginPage enterPassword(String password) {
        type(PASSWORD_FIELD, password);
        return this;
    }

    /**
     * Clicks login button.
     *
     * @return DashboardPage after successful login
     */
    public DashboardPage clickLogin() {
        click(LOGIN_BUTTON);
        return new DashboardPage(driver);
    }

    /**
     * Performs complete login action.
     *
     * @param username Username
     * @param password Password
     * @return DashboardPage after successful login
     */
    public DashboardPage login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    /**
     * Gets error message text.
     *
     * @return Error message text
     */
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    /**
     * Checks if error message is displayed.
     *
     * @return True if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }
}
