package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Dashboard Page.
 */
public class DashboardPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By PARTS_MENU = By.linkText("Parts");
    private static final By STOCK_MENU = By.linkText("Stock");
    private static final By BOM_MENU = By.linkText("BOM");
    private static final By DASHBOARD_HEADER = By.cssSelector("h1.dashboard-title");
    private static final By USER_PROFILE = By.cssSelector(".user-profile");
    private static final By SIDEBAR_NAV = By.cssSelector(".sidebar-nav");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to Parts page.
     *
     * @return PartsListPage
     */
    public PartsListPage navigateToParts() {
        logger.info("Navigating to Parts page");
        click(PARTS_MENU);
        return new PartsListPage(driver);
    }

    /**
     * Checks if dashboard is displayed.
     *
     * @return True if dashboard header is displayed
     */
    public boolean isDashboardDisplayed() {
        return isDisplayed(DASHBOARD_HEADER);
    }

    /**
     * Checks if user is logged in.
     *
     * @return True if user profile is displayed
     */
    public boolean isUserLoggedIn() {
        return isDisplayed(USER_PROFILE);
    }

    /**
     * Gets dashboard header text.
     *
     * @return Dashboard header text
     */
    public String getDashboardHeader() {
        return getText(DASHBOARD_HEADER);
    }

    /**
     * Checks if sidebar navigation is displayed.
     *
     * @return True if sidebar is displayed
     */
    public boolean isSidebarDisplayed() {
        return isDisplayed(SIDEBAR_NAV);
    }
}
