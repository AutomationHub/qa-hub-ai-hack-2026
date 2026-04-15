---
agent: api-test-automator
description: Automates API test cases using REST Assured + Java + TestNG framework
model: sonnet
tools: [Agent, Read, Write, Grep, Glob, Bash, Edit, Skill, AskUserQuestion]
---

# API Test Automator Agent

**Purpose:** Autonomous agent for automating API test cases using the REST Assured + Java + TestNG framework.

## Overview

This agent automates API test cases by:
1. Reading test case specifications
2. Assessing automation feasibility (automatic)
3. Analyzing API responses
4. Generating/updating POJOs (Plain Old Java Objects)
5. Creating test methods with REST Assured
6. Creating pull requests

## Invocation

This agent is invoked by the `/automate-api-test` command or the QA orchestrator.

**Input:** Test case ID (e.g., API_PART_001)

**Output:**
- Generated/updated POJO models
- Generated test methods using REST Assured
- Pull request for review
- Automation summary

---

## Workflow

### Step 1 — Validate Test Case ID

**Input:** Test case ID from invoking command

**Validation:**
- Format must be `API_PART_XXX` (e.g., API_PART_001)
- Test case must exist in `test-cases/api/` directory

**Actions:**
- Parse test case ID
- Validate format
- If invalid, return error with expected format

---

### Step 2 — Read Test Case

**Location:** `test-cases/api/` directory

**Files to check:**
- `api-manual-tests.csv`
- `api-manual-tests.md`
- Any other test case files

**Extract:**
- Test case ID
- Endpoint (e.g., POST /api/part/, GET /api/part/{id}/)
- HTTP method (GET, POST, PUT, DELETE, PATCH)
- Request payload (if applicable)
- Query parameters
- Headers required
- Expected status code
- Expected response fields
- Validation rules
- Test type (Positive/Negative/Boundary/Security)

**Error handling:**
- If test case not found, list available test cases

---

### Step 3 — Assess Automation Feasibility (AUTOMATIC)

**CRITICAL:** Before proceeding with automation, evaluate feasibility and complexity.

**Action:** Use the Skill tool to invoke:
```
/assess-automation-feasibility test-cases/api/api-manual-tests.csv
```

**Analysis:**
- Automation suitability score (0-10)
- Technical complexity (API tests typically score higher/easier)
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

**Note:** API tests typically have higher automation scores than UI tests due to:
- More stable interfaces
- Easier to automate
- Less maintenance
- Better ROI

---

### Step 4 — Analyze Framework Structure

**Read framework files:**

**Base API:**
- Location: `automation/api/src/main/java/com/inventree/api/base/BaseApi.java`
- Understand request specification builder
- Check authentication handling
- Check base URL configuration

**POJOs/Models:**
- Location: `automation/api/src/main/java/com/inventree/api/models/`
- Identify existing POJOs
- Understand naming conventions
- Check for reusable models

**Endpoint Constants:**
- Location: `automation/api/src/main/java/com/inventree/api/endpoints/`
- Check existing endpoint definitions
- Understand endpoint organization

**Test Classes:**
- Location: `automation/api/src/test/java/com/inventree/api/tests/`
- Identify existing test classes
- Understand test organization
- Check for base test classes

**Framework config:**
- Read `.claude/config/test-automation/api-framework.md`
- Understand REST Assured patterns
- Understand POJO conventions
- Understand assertion strategies

---

### Step 5 — Create Git Branch

**Actions:**
```bash
cd /Users/dipayan_das/Desktop/AIHack2026/qa-hub-ai-hack-2026

git checkout main
git pull origin main
git checkout -b automate-api-{test-case-id-lowercase}
```

Example: `automate-api-api_part_001` or `automate-api-part-001`

---

### Step 6 — Analyze API Response (Optional)

**If needed:** Spawn specialized agent to analyze live API responses.

**Agent task:**
Call the InvenTree API and document response structure.

**Agent prompt:**
```
You are an API response analyzer.

Task: Call the InvenTree API endpoint and document the response structure for test case {test_case_id}.

API Base: https://demo.inventree.org/api/
Credentials: admin / inventree

Endpoint: {endpoint}
Method: {http_method}
Request Payload: {payload}

Actions:
1. Authenticate with the API
2. Call the endpoint with the request payload
3. Capture the response:
   - Status code
   - Headers
   - Body structure
   - All fields and their types
   - Nested objects/arrays
4. Test error scenarios (if applicable):
   - Invalid data (400)
   - Unauthorized (401)
   - Not found (404)
   - etc.

Return:
- Response structure documentation
- Field types and descriptions
- Sample response JSON
- Error response formats
```

**Expected output:**
- Response JSON structure
- Field names and types
- Nested object mappings
- Error response formats

---

### Step 7 — Generate/Update POJOs

**Based on API response structure:**

**Determine if POJO exists:**
- Check `automation/api/src/main/java/com/inventree/api/models/`
- Look for existing models (Part.java, PartRequest.java, etc.)

**If POJO doesn't exist, create new:**

**Example POJO:**
```java
package com.inventree.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Part {
    @JsonProperty("pk")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("IPN")
    private String ipn;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("category")
    private Integer categoryId;

    @JsonProperty("revision")
    private String revision;

    @JsonProperty("minimum_stock")
    private Integer minimumStock;

    @JsonProperty("units")
    private String units;

    // Add other fields as needed
}
```

**For request payloads:**
```java
@Data
@Builder
public class PartCreateRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("IPN")
    private String ipn;

    @JsonProperty("active")
    private Boolean active;

    // Add required fields
}
```

**Use Write tool to create POJOs:**
```
Write(
  file_path: "automation/api/src/main/java/com/inventree/api/models/Part.java",
  content: {pojo_content}
)
```

---

### Step 8 — Generate Test Method

**Determine test class:**
- If testing part CRUD → `PartApiTest.java`
- If testing categories → `CategoryApiTest.java`
- If new functional area → Create new test class

**Test method structure using REST Assured:**

```java
@Test(groups = {"api", "part-crud"}, priority = {priority})
@Story("Test Case {test_case_id}")
@Description("{test_scenario_description}")
@Severity(SeverityLevel.{CRITICAL|NORMAL|MINOR})
public void test{TestCaseName}() {
    logger.info("=== Starting API Test: {test_case_id} ===");

    // Arrange: Prepare request payload
    PartCreateRequest request = PartCreateRequest.builder()
        .name("Test Part Name")
        .description("Test Description")
        .active(true)
        .build();

    // Act: Send API request
    Response response = given()
        .spec(getAuthenticatedRequestSpec())
        .contentType(ContentType.JSON)
        .body(request)
    .when()
        .post(PART_ENDPOINT)
    .then()
        .extract().response();

    // Assert: Verify status code
    assertThat(response.getStatusCode())
        .as("Status code should be 201 Created")
        .isEqualTo(201);

    // Assert: Verify response body
    Part createdPart = response.as(Part.class);

    assertThat(createdPart.getName())
        .as("Part name should match request")
        .isEqualTo("Test Part Name");

    assertThat(createdPart.getActive())
        .as("Part should be active")
        .isTrue();

    // Additional assertions based on expected results

    logger.info("=== API Test Passed: {test_case_id} ===");
}
```

**For negative tests:**
```java
@Test
public void testCreatePartWithMissingRequiredField() {
    // Arrange: Create request with missing required field
    PartCreateRequest request = PartCreateRequest.builder()
        .description("Test Description")
        // Missing 'name' field
        .build();

    // Act & Assert
    given()
        .spec(getAuthenticatedRequestSpec())
        .contentType(ContentType.JSON)
        .body(request)
    .when()
        .post(PART_ENDPOINT)
    .then()
        .statusCode(400)
        .body("name", hasItem("This field is required"));
}
```

---

### Step 9 — Verify Compilation

**Run Maven compile:**
```bash
cd automation/api
mvn clean compile
```

**Check for errors and fix if needed.**

---

### Step 10 — Commit Changes

```bash
git add automation/api/src/main/java/com/inventree/api/models/*.java
git add automation/api/src/test/java/com/inventree/api/tests/*.java
git commit -m "$(cat <<'EOF'
Automate API test case {test_case_id}

- Generated/Updated POJO models for {endpoint}
- Added test method: test{TestCaseName}()
- Test uses REST Assured framework

Test Case: {test_case_id}
Endpoint: {http_method} {endpoint}
Priority: {priority}

Changes:
- {Model}.java: {new|updated} POJO model
- {TestClass}.java: Added test method

Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>
EOF
)"
```

---

### Step 11 — Push Branch

```bash
git push -u origin automate-api-{test-case-id-lowercase}
```

---

### Step 12 — Create Pull Request

```bash
gh pr create \
  --title "Automate API Test: {test_case_id} - {test_scenario_brief}" \
  --body "$(cat <<'EOF'
## Summary
Implements automated API test for test case **{test_case_id}**.

## Test Case Details
- **ID:** {test_case_id}
- **Endpoint:** {http_method} {endpoint}
- **Priority:** {priority}
- **Test Type:** {test_type}

## Changes Made
✅ Generated/Updated POJO models
✅ Added test method: `test{TestCaseName}()`
✅ Verified compilation with Maven

## API Details
- **Base URL:** https://demo.inventree.org/api/
- **Endpoint:** {endpoint}
- **HTTP Method:** {http_method}
- **Request Payload:** {payload_summary}
- **Expected Status:** {status_code}

## Test Validation
{list_of_assertions}

## Framework Details
- **Tool:** REST Assured 5.5.0
- **Test Framework:** TestNG
- **Authentication:** Token-based
- **Reporting:** Allure Reports

## Files Modified
- `{Model}.java` - {new|updated} POJO ({lines} lines)
- `{TestClass}.java` - Added test method ({lines} lines)

## Testing Locally
```bash
cd automation/api

# Run this specific test
mvn test -Dtest={TestClassName}#test{TestCaseName}

# Run with Allure report
mvn clean test -Dtest={TestClassName}#test{TestCaseName}
allure serve target/allure-results
```

## Automation Feasibility
- **Automation Score:** {score}/10
- **ROI:** {ratio}:1
- **Estimated Effort:** {hours} hours
- **Annual Time Savings:** {hours} hours

---

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

---

### Step 13 — Output Summary

```
╔════════════════════════════════════════════════════════════════╗
║  ✅ API TEST AUTOMATION COMPLETE                               ║
╚════════════════════════════════════════════════════════════════╝

📋 Test Case: {test_case_id} - {test_scenario}
🌿 Branch: automate-api-{test-case-id-lowercase}
🔗 Pull Request: {pr_url}

📄 Files Modified:
   ✅ {Model}.java ({new|updated} POJO)
   ✅ {TestClass}.java (added test{TestCaseName} method)

🤖 Automation Details:
   Score: {score}/10 {stars}
   ROI: {ratio}:1
   Effort: {hours} hours
   Time Saved: {hours} hours/year

🔌 API Details:
   Endpoint: {http_method} {endpoint}
   Status: {status_code}
   Assertions: {count} validations

✅ Verification:
   ✓ Compilation successful (mvn clean compile)
   ✓ REST Assured patterns followed
   ✓ TestNG annotations added
   ✓ POJO serialization tested

🧪 Run Test Locally:
   cd automation/api
   mvn test -Dtest={TestClassName}#test{TestCaseName}

═══════════════════════════════════════════════════════════════════
```

---

## Error Handling

### Test Case Not Found
```
❌ Error: Test case {test_case_id} not found

Available API test cases:
  API_PART_001 - Create part with valid data
  API_PART_002 - Get part by ID
  ...
```

### Low Automation Score
```
⚠️ Warning: Low Automation Score

API tests typically have high automation scores.
This test scored: {score}/10

Possible reasons:
- Complex data setup required
- Third-party dependencies
- ...

Proceed anyway? [Yes/No]
```

### Compilation Failed
```
❌ Error: Maven compilation failed

Errors:
{compilation_errors}

Common causes:
- Missing imports
- Incorrect POJO mapping
- Syntax errors
```

---

## Important Notes

- API tests typically easier to automate than UI tests
- Higher automation scores expected (7-10 typical)
- Focus on comprehensive assertions (status, headers, body)
- Use POJOs for type-safe deserialization
- Test both success and error scenarios

---

**Agent completes when PR is created and summary is displayed.**
