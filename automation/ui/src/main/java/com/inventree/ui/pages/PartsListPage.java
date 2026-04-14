package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Parts List Page.
 */
public class PartsListPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By PARTS_TABLE = By.cssSelector("table.parts-table");
    private static final By SEARCH_BOX = By.cssSelector("input[type='search']");
    private static final By ADD_PART_BUTTON = By.cssSelector("button.add-part");
    private static final By CATEGORY_SIDEBAR = By.cssSelector(".category-sidebar");
    private static final By FILTER_ACTIVE = By.cssSelector("input[name='active']");
    private static final By FILTER_TEMPLATE = By.cssSelector("input[name='template']");
    private static final By FILTER_ASSEMBLY = By.cssSelector("input[name='assembly']");
    private static final By FIRST_PART_ROW = By.cssSelector("table.parts-table tbody tr:first-child");
    private static final By PARTS_COUNT = By.cssSelector(".parts-count");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public PartsListPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Searches for a part.
     *
     * @param searchTerm Search term
     * @return PartsListPage for method chaining
     */
    public PartsListPage searchPart(String searchTerm) {
        logger.info("Searching for part: {}", searchTerm);
        type(SEARCH_BOX, searchTerm);
        return this;
    }

    /**
     * Clicks add part button.
     *
     * @return CreatePartPage
     */
    public CreatePartPage clickAddPart() {
        logger.info("Clicking add part button");
        click(ADD_PART_BUTTON);
        return new CreatePartPage(driver);
    }

    /**
     * Clicks on the first part in the list.
     *
     * @return PartDetailPage
     */
    public PartDetailPage clickFirstPart() {
        logger.info("Clicking first part in list");
        click(FIRST_PART_ROW);
        return new PartDetailPage(driver);
    }

    /**
     * Checks if parts table is displayed.
     *
     * @return True if parts table is displayed
     */
    public boolean isPartsTableDisplayed() {
        return isDisplayed(PARTS_TABLE);
    }

    /**
     * Checks if category sidebar is displayed.
     *
     * @return True if category sidebar is displayed
     */
    public boolean isCategorySidebarDisplayed() {
        return isDisplayed(CATEGORY_SIDEBAR);
    }

    /**
     * Filters by active parts.
     *
     * @return PartsListPage for method chaining
     */
    public PartsListPage filterByActive() {
        logger.info("Filtering by active parts");
        click(FILTER_ACTIVE);
        return this;
    }

    /**
     * Filters by template parts.
     *
     * @return PartsListPage for method chaining
     */
    public PartsListPage filterByTemplate() {
        logger.info("Filtering by template parts");
        click(FILTER_TEMPLATE);
        return this;
    }

    /**
     * Filters by assembly parts.
     *
     * @return PartsListPage for method chaining
     */
    public PartsListPage filterByAssembly() {
        logger.info("Filtering by assembly parts");
        click(FILTER_ASSEMBLY);
        return this;
    }

    /**
     * Gets the total count of parts.
     *
     * @return Parts count text
     */
    public String getPartsCount() {
        return getText(PARTS_COUNT);
    }
}
