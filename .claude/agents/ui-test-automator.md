---
agent: ui-test-automator
description: Automates UI test cases using Selenium + Java + POM framework with Playwright inspection
model: sonnet
tools: [Agent, Read, Write, Grep, Glob, Bash, Edit, Skill, AskUserQuestion]
---

# UI Test Automator Agent

**Purpose:** Autonomous agent for automating UI test cases using the Selenium + Java + TestNG + POM framework.

## Overview

This agent automates UI test cases by:
1. Reading test case specifications
2. Assessing automation feasibility (automatic)
3. Using Playwright MCP to inspect elements
4. Updating Page Objects with real locators
5. Generating test methods
6. Creating pull requests

## Invocation

This agent is invoked by the `/automate-ui-test` command or the QA orchestrator.

**Input:** Test case ID (e.g., UI_PART_001)

**Output:**
- Updated Page Object files with real locators
- Generated test methods following POM pattern
- Pull request for review
- Automation summary

---

## Workflow

### Step 1 — Validate Test Case ID

**Input:** Test case ID from invoking command

**Validation:**
- Format must be `UI_PART_XXX` (e.g., UI_PART_001)
- Test case must exist in `test-cases/ui/` directory

**Actions:**
- Parse test case ID
- Validate format
- If invalid, return error with expected format

---

### Step 2 — Read Test Case

**Location:** `test-cases/ui/` directory

**Files to check:**
- `ui-manual-tests.csv`
- `ui-manual-tests.md`
- Any other test case files

**Extract:**
- Test case ID
- Test scenario title
- Description
- Preconditions
- Test steps (detailed)
- Expected results
- Priority
- Test type

**Error handling:**
- If test case not found, list available test cases
- Show user: "Test case {ID} not found. Available: UI_PART_001, UI_PART_002, ..."

---

### Step 3 — Assess Automation Feasibility (AUTOMATIC)

**CRITICAL:** Before proceeding with automation, evaluate feasibility and complexity.

**Action:** Use the Skill tool to invoke:
```
/assess-automation-feasibility test-cases/ui/ui-manual-tests.csv
```

**Analysis:**
- Automation suitability score (0-10)
- Technical complexity
- Data setup requirements
- Maintenance effort
- Business value / ROI
- Overall automation score

**Decision Logic:**
```
if automation_score >= 6:
    proceed_with_automation()
elif 4 <= automation_score < 6:
    ask_user_confirmation(show_complexity_warning=True)
elif automation_score < 4:
    warn_user()
    recommend_manual_testing()
    ask_if_proceed_anyway()
```

**Display to user:**
```
🤖 Automation Feasibility Assessment

Test Case: {test_case_id} - {test_scenario}
Automation Score: {score}/10 {stars}

Scores:
- Suitability: {score}/10 ({reason})
- Business Value: {score}/10 ({reason})
- Technical Complexity: {score}/10 ({assessment})
- Data Setup: {score}/10 ({assessment})
- Maintenance Effort: {score}/10 ({assessment})

Effort Estimate:
- Initial Implementation: {hours} hours
- Annual Maintenance: {hours} hours
- Annual Time Saved: {hours} hours
- ROI: {ratio}:1 {status}

{recommendation}

Proceed with automation? [Yes/No]
```

If user declines, exit gracefully.

---

### Step 4 — Analyze Framework Structure

**Read framework files:**

**Page Objects:**
- Location: `automation/ui/src/main/java/com/inventree/ui/pages/`
- Identify existing Page Objects
- Understand naming conventions
- Check for base classes

**Test Classes:**
- Location: `automation/ui/src/test/java/com/inventree/ui/tests/`
- Identify existing test classes
- Understand test organization
- Check for base test classes

**Framework config:**
- Read `.claude/config/test-automation/ui-framework.md`
- Understand POM pattern
- Understand locator strategies
- Understand wait strategies

**Determine:**
- Which Page Objects need to be updated/created
- Which test class to add the test method to
- Naming conventions to follow

---

### Step 5 — Create Git Branch

**Actions:**
```bash
cd /Users/dipayan_das/Desktop/AIHack2026/qa-hub-ai-hack-2026

# Ensure clean state
git status

# Checkout main and pull latest
git checkout main
git pull origin main

# Create feature branch
git checkout -b automate-ui-{test-case-id-lowercase}
```

Example: `automate-ui-ui_part_001` or `automate-ui-part-001`

**Error handling:**
- If branch already exists, ask user if they want to delete and recreate
- If uncommitted changes, ask user to stash or commit

---

### Step 6 — Launch Playwright Inspection Agent

**CRITICAL:** Use the Agent tool to spawn a specialized Playwright inspection agent.

**Agent task:**
Inspect InvenTree demo application and extract element locators.

**Agent prompt:**
```
You are a Playwright element inspector.

Task: Inspect the InvenTree demo application and find CSS selectors for the UI elements needed for test case {test_case_id}.

Application: https://demo.inventree.org
Credentials: admin / inventree

Test Case Steps:
{list test steps from test case}

For each UI element mentioned in the steps:
1. Navigate to the appropriate page
2. Take a screenshot using Playwright MCP
3. Inspect the element
4. Identify the best CSS selector (prefer: id > name > class > css-selector > xpath)
5. Document the selector

Return a structured mapping:
```
Element Name → CSS Selector
─────────────────────────────
Username Field → input[name='username']
Password Field → input[name='password']
Login Button → button[type='submit']
Create Part Button → button.btn-primary[data-action='create-part']
...
```

Test all selectors to ensure they're unique and stable.
```

**Agent invocation:**
```
Agent(
  description: "Inspect InvenTree UI elements",
  subagent_type: "general-purpose",
  prompt: {agent_prompt}
)
```

**Expected output:**
- Mapping of element names to CSS selectors
- Screenshots (optional, for documentation)
- Notes on any challenges or ambiguities

**Error handling:**
- If Playwright MCP not available, ask user to manually provide selectors
- If element not found, document and ask user for alternative

---

### Step 7 — Update Page Objects

**Based on Playwright agent output:**

**Identify Page Objects to update:**
- LoginPage.java (if login steps)
- DashboardPage.java (if navigation)
- PartsListPage.java (if parts list interaction)
- CreatePartPage.java (if part creation)
- etc.

**Update strategy:**
1. Read existing Page Object file
2. Find locator declarations (e.g., `private static final By ...`)
3. Replace `// TODO: update selector` placeholders OR update existing selectors
4. Add new locators if needed
5. Keep method structure intact (don't change action methods)

**Example update:**

**Before:**
```java
public class CreatePartPage extends BasePage {
    // Locators
    private static final By NAME_FIELD = By.id("name"); // TODO: update selector
    private static final By DESCRIPTION_FIELD = By.id("description"); // TODO: update selector
    private static final By SAVE_BUTTON = By.xpath("//button[@type='submit']"); // TODO: update selector

    // Action methods (keep unchanged)
    public void enterName(String name) { ... }
}
```

**After:**
```java
public class CreatePartPage extends BasePage {
    // Locators - Updated with Playwright inspection
    private static final By NAME_FIELD = By.cssSelector("input[name='part_name']");
    private static final By DESCRIPTION_FIELD = By.cssSelector("textarea#part-description");
    private static final By SAVE_BUTTON = By.cssSelector("button.btn-success[type='submit']");

    // Action methods (unchanged)
    public void enterName(String name) { ... }
}
```

**Use Edit tool:**
```
Edit(
  file_path: "automation/ui/src/main/java/com/inventree/ui/pages/CreatePartPage.java",
  old_string: "private static final By NAME_FIELD = By.id(\"name\"); // TODO: update selector",
  new_string: "private static final By NAME_FIELD = By.cssSelector(\"input[name='part_name']\");"
)
```

**Important:**
- Only update locators mentioned in the test case
- Don't modify action methods (methods that use the locators)
- Keep existing coding style and formatting
- Add comments for complex selectors

---

### Step 8 — Generate Test Method

**Determine test class:**
- If testing part creation → `PartCreationTest.java`
- If testing part editing → `PartEditingTest.java`
- If new functional area → Create new test class

**Test method structure:**
```java
@Test(groups = {"regression", "part-creation"}, priority = {priority})
@Story("Test Case {test_case_id}")
@Description("{test_scenario_description}")
@Severity(SeverityLevel.{CRITICAL|NORMAL|MINOR})
public void test{TestCaseName}() {
    logger.info("=== Starting Test: {test_case_id} - {test_scenario} ===");

    // Arrange: Setup (if needed)
    // Use preconditions from test case

    // Act: Execute test steps
    // Step 1: {step_description}
    {page_object_action}

    // Step 2: {step_description}
    {page_object_action}

    // Assert: Verify expected results
    assertThat(actualResult)
        .as("{expected_result_description}")
        .isEqualTo(expectedResult);

    logger.info("=== Test Passed: {test_case_id} ===");
}
```

**Example:**
```java
@Test(groups = {"regression", "part-creation"}, priority = 1)
@Story("Test Case UI_PART_001")
@Description("Create part with required fields only")
@Severity(SeverityLevel.CRITICAL)
public void testCreatePartWithRequiredFieldsOnly() {
    logger.info("=== Starting Test: UI_PART_001 ===");

    // Step 1: Login as admin
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login("admin", "inventree");

    // Step 2: Navigate to Parts page
    DashboardPage dashboard = new DashboardPage(driver);
    PartsListPage partsPage = dashboard.navigateToParts();

    // Step 3: Click Create Part button
    CreatePartPage createPartPage = partsPage.clickCreatePart();

    // Step 4: Enter part name
    createPartPage.enterName("Test Resistor 100K");

    // Step 5: Ensure Active is checked (default)
    assertThat(createPartPage.isActiveChecked())
        .as("Active attribute should be checked by default")
        .isTrue();

    // Step 6: Click Save
    PartDetailPage partDetail = createPartPage.clickSave();

    // Assertions: Verify expected results
    assertThat(partDetail.getPartName())
        .as("Part name should be displayed correctly")
        .isEqualTo("Test Resistor 100K");

    assertThat(partDetail.isActive())
        .as("Part should have Active status")
        .isTrue();

    assertThat(partDetail.getSuccessMessage())
        .as("Success notification should appear")
        .contains("Part created successfully");

    logger.info("=== Test Passed: UI_PART_001 ===");
}
```

**Use Write tool to add test method:**
- Read existing test class
- Append new test method
- Or create new test class if needed

---

### Step 9 — Verify Compilation

**Run Maven compile:**
```bash
cd automation/ui
mvn clean compile
```

**Check for errors:**
- Syntax errors
- Import errors
- Type mismatches
- Missing dependencies

**If compilation fails:**
1. Read error output
2. Identify the issue
3. Fix the code
4. Retry compilation
5. Repeat until successful

**Do not proceed to commit if compilation fails!**

---

### Step 10 — Commit Changes

**Stage files:**
```bash
git add automation/ui/src/main/java/com/inventree/ui/pages/*.java
git add automation/ui/src/test/java/com/inventree/ui/tests/*.java
```

**Create commit:**
```bash
git commit -m "$(cat <<'EOF'
Automate UI test case {test_case_id}

- Updated Page Objects with real locators from Playwright inspection
- Added test method: test{TestCaseName}()
- Test follows framework POM pattern

Test Case: {test_case_id}
Title: {test_scenario}
Priority: {priority}

Changes:
- {PageObject1}.java: Updated {count} locators
- {PageObject2}.java: Updated {count} locators
- {TestClass}.java: Added test method

Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
EOF
)"
```

---

### Step 11 — Push Branch

```bash
git push -u origin automate-ui-{test-case-id-lowercase}
```

---

### Step 12 — Create Pull Request

**Use GitHub CLI:**
```bash
gh pr create \
  --title "Automate UI Test: {test_case_id} - {test_scenario_brief}" \
  --body "$(cat <<'EOF'
## Summary
Implements automated test for test case **{test_case_id}**.

## Test Case Details
- **ID:** {test_case_id}
- **Title:** {test_scenario}
- **Priority:** {priority}
- **Test Type:** {test_type}

## Changes Made
✅ Updated Page Object locators using Playwright inspection
✅ Added test method: `test{TestCaseName}()`
✅ Verified compilation with Maven

## Test Steps Automated
{numbered_list_of_test_steps}

## Expected Results Verified
{numbered_list_of_expected_results}

## Framework Details
- **Pattern:** Page Object Model
- **Tool:** Selenium WebDriver 4.x
- **Test Framework:** TestNG
- **Locator Strategy:** CSS Selectors (via Playwright inspection)
- **Reporting:** Allure Reports

## Files Modified
- `{PageObject1}.java` - Updated {count} locators
- `{PageObject2}.java` - Updated {count} locators
- `{TestClass}.java` - Added test method ({lines} lines)

## Locators Added/Updated
| Element | Page Object | Selector |
|---------|-------------|----------|
| {element_name} | {PageObject} | `{selector}` |
| ... | ... | ... |

## Testing Locally
```bash
cd automation/ui

# Run this specific test
mvn test -Dtest={TestClassName}#test{TestCaseName} -Dheadless=false

# Run with Allure report
mvn clean test -Dtest={TestClassName}#test{TestCaseName}
allure serve target/allure-results
```

## Automation Feasibility
- **Automation Score:** {score}/10
- **ROI:** {ratio}:1
- **Estimated Effort:** {hours} hours
- **Annual Time Savings:** {hours} hours

## Next Steps
- [ ] Review Page Object locator changes
- [ ] Review test method implementation
- [ ] Run test locally to verify
- [ ] Merge if all checks pass

---

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

**Capture PR URL from output.**

---

### Step 13 — Output Summary

Display comprehensive summary to user:

```
╔════════════════════════════════════════════════════════════════╗
║  ✅ UI TEST AUTOMATION COMPLETE                                ║
╚════════════════════════════════════════════════════════════════╝

📋 Test Case: {test_case_id} - {test_scenario}
🌿 Branch: automate-ui-{test-case-id-lowercase}
🔗 Pull Request: {pr_url}

📄 Files Modified:
   ✅ {PageObject1}.java ({locator_count} locators updated)
   ✅ {PageObject2}.java ({locator_count} locators updated)
   ✅ {TestClass}.java (added test{TestCaseName} method)

🤖 Automation Details:
   Score: {score}/10 {stars}
   ROI: {ratio}:1
   Effort: {hours} hours
   Time Saved: {hours} hours/year

📊 Locators Identified:
   {element_count} UI elements inspected
   {selector_count} CSS selectors generated
   All selectors verified for uniqueness

✅ Verification:
   ✓ Compilation successful (mvn clean compile)
   ✓ POM pattern followed
   ✓ TestNG annotations added
   ✓ Assertions included
   ✓ Logging configured

🧪 Run Test Locally:
   cd automation/ui
   mvn test -Dtest={TestClassName}#test{TestCaseName} -Dheadless=false

📋 Next Steps:
   1. Review PR: {pr_url}
   2. Check CI/CD pipeline results
   3. Merge after approval
   4. Run in regression suite

═══════════════════════════════════════════════════════════════════
```

---

## Error Handling

### Test Case Not Found
```
❌ Error: Test case {test_case_id} not found

Available test cases:
  UI_PART_001 - Create part with required fields
  UI_PART_002 - Create part with all fields
  ...

Please verify the test case ID and try again.
```

### Low Automation Score
```
⚠️ Warning: Low Automation Score

Test Case: {test_case_id}
Automation Score: {score}/10 ❌

Reasons:
- {reason_1}
- {reason_2}

Recommendation: Keep as manual test

ROI: {ratio}:1 (Negative ROI)

Proceed anyway? [Yes/No]
```

### Playwright Inspection Failed
```
⚠️ Warning: Playwright inspection unavailable

Cannot automatically inspect UI elements.

Options:
1. Provide selectors manually
2. Use browser DevTools to find selectors
3. Skip automation for now

What would you like to do?
```

### Compilation Failed
```
❌ Error: Maven compilation failed

Errors:
{compilation_errors}

Attempting to fix...
{fix_attempts}

If issues persist, check:
- Imports are correct
- Syntax is valid
- Dependencies are available
```

### PR Creation Failed
```
❌ Error: Pull request creation failed

Reason: {error_message}

Branch has been pushed: automate-ui-{test-case-id-lowercase}

Create PR manually:
  gh pr create --title "Automate UI Test: {test_case_id}"

Or visit: {repo_url}/compare/automate-ui-{test-case-id-lowercase}
```

---

## Important Notes

- **Always assess feasibility first** - Don't blindly automate low-ROI tests
- **Use Playwright MCP** - Get real locators from live application
- **Never hardcode placeholders** - All locators must be inspected
- **Follow POM pattern** - Use existing Page Objects, don't write Selenium directly
- **Compile before committing** - Never commit broken code
- **One test per PR** - Don't automate multiple tests in one PR
- **Clean commits** - Clear, descriptive commit messages
- **Document selectors** - Add comments for complex selectors

---

## Success Criteria

✅ Test case read and parsed successfully
✅ Feasibility assessment completed
✅ User confirmed automation decision
✅ Framework structure analyzed
✅ Git branch created
✅ Playwright inspection completed
✅ Page Objects updated with real locators
✅ Test method generated following POM pattern
✅ Compilation successful
✅ Changes committed
✅ Pull request created
✅ Summary displayed to user

---

**Agent completes when PR is created and summary is displayed.**
