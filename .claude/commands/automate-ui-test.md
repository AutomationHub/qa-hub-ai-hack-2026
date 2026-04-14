---
skill: automate-ui-test
description: Automates a UI test case by inspecting elements with Playwright MCP, generating POM code, creating test methods, and submitting a PR
args: test-case-id
triggers:
  - "automate ui test"
  - "generate ui test"
  - "implement ui test case"
---

# UI Test Automation Command

Automates a UI test case from test-cases/ui/ by:
1. Reading the test case specification
2. Using Playwright MCP to inspect elements and generate locators
3. Updating Page Objects with real locators
4. Generating test methods
5. Creating a PR

## Usage

```
/automate-ui-test <test-case-id>
```

Example:
```
/automate-ui-test UI_PART_001
```

## Workflow

You are an expert SDET implementing UI test automation using the existing Selenium + Java + POM framework.

### Step 1 — Parse Arguments

Extract the test case ID from the args parameter. Format: `UI_PART_XXX`

### Step 2 — Read Test Case

Read the test case from `test-cases/ui/` directory:
- Look for files matching the test case ID
- Parse test case details: title, preconditions, steps, expected results
- If test case not found, show error and list available test cases

### Step 3 — Analyze Framework

Read and understand the existing framework structure:
- `automation/ui/src/main/java/com/inventree/ui/pages/*.java` — Page Objects
- `automation/ui/src/test/java/com/inventree/ui/tests/*.java` — Test classes
- Understand the POM pattern and coding conventions

### Step 4 — Create Git Branch

```bash
git checkout main
git pull origin main
git checkout -b automate-ui-<test-case-id-lowercase>
```

### Step 5 — Launch Playwright Inspection Agent

**CRITICAL:** Use the Agent tool to spawn a specialized agent for Playwright inspection.

The agent should:
- Open the InvenTree demo application (https://demo.inventree.org)
- Login with admin credentials (admin/inventree)
- Navigate to the pages mentioned in the test case
- Use Playwright MCP `playwright_navigate` and `playwright_screenshot` to inspect elements
- For each element in the test case steps:
  - Take screenshots of the page
  - Identify the element (button, input field, link, etc.)
  - Use browser DevTools to find CSS selectors or XPath
  - Document the locator strategy (prefer CSS > XPath > ID)
- Return a mapping of element names to locators

Example agent prompt:
```
Use Playwright MCP to inspect InvenTree demo app and find locators for:
- Login page: username field, password field, login button
- Parts page: add part button, search box, parts table
- Create Part form: name field, description field, category dropdown, save button

For each element:
1. Navigate to the page
2. Take a screenshot
3. Inspect the element
4. Provide the best CSS selector

Return a structured list of element names and their locators.
```

### Step 6 — Update Page Objects

Based on the locators from the Playwright agent:
- Identify which Page Object file needs updates (e.g., LoginPage.java, PartsListPage.java)
- Replace `// TODO: update selector` placeholders with real locators
- Update only the locators relevant to this test case
- Keep the existing method structure intact

Example:
```java
// Before
private static final By USERNAME_FIELD = By.id("username"); // TODO: update selector

// After (using real locator from Playwright)
private static final By USERNAME_FIELD = By.cssSelector("input[name='login']");
```

### Step 7 — Generate Test Method

Create a new test method in the appropriate test class (or create new test class if needed):
- Follow the pattern in existing tests (e.g., PartCreationTest.java)
- Use the Page Objects (don't write Selenium code directly)
- Include proper assertions for expected results
- Add TestNG annotations: `@Test`, groups, Allure annotations
- Follow the test case steps exactly

Example structure:
```java
@Test(groups = {"regression"}, priority = X)
@Story("Test Case UI_PART_XXX")
@Description("Test case description from test case file")
public void testXXX() {
    logger.info("=== Starting Test: UI_PART_XXX ===");

    // Step 1: Navigate and login
    loginAsAdmin();
    DashboardPage dashboard = new DashboardPage(driver);

    // Step 2: Navigate to Parts page
    PartsListPage partsPage = dashboard.navigateToParts();

    // Step 3: ... follow test case steps

    // Assertions
    assertThat(actual).isEqualTo(expected);

    logger.info("=== Test Passed ===");
}
```

### Step 8 — Verify Compilation

Run Maven compile to ensure no errors:
```bash
cd automation/ui
mvn clean compile
```

If compilation fails, fix the errors before proceeding.

### Step 9 — Commit Changes

```bash
git add automation/ui/src/main/java/com/inventree/ui/pages/*.java
git add automation/ui/src/test/java/com/inventree/ui/tests/*.java
git commit -m "$(cat <<'EOF'
Automate UI test case <test-case-id>

- Updated Page Objects with real locators from Playwright inspection
- Added test method for <test-case-description>
- Test follows framework POM pattern

Test Case: <test-case-id>
Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
EOF
)"
```

### Step 10 — Create Pull Request

Use GitHub CLI to create PR:
```bash
gh pr create --title "Automate UI Test: <test-case-id>" --body "$(cat <<'EOF'
## Summary
Implements automated test for test case <test-case-id>

## Changes
- ✅ Updated Page Object locators using Playwright inspection
- ✅ Added test method: `testXXX()`
- ✅ Verified compilation with Maven

## Test Case Details
**ID:** <test-case-id>
**Title:** <test-case-title>

**Steps Automated:**
<list of steps from test case>

## Framework
- **Pattern:** Page Object Model
- **Tool:** Selenium WebDriver 4.x
- **Test Framework:** TestNG
- **Locator Strategy:** CSS Selectors (via Playwright inspection)

## Testing
```bash
cd automation/ui
mvn test -Dtest=<TestClassName>#<testMethodName> -Dheadless=false
```

## Locators Added/Updated
<list of page objects and locators updated>

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

### Step 11 — Output Summary

Provide a summary to the user:
```
✅ UI Test Case <test-case-id> Automated Successfully

📋 Test Case: <test-case-title>
🌿 Branch: automate-ui-<test-case-id-lowercase>
📄 Files Modified:
   - automation/ui/src/main/java/com/inventree/ui/pages/<PageObject>.java
   - automation/ui/src/test/java/com/inventree/ui/tests/<TestClass>.java

🔗 Pull Request: <PR URL>

🧪 Run the test locally:
   cd automation/ui
   mvn test -Dtest=<TestClass>#<testMethod> -Dheadless=false
```

## Important Notes

- **Always use Playwright MCP agent** to get real locators from the live application
- **Never hardcode placeholder locators** - all locators must be inspected
- **Follow existing framework patterns** - don't create new base classes or utilities
- **Keep Page Objects clean** - only locators and action methods, no assertions
- **Test methods have assertions** - verify expected results from test case
- **Compile before committing** - `mvn clean compile` must pass
- **One test case per PR** - don't automate multiple test cases in one PR

## Error Handling

If Playwright MCP is not available:
- Try using browser DevTools manually
- Provide instructions to user on how to find locators
- Document the locators needed and ask user to provide them

If test case file not found:
- List available test cases in test-cases/ui/
- Ask user to verify the test case ID

If compilation fails:
- Show the compilation errors
- Fix syntax errors
- Verify imports and class names
- Do not create PR until compilation succeeds

## Example Usage

```bash
# Automate test case UI_PART_001
/automate-ui-test UI_PART_001

# This will:
# 1. Read test-cases/ui/part-creation-tests.md (or similar)
# 2. Use Playwright to inspect elements on demo.inventree.org
# 3. Update CreatePartPage.java with real locators
# 4. Add test method to PartCreationTest.java
# 5. Commit and create PR
```
