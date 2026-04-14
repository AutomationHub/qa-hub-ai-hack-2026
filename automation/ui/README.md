# InvenTree UI Automation Framework

Selenium + Java + Page Object Model test automation framework for InvenTree Parts module testing.

## Prerequisites

- **Java 17+** - [Download](https://adoptium.net/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Chrome/Firefox/Edge Browser**

## Framework Stack

- **Language:** Java 17
- **Build Tool:** Maven
- **Test Runner:** TestNG
- **Browser Automation:** Selenium WebDriver 4.27.0
- **Driver Management:** WebDriverManager 5.9.2
- **Design Pattern:** Page Object Model (POM)
- **Reporting:** Allure Reports
- **Logging:** SLF4J + Logback
- **Assertions:** TestNG + AssertJ
- **Test Data:** JSON files via Jackson

## Project Structure

```
automation/ui/
├── pom.xml                                 # Maven project configuration
├── testng.xml                              # TestNG suite configuration
├── src/
│   ├── main/java/com/inventree/ui/
│   │   ├── base/
│   │   │   └── BasePage.java              # Common POM methods
│   │   ├── pages/                         # Page Objects
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   ├── PartsListPage.java
│   │   │   ├── PartDetailPage.java
│   │   │   ├── CreatePartPage.java
│   │   │   └── PartCategoryPage.java
│   │   ├── config/
│   │   │   └── ConfigReader.java          # Configuration loader
│   │   └── utils/
│   │       ├── DriverFactory.java         # WebDriver factory
│   │       ├── WaitUtils.java             # Explicit wait helpers
│   │       └── TestDataReader.java        # JSON data loader
│   └── test/
│       ├── java/com/inventree/ui/tests/
│       │   ├── BaseTest.java              # Test lifecycle management
│       │   └── PartCreationTest.java      # Sample test class
│       └── resources/
│           ├── config.properties          # Test configuration
│           ├── testdata/
│           │   └── parts.json             # Test data
│           └── logback-test.xml           # Logging configuration
```

## Installation & Setup

### 1. Install Java 17

```bash
# MacOS (using Homebrew)
brew install openjdk@17

# Linux (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install openjdk-17-jdk

# Windows
# Download from https://adoptium.net/
```

Verify installation:
```bash
java -version
# Should show: openjdk version "17.x.x"
```

### 2. Install Maven

```bash
# MacOS (using Homebrew)
brew install maven

# Linux (Ubuntu/Debian)
sudo apt-get install maven

# Windows
# Download from https://maven.apache.org/download.cgi
# Extract and add to PATH
```

Verify installation:
```bash
mvn -version
# Should show: Apache Maven 3.x.x
```

### 3. Compile the Project

```bash
cd automation/ui
mvn clean compile
```

This will:
- Download all dependencies
- Compile the code
- Set up WebDriverManager (auto-downloads browser drivers)

### 4. Update Locators

**IMPORTANT:** The page objects contain placeholder CSS/XPath selectors marked with `// TODO: update selector` comments.

Before running tests, you must:
1. Inspect the InvenTree demo application at https://demo.inventree.org
2. Update locators in page object files under `src/main/java/com/inventree/ui/pages/`
3. Use browser DevTools to find correct element selectors

Example:
```java
// Before (placeholder):
private static final By USERNAME_FIELD = By.id("username");

// After (actual selector from app):
private static final By USERNAME_FIELD = By.cssSelector("input[name='login']");
```

## Configuration

Edit `src/test/resources/config.properties` to customize:

```properties
# Browser settings
browser=chrome          # chrome, firefox, or edge
headless=true          # true for CI, false for local debugging

# Base URL
base.url=https://demo.inventree.org

# Timeouts (seconds)
implicit.wait=10
explicit.wait=15

# Credentials (already configured for InvenTree demo)
admin.username=admin
admin.password=inventree
```

### CI/CD Overrides

Override properties via system properties:
```bash
mvn test -Dbase.url=https://staging.example.com -Dheadless=true
```

## Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Class

```bash
mvn test -Dtest=PartCreationTest
```

### Run by TestNG Groups

```bash
# Smoke tests only
mvn test -Dgroups=smoke

# Regression tests
mvn test -Dgroups=regression
```

### Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run in Non-Headless Mode (for debugging)

```bash
mvn test -Dheadless=false
```

## Test Data

Test data is stored in JSON format: `src/test/resources/testdata/parts.json`

Example usage in tests:
```java
List<Map<String, Object>> partData = TestDataReader.readJsonAsList("parts.json");
Map<String, Object> testPart = partData.get(0);

String partName = (String) testPart.get("name");
String description = (String) testPart.get("description");
```

Add more test data files as needed for different test scenarios.

## Generating Allure Reports

### 1. Run Tests with Allure

```bash
mvn clean test
```

### 2. Generate Report

```bash
mvn allure:report
```

### 3. View Report

```bash
mvn allure:serve
```

This opens the report in your browser at http://localhost:port

## Logging

Logs are written to:
- **Console:** Real-time output during test execution
- **File:** `target/logs/test-execution.log`

Log levels:
- `com.inventree` package: DEBUG
- Selenium: WARN
- Root: INFO

Configure in `src/test/resources/logback-test.xml`

## Writing New Tests

### 1. Extend BaseTest

```java
public class MyNewTest extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        loginAsAdmin(); // or loginAsAllAccess(), loginAsReader()
    }

    @Test(groups = {"smoke"})
    public void testSomething() {
        // Test logic here
    }
}
```

### 2. Use Page Objects

```java
@Test
public void testPartCreation() {
    DashboardPage dashboard = new DashboardPage(driver);
    PartsListPage partsPage = dashboard.navigateToParts();

    CreatePartPage createPage = partsPage.clickAddPart();
    PartDetailPage detailPage = createPage
        .enterName("New Part")
        .enterDescription("Description")
        .clickSave();

    assertThat(detailPage.getPartName()).isEqualTo("New Part");
}
```

### 3. Follow POM Pattern

- All page interactions through page objects
- No Selenium WebDriver calls in test classes
- Use fluent API (method chaining) for readability
- Keep page objects simple - no business logic
- Test assertions in test classes, not page objects

## Parallel Execution

Configured in `testng.xml`:
```xml
<suite name="Test Suite" parallel="methods" thread-count="3">
```

Supported parallel modes:
- `methods` - Run test methods in parallel
- `classes` - Run test classes in parallel
- `tests` - Run test tags in parallel

Adjust `thread-count` based on system resources.

## Troubleshooting

### Issue: Tests fail with "Element not found"

**Solution:** Update locators in page objects. Use browser DevTools to inspect elements.

### Issue: WebDriver crashes or hangs

**Solution:**
1. Update WebDriverManager: `mvn clean install -U`
2. Clear WebDriverManager cache: `~/.m2/repository/webdriver/`
3. Try different browser: `-Dbrowser=firefox`

### Issue: Tests slow in headless mode

**Solution:** Increase timeouts in `config.properties`:
```properties
implicit.wait=15
explicit.wait=20
```

### Issue: Screenshots not captured on failure

**Solution:** Ensure Allure listener is configured in `testng.xml`:
```xml
<listeners>
    <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
</listeners>
```

## Next Steps

1. **Update Locators:** Inspect InvenTree app and update all `// TODO: update selector` locators
2. **Run Smoke Tests:** Verify framework works with actual app
3. **Add More Tests:** Create test classes for:
   - Part editing
   - Part deletion
   - Part categories
   - Stock management
   - BOM operations
4. **CI/CD Integration:** Set up pipeline with `mvn clean test` command

## Application Under Test

- **Demo URL:** https://demo.inventree.org
- **API Docs:** https://docs.inventree.org/en/stable/api/schema/part/
- **UI Docs:** https://docs.inventree.org/en/stable/part/
- **Demo resets daily** - Data created during tests is not persistent

### Test Credentials

| Account | Password | Access Level |
|---------|----------|-------------|
| admin | inventree | Superuser |
| allaccess | nolimits | Full CRUD |
| engineer | partsonly | Parts/stock only |
| reader | readonly | View-only |

## Framework Features

✅ **Thread-safe** - Parallel test execution support
✅ **Auto driver management** - No manual driver downloads
✅ **Cross-browser** - Chrome, Firefox, Edge
✅ **Headless mode** - CI/CD ready
✅ **Allure reports** - Beautiful test reports
✅ **JSON test data** - Easy data management
✅ **Explicit waits** - Reliable element interactions
✅ **Screenshot on failure** - Debug failed tests
✅ **Fluent API** - Readable test code
✅ **CI overrides** - Runtime configuration

## Contact

For issues or questions about the framework, refer to project documentation or contact the QA team.
