package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Part Category Page.
 */
public class PartCategoryPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By CATEGORY_TREE = By.cssSelector(".category-tree");
    private static final By CATEGORY_NAME = By.cssSelector("h2.category-name");
    private static final By CATEGORY_DESCRIPTION = By.cssSelector(".category-description");
    private static final By SUBCATEGORIES_LIST = By.cssSelector(".subcategories-list");
    private static final By PARTS_IN_CATEGORY = By.cssSelector(".parts-in-category");
    private static final By ADD_SUBCATEGORY_BUTTON = By.cssSelector("button.add-subcategory");
    private static final By EDIT_CATEGORY_BUTTON = By.cssSelector("button.edit-category");
    private static final By FIRST_SUBCATEGORY = By.cssSelector(".subcategories-list li:first-child");
    private static final By PARTS_TABLE = By.cssSelector("table.parts-table");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public PartCategoryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Gets category name.
     *
     * @return Category name text
     */
    public String getCategoryName() {
        return getText(CATEGORY_NAME);
    }

    /**
     * Gets category description.
     *
     * @return Category description text
     */
    public String getCategoryDescription() {
        return getText(CATEGORY_DESCRIPTION);
    }

    /**
     * Checks if category tree is displayed.
     *
     * @return True if category tree is displayed
     */
    public boolean isCategoryTreeDisplayed() {
        return isDisplayed(CATEGORY_TREE);
    }

    /**
     * Checks if subcategories list is displayed.
     *
     * @return True if subcategories list is displayed
     */
    public boolean isSubcategoriesDisplayed() {
        return isDisplayed(SUBCATEGORIES_LIST);
    }

    /**
     * Clicks first subcategory.
     *
     * @return PartCategoryPage
     */
    public PartCategoryPage clickFirstSubcategory() {
        logger.info("Clicking first subcategory");
        click(FIRST_SUBCATEGORY);
        return this;
    }

    /**
     * Clicks add subcategory button.
     *
     * @return PartCategoryPage for method chaining
     */
    public PartCategoryPage clickAddSubcategory() {
        logger.info("Clicking add subcategory button");
        click(ADD_SUBCATEGORY_BUTTON);
        return this;
    }

    /**
     * Clicks edit category button.
     *
     * @return PartCategoryPage for method chaining
     */
    public PartCategoryPage clickEditCategory() {
        logger.info("Clicking edit category button");
        click(EDIT_CATEGORY_BUTTON);
        return this;
    }

    /**
     * Checks if parts table is displayed in category.
     *
     * @return True if parts table is displayed
     */
    public boolean isPartsTableDisplayed() {
        return isDisplayed(PARTS_TABLE);
    }

    /**
     * Gets count of parts in category.
     *
     * @return Parts count text
     */
    public String getPartsCount() {
        return getText(PARTS_IN_CATEGORY);
    }
}
