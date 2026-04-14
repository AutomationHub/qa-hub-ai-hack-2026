package com.inventree.ui.pages;

import com.inventree.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Create Part Page (also used for editing parts).
 */
public class CreatePartPage extends BasePage {

    // Locators
    // TODO: update selectors based on actual application
    private static final By NAME_FIELD = By.id("name");
    private static final By DESCRIPTION_FIELD = By.id("description");
    private static final By CATEGORY_DROPDOWN = By.id("category");
    private static final By IPN_FIELD = By.id("IPN");
    private static final By REVISION_FIELD = By.id("revision");
    private static final By KEYWORDS_FIELD = By.id("keywords");
    private static final By UNITS_FIELD = By.id("units");
    private static final By LINK_FIELD = By.id("link");

    // Attribute checkboxes
    private static final By ACTIVE_CHECKBOX = By.id("active");
    private static final By TEMPLATE_CHECKBOX = By.id("is_template");
    private static final By ASSEMBLY_CHECKBOX = By.id("assembly");
    private static final By COMPONENT_CHECKBOX = By.id("component");
    private static final By TRACKABLE_CHECKBOX = By.id("trackable");
    private static final By PURCHASEABLE_CHECKBOX = By.id("purchaseable");
    private static final By SALEABLE_CHECKBOX = By.id("saleable");
    private static final By VIRTUAL_CHECKBOX = By.id("virtual");

    // Buttons
    private static final By SAVE_BUTTON = By.cssSelector("button[type='submit']");
    private static final By CANCEL_BUTTON = By.cssSelector("button.cancel");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".alert-success");
    private static final By ERROR_MESSAGE = By.cssSelector(".alert-danger");
    private static final By VALIDATION_ERROR = By.cssSelector(".invalid-feedback");

    /**
     * Constructor.
     *
     * @param driver WebDriver instance
     */
    public CreatePartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enters part name.
     *
     * @param name Part name
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterName(String name) {
        type(NAME_FIELD, name);
        return this;
    }

    /**
     * Enters part description.
     *
     * @param description Part description
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterDescription(String description) {
        type(DESCRIPTION_FIELD, description);
        return this;
    }

    /**
     * Selects category.
     *
     * @param category Category name
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage selectCategory(String category) {
        selectDropdown(CATEGORY_DROPDOWN, category);
        return this;
    }

    /**
     * Enters IPN (Internal Part Number).
     *
     * @param ipn IPN value
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterIPN(String ipn) {
        type(IPN_FIELD, ipn);
        return this;
    }

    /**
     * Enters revision.
     *
     * @param revision Revision value
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterRevision(String revision) {
        type(REVISION_FIELD, revision);
        return this;
    }

    /**
     * Enters keywords.
     *
     * @param keywords Keywords
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterKeywords(String keywords) {
        type(KEYWORDS_FIELD, keywords);
        return this;
    }

    /**
     * Enters units.
     *
     * @param units Units value
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterUnits(String units) {
        type(UNITS_FIELD, units);
        return this;
    }

    /**
     * Enters link.
     *
     * @param link Link URL
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage enterLink(String link) {
        type(LINK_FIELD, link);
        return this;
    }

    /**
     * Sets active checkbox.
     *
     * @param active True to check, false to uncheck
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage setActive(boolean active) {
        if (active) {
            click(ACTIVE_CHECKBOX);
        }
        return this;
    }

    /**
     * Sets template checkbox.
     *
     * @param template True to check, false to uncheck
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage setTemplate(boolean template) {
        if (template) {
            click(TEMPLATE_CHECKBOX);
        }
        return this;
    }

    /**
     * Sets assembly checkbox.
     *
     * @param assembly True to check, false to uncheck
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage setAssembly(boolean assembly) {
        if (assembly) {
            click(ASSEMBLY_CHECKBOX);
        }
        return this;
    }

    /**
     * Sets component checkbox.
     *
     * @param component True to check, false to uncheck
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage setComponent(boolean component) {
        if (component) {
            click(COMPONENT_CHECKBOX);
        }
        return this;
    }

    /**
     * Sets purchaseable checkbox.
     *
     * @param purchaseable True to check, false to uncheck
     * @return CreatePartPage for method chaining
     */
    public CreatePartPage setPurchaseable(boolean purchaseable) {
        if (purchaseable) {
            click(PURCHASEABLE_CHECKBOX);
        }
        return this;
    }

    /**
     * Clicks save button.
     *
     * @return PartDetailPage after successful save
     */
    public PartDetailPage clickSave() {
        logger.info("Clicking save button");
        click(SAVE_BUTTON);
        return new PartDetailPage(driver);
    }

    /**
     * Clicks save button expecting to stay on form (validation error).
     *
     * @return CreatePartPage
     */
    public CreatePartPage clickSaveExpectingError() {
        logger.info("Clicking save button (expecting error)");
        click(SAVE_BUTTON);
        return this;
    }

    /**
     * Checks if success message is displayed.
     *
     * @return True if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(SUCCESS_MESSAGE);
    }

    /**
     * Checks if error message is displayed.
     *
     * @return True if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    /**
     * Checks if validation error is displayed.
     *
     * @return True if validation error is displayed
     */
    public boolean isValidationErrorDisplayed() {
        return isDisplayed(VALIDATION_ERROR);
    }

    /**
     * Gets validation error message.
     *
     * @return Validation error text
     */
    public String getValidationError() {
        return getText(VALIDATION_ERROR);
    }
}
