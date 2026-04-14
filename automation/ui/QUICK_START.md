# Quick Start Guide

## Prerequisites Check

```bash
# Verify Java 17+
java -version

# Verify Maven
mvn -version
```

If not installed, see installation instructions in README.md

## First-Time Setup

### 1. Compile Project (downloads dependencies)

```bash
cd automation/ui
mvn clean compile
```

### 2. Update Locators

**CRITICAL:** Before running tests, update placeholder selectors in page objects.

Files to update (all in `src/main/java/com/inventree/ui/pages/`):
- LoginPage.java
- DashboardPage.java
- PartsListPage.java
- PartDetailPage.java
- CreatePartPage.java
- PartCategoryPage.java

How to find selectors:
1. Open https://demo.inventree.org
2. Login with admin/inventree
3. Press F12 (DevTools)
4. Use Element Picker to inspect elements
5. Copy CSS selectors or XPath
6. Update locators in page objects

Example:
```java
// Find this in page objects:
// TODO: update selector
private static final By USERNAME_FIELD = By.id("username");

// Replace with actual selector from app:
private static final By USERNAME_FIELD = By.cssSelector("input[name='login']");
```

## Run Tests

### Quick Test Run

```bash
# Run all tests (headless mode)
mvn clean test

# Run with browser visible (for debugging)
mvn test -Dheadless=false

# Run smoke tests only
mvn test -Dgroups=smoke
```

### View Reports

```bash
# Generate and open Allure report
mvn allure:serve
```

## Common Commands

| Command | Description |
|---------|-------------|
| `mvn clean compile` | Compile code |
| `mvn clean test` | Run all tests |
| `mvn test -Dtest=PartCreationTest` | Run specific test class |
| `mvn test -Dgroups=smoke` | Run smoke tests |
| `mvn test -Dheadless=false` | Run with visible browser |
| `mvn allure:report` | Generate Allure report |
| `mvn allure:serve` | Generate and open report |

## File Locations

| File | Purpose |
|------|---------|
| `config.properties` | Test configuration (URL, browser, credentials) |
| `testng.xml` | Test suite configuration |
| `testdata/parts.json` | Test data |
| `target/logs/test-execution.log` | Test logs |
| `target/allure-results/` | Test results for reporting |

## Troubleshooting

### Maven not found
```bash
# Install Maven first (see README.md)
brew install maven  # MacOS
```

### Java version error
```bash
# Check Java version
java -version
# Must be 17 or higher
```

### Tests fail with "element not found"
- Update locators in page objects (see step 2 above)
- Use browser DevTools to find correct selectors

### WebDriver issues
```bash
# Clear WebDriverManager cache
rm -rf ~/.m2/repository/webdriver/
mvn clean install
```

## Next Steps

1. ✅ Install Java 17 and Maven
2. ✅ Compile project
3. ⚠️ **Update locators** (REQUIRED before running tests)
4. ✅ Run smoke tests
5. ✅ View Allure reports
6. ✅ Write more test cases

## Need Help?

See full documentation in `README.md`
