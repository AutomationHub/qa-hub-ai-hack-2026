---
description: "Scaffold the UI automation framework (Selenium + Java + TestNG + POM) in automation/ui/. Creates Maven project, dependencies, base classes, page objects, config, and test runner."
disable-model-invocation: true
allowed-tools: Write, Read, Glob, Grep, Bash, Agent
---

# UI Automation Framework Setup

You are a Senior SDET setting up a **Selenium + Java + Page Object Model** test automation framework.

## Configuration

- **AUT details and credentials** are in `CLAUDE.md` (already in context).
- **Framework spec** (stack, project structure, dependencies, plugins, config, design decisions) is in `.claude/input-config/ui-automation-config.md` — **read this file first**.

## Workflow

### Step 1 — Read Config
Read `.claude/input-config/ui-automation-config.md` to load the full framework specification.

### Step 2 — Create Project Structure
Create the **exact directory and file structure** defined in the config under `automation/ui/`. Create every directory and every file listed.

### Step 3 — Generate pom.xml
Create `automation/ui/pom.xml` with:
- `groupId`: `com.inventree`
- `artifactId`: `inventree-ui-tests`
- All dependencies from the config (exact versions specified)
- All plugins from the config
- Java 17 compiler settings
- UTF-8 encoding properties

### Step 4 — Generate Base Classes

**DriverFactory.java** — WebDriverManager-based driver creation:
- Support Chrome, Firefox, Edge via config
- Headless mode toggle from config
- Thread-safe (ThreadLocal) for parallel execution

**BasePage.java** — Abstract page object base:
- Constructor takes `WebDriver`
- Reusable methods: `click(By)`, `type(By, String)`, `getText(By)`, `isDisplayed(By)`, `waitForVisible(By)`, `waitForClickable(By)`, `selectDropdown(By, String)`
- Uses explicit waits from `WaitUtils`
- Logging on each action

**BaseTest.java** — Test lifecycle:
- `@BeforeMethod` — init driver via DriverFactory, navigate to base URL
- `@AfterMethod` — quit driver, capture screenshot on failure
- Login helper method using `LoginPage`
- Config loading from `config.properties`

**ConfigReader.java** — Properties loader:
- Loads from `config.properties`
- System property overrides (`-Dbase.url=...` for CI)
- Typed getters: `getString()`, `getInt()`, `getBoolean()`

**WaitUtils.java** — Explicit wait helpers:
- `waitForVisible(WebDriver, By, int)`
- `waitForClickable(WebDriver, By, int)`
- `waitForInvisible(WebDriver, By, int)`
- `waitForTextPresent(WebDriver, By, String, int)`

**TestDataReader.java** — JSON test data loader:
- Reads JSON files from `src/test/resources/testdata/`
- Returns as Map or List via Jackson

### Step 5 — Generate Page Objects

Create page objects following POM pattern. Each page class:
- Extends `BasePage`
- Has `By` locator constants at the top
- Has action methods that return page objects (fluent chaining)
- Use **placeholder CSS/XPath selectors** with `// TODO: update selector` comments

Pages to create:
- **LoginPage** — username field, password field, login button, login method returning DashboardPage
- **DashboardPage** — navigation to Parts, sidebar links
- **PartsListPage** — parts table, search box, add part button, category sidebar, filter controls
- **PartDetailPage** — tabs (Stock, BOM, Parameters, Variants, Revisions, etc.), edit button, part attributes display
- **CreatePartPage** — form fields (name, description, category, IPN, keywords, etc.), attribute checkboxes, save button
- **PartCategoryPage** — category tree, category detail, subcategories, parts in category

### Step 6 — Generate Config Files

- `config.properties` — from the config spec
- `testng.xml` — suite with test groups (smoke, regression), parallel mode enabled
- `logback-test.xml` — console + file appenders, INFO level
- `testdata/parts.json` — sample test data (2-3 valid parts with all fields)

### Step 7 — Generate Sample Test Class

Create `PartCreationTest.java` with:
- 2-3 `@Test` methods as examples (create part happy path, missing required field, duplicate name)
- Uses page objects properly
- Proper assertions
- `@BeforeMethod` login flow
- Comments explaining the pattern for adding more tests

### Step 8 — Verify Build

Run `mvn compile -f automation/ui/pom.xml` to verify the project compiles. Fix any compilation errors.

## Quality Criteria

- All files must compile without errors
- Page objects must follow POM pattern strictly (no test logic in pages)
- Base classes must be truly reusable (no hardcoded values)
- Config must support CI overrides via system properties
- Selectors should be clearly marked as placeholders where actual selectors are unknown
