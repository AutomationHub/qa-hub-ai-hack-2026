# Test Automation Commands

Two powerful commands to automate test cases from specifications to working code with PRs.

## Commands Overview

| Command | Purpose | Input | Output |
|---------|---------|-------|--------|
| `/automate-ui-test` | Automate UI test cases | Test Case ID | Updated Page Objects + Test Method + PR |
| `/automate-api-test` | Automate API test cases | Test Case ID | POJOs + Test Method + PR |

## 🎯 `/automate-ui-test <test-case-id>`

### What It Does

1. **Reads test case** from `test-cases/ui/`
2. **Analyzes framework** structure (Page Objects, test classes)
3. **Launches Playwright MCP agent** to inspect live application
4. **Finds real locators** for elements (CSS selectors, XPath)
5. **Updates Page Objects** with discovered locators
6. **Generates test method** following POM pattern
7. **Creates git branch** `automate-ui-<test-case-id>`
8. **Commits code** with descriptive message
9. **Creates Pull Request** with full documentation

### Usage

```bash
/automate-ui-test UI_PART_001
```

### What Gets Created/Updated

```
automation/ui/
├── src/main/java/com/inventree/ui/pages/
│   ├── LoginPage.java              # ✏️ Updated with real locators
│   ├── PartsListPage.java          # ✏️ Updated with real locators
│   └── CreatePartPage.java         # ✏️ Updated with real locators
└── src/test/java/com/inventree/ui/tests/
    └── PartCreationTest.java       # ✨ New test method added
```

### Example Test Case Format

```markdown
## Test Case ID: UI_PART_001

**Title:** Create a New Part via UI

**Preconditions:**
- User is logged in as admin
- User is on Parts page

**Steps:**
1. Click "Add Part" button
2. Enter name: "Test Resistor"
3. Enter description: "10K Ohm resistor"
4. Select category: "Electronics"
5. Click "Save" button

**Expected Result:**
- Part is created successfully
- Success message is displayed
- User is redirected to part detail page
- Part details match entered data
```

### Generated Code Example

**Page Object (PartsListPage.java):**
```java
// Before
private static final By ADD_PART_BUTTON = By.cssSelector("button.add-part"); // TODO: update selector

// After (from Playwright inspection)
private static final By ADD_PART_BUTTON = By.cssSelector("button[data-testid='add-part-btn']");
```

**Test Method (PartCreationTest.java):**
```java
@Test(groups = {"regression"}, priority = 5)
@Story("Test Case UI_PART_001")
@Description("Create a New Part via UI")
public void testCreatePartUI001() {
    logger.info("=== Starting Test: UI_PART_001 ===");

    // Login and navigate
    loginAsAdmin();
    DashboardPage dashboard = new DashboardPage(driver);
    PartsListPage partsPage = dashboard.navigateToParts();

    // Click Add Part button
    CreatePartPage createPage = partsPage.clickAddPart();

    // Fill form
    PartDetailPage detailPage = createPage
        .enterName("Test Resistor")
        .enterDescription("10K Ohm resistor")
        .selectCategory("Electronics")
        .clickSave();

    // Verify
    assertThat(detailPage.getPartName()).isEqualTo("Test Resistor");
    assertThat(detailPage.getPartDescription()).contains("10K Ohm resistor");

    logger.info("=== Test Passed: UI_PART_001 ===");
}
```

### Key Features

✅ **Playwright MCP Integration** - Inspects live application for real locators
✅ **Smart Locator Strategy** - Prefers CSS > data-testid > XPath
✅ **Page Object Pattern** - Maintains clean separation of concerns
✅ **Framework-Aware** - Follows existing coding conventions
✅ **Compilation Verified** - Runs `mvn compile` before committing
✅ **Auto PR Creation** - Full documentation in PR body

---

## 🚀 `/automate-api-test <test-case-id>`

### What It Does

1. **Reads test case** from `test-cases/api/`
2. **Analyzes framework** (BaseApi, POJOs, endpoints)
3. **Optionally launches API analysis agent** to inspect live API
4. **Creates/Updates POJOs** with Jackson annotations
5. **Adds endpoint constants** if needed
6. **Generates test method** with REST Assured
7. **Creates git branch** `automate-api-<test-case-id>`
8. **Commits code** with descriptive message
9. **Creates Pull Request** with full documentation

### Usage

```bash
/automate-api-test API_PART_001
```

### What Gets Created/Updated

```
automation/api/
├── src/main/java/com/inventree/api/
│   ├── models/
│   │   └── Part.java                 # ✏️ Updated/Created POJO
│   ├── endpoints/
│   │   └── PartEndpoints.java        # ✏️ Updated with new endpoints
│   └── utils/
│       └── TestDataGenerator.java    # ✏️ Updated with new test data
└── src/test/java/com/inventree/api/tests/
    └── PartCrudTest.java             # ✨ New test method added
```

### Example Test Case Format

```markdown
## Test Case ID: API_PART_001

**Title:** Create Part via POST API

**Endpoint:** POST /api/part/

**Authentication:** Token (admin user)

**Request Payload:**
```json
{
  "name": "Test Part",
  "description": "API test part",
  "category": 1,
  "active": true
}
```

**Expected Status Code:** 201 Created

**Expected Response:**
```json
{
  "pk": <integer>,
  "name": "Test Part",
  "description": "API test part",
  "category": 1,
  "active": true,
  ...
}
```

**Validations:**
- Response contains pk (not null)
- Name matches request
- Description matches request
- Category matches request
- Active is true
```

### Generated Code Example

**POJO (Part.java):**
```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {
    @JsonProperty("pk")
    private Integer pk;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private Integer category;

    @JsonProperty("active")
    private Boolean active;

    // Builder, getters, setters...
}
```

**Test Method (PartCrudTest.java):**
```java
@Test(groups = {"regression"}, priority = 12)
@Story("Test Case API_PART_001")
@Description("Create Part via POST API")
public void testCreatePartAPI001() {
    logger.info("=== Test: API_PART_001 ===");

    // Prepare test data
    Part newPart = Part.builder()
        .name("Test Part " + UUID.randomUUID().toString().substring(0, 8))
        .description("API test part")
        .category(1)
        .active(true)
        .build();

    // Make API request
    Response response = BaseApi.givenAdmin()
        .body(newPart)
        .post(PartEndpoints.PARTS);

    logResponse(response);

    // Verify response
    response.then()
        .statusCode(201)
        .body("name", equalTo(newPart.getName()))
        .body("description", equalTo(newPart.getDescription()))
        .body("category", equalTo(newPart.getCategory()))
        .body("active", equalTo(newPart.getActive()))
        .body("pk", notNullValue());

    // Additional validation with AssertJ
    Part createdPart = response.as(Part.class);
    assertThat(createdPart.getPk()).isNotNull();
    assertThat(createdPart.getName()).isEqualTo(newPart.getName());

    // Cleanup
    int partId = response.jsonPath().getInt("pk");
    createdPartIds.add(partId);

    logger.info("=== Test Passed: API_PART_001 ===");
}
```

### Key Features

✅ **POJO-Based** - Type-safe request/response handling
✅ **Builder Pattern** - Clean test data creation
✅ **REST Assured** - Fluent API for HTTP requests
✅ **Hamcrest + AssertJ** - Rich assertion library
✅ **Auto Cleanup** - Created resources deleted after test
✅ **Test Execution Verified** - Runs test before PR
✅ **Auto PR Creation** - Full documentation in PR body

---

## 🎬 Workflow Diagram

### UI Test Automation Flow

```
┌─────────────────┐
│  Test Case File │
│   (Markdown)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Read & Parse   │
│   Test Case     │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│  Launch Playwright MCP  │
│      Sub-Agent          │
│  - Navigate to app      │
│  - Take screenshots     │
│  - Inspect elements     │
│  - Find locators        │
└────────┬────────────────┘
         │
         ▼
┌─────────────────┐
│  Update Page    │
│    Objects      │
│ (Real Locators) │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Generate Test  │
│     Method      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Create Branch   │
│ Commit Changes  │
│   Create PR     │
└─────────────────┘
```

### API Test Automation Flow

```
┌─────────────────┐
│  Test Case File │
│   (Markdown)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Read & Parse   │
│   Test Case     │
│ - Endpoint      │
│ - Request       │
│ - Expected      │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│  Optional: Launch API   │
│   Analysis Sub-Agent    │
│  - Call live endpoint   │
│  - Analyze response     │
│  - Document fields      │
└────────┬────────────────┘
         │
         ▼
┌─────────────────┐
│ Create/Update   │
│     POJOs       │
│  (Jackson)      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Generate Test  │
│     Method      │
│ (REST Assured)  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Run Test      │
│  (mvn test)     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Create Branch   │
│ Commit Changes  │
│   Create PR     │
└─────────────────┘
```

---

## 📋 Requirements

### For Both Commands

✅ Git repository initialized
✅ GitHub CLI (`gh`) installed and authenticated
✅ Java 17+ and Maven installed
✅ Test case files in correct format

### UI Command Specific

✅ Playwright MCP server configured (for element inspection)
✅ Access to InvenTree demo server (https://demo.inventree.org)

### API Command Specific

✅ Network access to InvenTree API (https://demo.inventree.org/api/)
✅ Valid credentials in `automation/api/src/test/resources/config.properties`

---

## 🛠️ Sub-Agents Used

### UI Test Automation

**Playwright Inspection Agent:**
- **Purpose:** Inspect live application to find element locators
- **Tools:** Playwright MCP (navigate, screenshot, inspect)
- **Input:** List of elements to find (from test case steps)
- **Output:** Mapping of element names to locators (CSS, XPath)

### API Test Automation

**API Analysis Agent (Optional):**
- **Purpose:** Analyze API endpoint behavior and response structure
- **Tools:** REST Assured, Bash (curl)
- **Input:** API endpoint, sample requests
- **Output:** Response field mapping, validation rules, error scenarios

---

## 📊 Pull Request Template

Both commands create PRs with this structure:

```markdown
## Summary
Implements automated test for test case <ID>

## Changes
- ✅ Updated Page Objects / POJOs
- ✅ Added test method: `testXXX()`
- ✅ Verified compilation / test execution

## Test Case Details
**ID:** <ID>
**Title:** <Title>

<Test case specifics>

## Framework
<Framework details>

## Testing
```bash
<Commands to run the test>
```

## Checklist
- ✅ Code compiles without errors
- ✅ Test passes locally
- ✅ Follows framework patterns
- ✅ Includes proper assertions

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

---

## 🎓 Best Practices

### Writing Test Cases for Automation

**Good Test Case:**
```markdown
## Test Case ID: UI_PART_001
**Title:** Create Part with Required Fields
**Steps:**
1. Navigate to Parts page
2. Click "Add Part" button (locator needed)
3. Enter name: "Test Part"
4. Select category: "Electronics"
5. Click "Save" (locator needed)
**Expected:** Part created, redirected to detail page
```

**Bad Test Case:**
```markdown
## Test Case ID: UI_PART_001
**Title:** Create Part
**Steps:** Create a part
**Expected:** Part is created
```

### Test Case Checklist

- ✅ Clear, unique test case ID
- ✅ Descriptive title
- ✅ Explicit step-by-step instructions
- ✅ Specific test data values
- ✅ Clear expected results
- ✅ For API: include endpoint, method, payload, status code

---

## 🐛 Troubleshooting

### UI Command Issues

**Issue:** Playwright MCP not available
- **Solution:** Configure Playwright MCP server, or manually provide locators

**Issue:** Locators not found
- **Solution:** Take screenshots, use browser DevTools, update Page Objects manually

**Issue:** Test compilation fails
- **Solution:** Check imports, verify Page Object methods exist, fix syntax errors

### API Command Issues

**Issue:** POJO fields don't match response
- **Solution:** Run API analysis agent, or manually inspect API response

**Issue:** Test fails with 401
- **Solution:** Verify credentials in config.properties, check token generation

**Issue:** Test fails with 400
- **Solution:** Verify request payload matches API schema, check required fields

---

## 📚 Examples

### Example 1: Automate UI Test

```bash
# You have a test case UI_PART_003 for editing a part
/automate-ui-test UI_PART_003

# Output:
✅ UI Test Case UI_PART_003 Automated Successfully
📋 Test Case: Edit Part Name and Description
🌿 Branch: automate-ui-ui_part_003
📄 Files Modified:
   - automation/ui/src/main/java/com/inventree/ui/pages/PartDetailPage.java
   - automation/ui/src/test/java/com/inventree/ui/tests/PartEditTest.java
🔗 Pull Request: https://github.com/user/repo/pull/42
```

### Example 2: Automate API Test

```bash
# You have a test case API_PART_005 for deleting a part
/automate-api-test API_PART_005

# Output:
✅ API Test Case API_PART_005 Automated Successfully
📋 Test Case: Delete Part via API
🔗 Endpoint: DELETE /api/part/{id}/
🌿 Branch: automate-api-api_part_005
📄 Files Modified:
   - automation/api/src/test/java/com/inventree/api/tests/PartCrudTest.java
🔗 Pull Request: https://github.com/user/repo/pull/43
```

---

## 🚀 Getting Started

1. **Generate test cases** (manual or with AI)
2. **Save test cases** in `test-cases/ui/` or `test-cases/api/`
3. **Run automation command:**
   ```bash
   /automate-ui-test UI_PART_001
   # or
   /automate-api-test API_PART_001
   ```
4. **Review PR** created by the command
5. **Merge PR** once approved
6. **Run tests** in CI/CD pipeline

---

## 📖 Related Documentation

- [UI Framework README](../automation/ui/README.md)
- [API Framework README](../automation/api/README.md)
- [Test Case Format Guide](../test-cases/README.md)
- [Contributing Guide](../CONTRIBUTING.md)

---

**Note:** These commands are designed to work with the InvenTree Parts module test automation frameworks. Adapt the workflow for other applications as needed.
