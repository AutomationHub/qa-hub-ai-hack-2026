package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Part Detail Page.
 */
public class PartDetailPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By PART_NAME = By.cssSelector("h2.part-name");
    private static final By PART_DESCRIPTION = By.cssSelector(".part-description");
    private static final By EDIT_BUTTON = By.cssSelector("button.edit-part");
    private static final By DELETE_BUTTON = By.cssSelector("button.delete-part");

    // Tab locators
    private static final By STOCK_TAB = By.cssSelector("a[href*='stock']");
    private static final By BOM_TAB = By.cssSelector("a[href*='bom']");
    private static final By PARAMETERS_TAB = By.cssSelector("a[href*='parameters']");
    private static final By VARIANTS_TAB = By.cssSelector("a[href*='variants']");
    private static final By REVISIONS_TAB = By.cssSelector("a[href*='revisions']");
    private static final By NOTES_TAB = By.cssSelector("a[href*='notes']");

    // Part attribute locators
    private static final By PART_IPN = By.cssSelector(".part-ipn");
    private static final By PART_CATEGORY = By.cssSelector(".part-category");
    private static final By PART_KEYWORDS = By.cssSelector(".part-keywords");
    private static final By PART_UNITS = By.cssSelector(".part-units");
    private static final By PART_ACTIVE_STATUS = By.cssSelector(".part-active");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public PartDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets part name.
     *
     * @return Part name text
     */
    public String getPartName() {
        return getText(PART_NAME);
    }

    /**
     * Gets part description.
     *
     * @return Part description text
     */
    public String getPartDescription() {
        return getText(PART_DESCRIPTION);
    }

    /**
     * Clicks edit button.
     *
     * @return CreatePartPage (same form for edit)
     */
    public CreatePartPage clickEdit() {
        logger.info("Clicking edit button");
        click(EDIT_BUTTON);
        return new CreatePartPage(driver);
    }

    /**
     * Clicks stock tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickStockTab() {
        logger.info("Clicking stock tab");
        click(STOCK_TAB);
        return this;
    }

    /**
     * Clicks BOM tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickBomTab() {
        logger.info("Clicking BOM tab");
        click(BOM_TAB);
        return this;
    }

    /**
     * Clicks parameters tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickParametersTab() {
        logger.info("Clicking parameters tab");
        click(PARAMETERS_TAB);
        return this;
    }

    /**
     * Clicks variants tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickVariantsTab() {
        logger.info("Clicking variants tab");
        click(VARIANTS_TAB);
        return this;
    }

    /**
     * Clicks revisions tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickRevisionsTab() {
        logger.info("Clicking revisions tab");
        click(REVISIONS_TAB);
        return this;
    }

    /**
     * Clicks notes tab.
     *
     * @return PartDetailPage for method chaining
     */
    public PartDetailPage clickNotesTab() {
        logger.info("Clicking notes tab");
        click(NOTES_TAB);
        return this;
    }

    /**
     * Gets part IPN.
     *
     * @return IPN text
     */
    public String getPartIPN() {
        return getText(PART_IPN);
    }

    /**
     * Gets part category.
     *
     * @return Category text
     */
    public String getPartCategory() {
        return getText(PART_CATEGORY);
    }

    /**
     * Gets part keywords.
     *
     * @return Keywords text
     */
    public String getPartKeywords() {
        return getText(PART_KEYWORDS);
    }

    /**
     * Checks if edit button is displayed.
     *
     * @return True if edit button is displayed
     */
    public boolean isEditButtonDisplayed() {
        return isDisplayed(EDIT_BUTTON);
    }

    /**
     * Checks if part is active.
     *
     * @return True if active status is displayed
     */
    public boolean isPartActive() {
        return isDisplayed(PART_ACTIVE_STATUS);
    }
}
