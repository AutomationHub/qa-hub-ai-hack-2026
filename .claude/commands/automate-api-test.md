---
skill: automate-api-test
description: Automates an API test case by analyzing the framework, generating REST Assured test code, and submitting a PR
args: test-case-id
triggers:
  - "automate api test"
  - "generate api test"
  - "implement api test case"
---

# API Test Automation Command

Automates an API test case from test-cases/api/ by:
1. Reading the test case specification
2. Analyzing the existing REST Assured framework
3. Generating POJOs if needed
4. Generating test methods with proper assertions
5. Creating a PR

## Usage

```
/automate-api-test <test-case-id>
```

Example:
```
/automate-api-test API_PART_001
```

## Workflow

You are an expert SDET implementing API test automation using the existing REST Assured + Java + TestNG framework.

### Step 1 — Parse Arguments

Extract the test case ID from the args parameter. Format: `API_PART_XXX`

### Step 2 — Read Test Case

Read the test case from `test-cases/api/` directory:
- Look for files matching the test case ID
- Parse test case details:
  - Endpoint (e.g., POST /api/part/)
  - Request payload
  - Expected status code
  - Expected response fields
  - Validation rules
- If test case not found, show error and list available test cases

### Step 3 — Analyze Framework

Read and understand the existing framework:
- `automation/api/src/main/java/com/inventree/api/base/BaseApi.java` — Request spec builder
- `automation/api/src/main/java/com/inventree/api/models/*.java` — POJOs
- `automation/api/src/main/java/com/inventree/api/endpoints/*.java` — Endpoint constants
- `automation/api/src/main/java/com/inventree/api/utils/TestDataGenerator.java` — Test data helpers
- `automation/api/src/test/java/com/inventree/api/tests/*.java` — Test classes
- Understand the REST Assured pattern and coding conventions

### Step 4 — Create Git Branch

```bash
git checkout main
git pull origin main
git checkout -b automate-api-<test-case-id-lowercase>
```

### Step 5 — Launch API Analysis Agent

**OPTIONAL:** If the test case involves a new API endpoint or complex logic, use the Agent tool to spawn a specialized agent for API analysis.

The agent should:
- Call the actual InvenTree API endpoint (using curl or REST Assured)
- Analyze the response structure
- Identify all response fields and their types
- Document any nested objects or arrays
- Test error scenarios (400, 404, 401, etc.)
- Return the API behavior documentation

Example agent prompt:
```
Analyze the InvenTree API endpoint: POST /api/part/

1. Call the endpoint with valid data and capture response
2. Call with invalid data and capture error response
3. Document response structure (fields, types, required vs optional)
4. Test different authentication scenarios
5. Identify validation rules

Return:
- Sample request payload
- Sample success response
- Sample error responses
- Response field mapping
```

### Step 6 — Check/Create POJOs

Based on the test case and API response:
- Check if POJO already exists (e.g., Part.java, PartCategory.java)
- If POJO exists and has all needed fields, use it
- If POJO is missing fields, add them with proper Jackson annotations
- If POJO doesn't exist, create a new POJO:
  - Use `@JsonIgnoreProperties(ignoreUnknown = true)`
  - Use `@JsonInclude(JsonInclude.Include.NON_NULL)`
  - Add `@JsonProperty` for all fields
  - Implement Builder pattern
  - Add toString() method

Example POJO:
```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {
    @JsonProperty("pk")
    private Integer pk;

    @JsonProperty("name")
    private String name;

    // ... getters, setters, builder
}
```

### Step 7 — Add Endpoint Constant (if needed)

If the test case uses a new endpoint:
- Add the endpoint constant to appropriate file in `endpoints/` package
- Follow existing naming convention

Example:
```java
public static final String PART_ATTACHMENTS = "/part/{id}/attachments/";
```

### Step 8 — Update TestDataGenerator (if needed)

If the test case needs specific test data that doesn't exist:
- Add a new method to TestDataGenerator
- Use unique suffixes to avoid name collisions
- Follow existing pattern

Example:
```java
public static Part partWithAttachments() {
    return Part.builder()
        .name("Part with Attachments " + getUniqueSuffix())
        .description("Part for attachment testing")
        .category(1)
        .active(true)
        .build();
}
```

### Step 9 — Generate Test Method

Create a new test method in the appropriate test class (or create new test class if needed):
- Follow the pattern in existing tests (e.g., PartCrudTest.java)
- Use BaseApi for request spec (givenAdmin(), givenAllAccess(), etc.)
- Use POJOs for request/response
- Use Hamcrest matchers for assertions
- Use AssertJ for complex assertions
- Include proper TestNG and Allure annotations
- Follow the test case steps exactly

Example structure:
```java
@Test(groups = {"regression"}, priority = X)
@Story("Test Case API_PART_XXX")
@Description("Test case description from test case file")
@Severity(SeverityLevel.NORMAL)
public void testXXX() {
    logger.info("=== Test: API_PART_XXX ===");

    // Step 1: Prepare test data
    Part part = TestDataGenerator.validPart();

    // Step 2: Make API request
    Response response = BaseApi.givenAdmin()
        .body(part)
        .post(PartEndpoints.PARTS);

    // Log response
    logResponse(response);

    // Step 3: Verify response
    response.then()
        .statusCode(201)
        .body("name", equalTo(part.getName()))
        .body("pk", notNullValue());

    // Step 4: Additional validations
    Part createdPart = response.as(Part.class);
    assertThat(createdPart.getName()).isEqualTo(part.getName());

    // Step 5: Cleanup
    int partId = response.jsonPath().getInt("pk");
    createdPartIds.add(partId);

    logger.info("=== Test Passed ===");
}
```

### Step 10 — Add JSON Schema (if needed)

If the test case requires schema validation:
- Create a JSON schema file in `src/test/resources/schemas/`
- Follow JSON Schema Draft 7 format
- Add schema validation to test method:
  ```java
  response.then()
      .body(matchesJsonSchemaInClasspath("schemas/your-schema.json"));
  ```

### Step 11 — Verify Compilation

Run Maven compile to ensure no errors:
```bash
cd automation/api
mvn clean compile
```

If compilation fails, fix the errors before proceeding.

### Step 12 — Run the Test

Verify the test works:
```bash
cd automation/api
mvn test -Dtest=<TestClassName>#<testMethodName>
```

Check the test passes. If it fails, debug and fix.

### Step 13 — Commit Changes

```bash
git add automation/api/src/main/java/com/inventree/api/
git add automation/api/src/test/java/com/inventree/api/
git add automation/api/src/test/resources/
git commit -m "$(cat <<'EOF'
Automate API test case <test-case-id>

- Added/Updated POJO for API entity
- Added test method for <test-case-description>
- Test uses REST Assured with proper assertions

Test Case: <test-case-id>
Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
EOF
)"
```

### Step 14 — Create Pull Request

Use GitHub CLI to create PR:
```bash
gh pr create --title "Automate API Test: <test-case-id>" --body "$(cat <<'EOF'
## Summary
Implements automated test for API test case <test-case-id>

## Changes
- ✅ Added/Updated POJO: `<PojoName>.java`
- ✅ Added test method: `testXXX()`
- ✅ Verified compilation and test execution

## Test Case Details
**ID:** <test-case-id>
**Endpoint:** <HTTP_METHOD> <endpoint_path>
**Status Code:** <expected_status>

**Request:**
```json
<sample request payload>
```

**Expected Response:**
```json
<sample response>
```

## Framework
- **Library:** REST Assured 5.5.0
- **Test Framework:** TestNG
- **Assertions:** Hamcrest + AssertJ
- **Reporting:** Allure

## Testing
```bash
cd automation/api
mvn test -Dtest=<TestClassName>#<testMethodName>
```

## Validations Implemented
- ✅ Status code: <expected_status>
- ✅ Response body fields
- ✅ Response structure (JSON schema)
- ✅ Error handling (if negative test)

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

### Step 15 — Output Summary

Provide a summary to the user:
```
✅ API Test Case <test-case-id> Automated Successfully

📋 Test Case: <test-case-title>
🔗 Endpoint: <HTTP_METHOD> <endpoint>
🌿 Branch: automate-api-<test-case-id-lowercase>

📄 Files Modified/Created:
   - automation/api/src/main/java/com/inventree/api/models/<POJO>.java
   - automation/api/src/test/java/com/inventree/api/tests/<TestClass>.java

🔗 Pull Request: <PR URL>

🧪 Run the test locally:
   cd automation/api
   mvn test -Dtest=<TestClass>#<testMethod>

📊 View Allure Report:
   mvn allure:serve
```

## Important Notes

- **Always use POJOs** - Don't construct JSON strings manually
- **Use BaseApi helpers** - Don't create inline auth specs
- **Follow existing patterns** - Check similar tests in PartCrudTest.java
- **Proper cleanup** - Add created resource IDs to `createdPartIds` for auto-cleanup
- **Comprehensive assertions** - Verify status code AND response body fields
- **Test groups** - Use appropriate groups: smoke, regression, negative
- **Compile before committing** - `mvn clean compile` must pass
- **Run test before PR** - Verify test passes: `mvn test -Dtest=...`

## Error Handling

If test case file not found:
- List available test cases in test-cases/api/
- Ask user to verify the test case ID

If API call fails during testing:
- Check base URL in config.properties
- Verify demo server is accessible
- Check authentication credentials
- Review request payload format

If compilation fails:
- Show the compilation errors
- Fix syntax errors, imports, missing dependencies
- Do not create PR until compilation succeeds

If test fails:
- Review API response in logs
- Check if test data is valid
- Verify assertions match actual response
- Debug and fix before creating PR

## Example Usage

```bash
# Automate test case API_PART_001
/automate-api-test API_PART_001

# This will:
# 1. Read test-cases/api/part-api-tests.md (or similar)
# 2. Analyze existing Part.java POJO
# 3. Add test method to PartCrudTest.java
# 4. Verify compilation and test execution
# 5. Commit and create PR
```

## Test Case Format Expected

The test case file should have clear API details:

```markdown
## Test Case ID: API_PART_001

**Title:** Create Part via API

**Endpoint:** POST /api/part/

**Request Payload:**
```json
{
  "name": "Test Part",
  "description": "Description",
  "category": 1,
  "active": true
}
```

**Expected Status Code:** 201

**Expected Response Fields:**
- pk (integer, not null)
- name (string, matches request)
- description (string, matches request)
- category (integer, matches request)
- active (boolean, true)

**Validation Rules:**
- Name is required
- Category is required
```

The command will parse this format and generate appropriate test code.
