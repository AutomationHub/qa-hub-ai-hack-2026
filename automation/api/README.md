# InvenTree API Automation Framework

REST Assured + Java + TestNG test automation framework for InvenTree API testing.

## Prerequisites

- **Java 17+** - [Download](https://adoptium.net/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)

## Framework Stack

- **Language:** Java 17
- **Build Tool:** Maven
- **Test Runner:** TestNG
- **API Testing:** REST Assured 5.5.0
- **JSON Schema Validation:** REST Assured JSON Schema Validator
- **Serialization:** Jackson (POJO mapping)
- **Reporting:** Allure Reports
- **Logging:** SLF4J + Logback
- **Assertions:** TestNG + Hamcrest + AssertJ

## Project Structure

```
automation/api/
├── pom.xml                                # Maven project configuration
├── testng.xml                             # TestNG suite configuration
├── src/
│   ├── main/java/com/inventree/api/
│   │   ├── base/
│   │   │   └── BaseApi.java              # REST Assured spec builder
│   │   ├── config/
│   │   │   └── ApiConfig.java            # Configuration loader
│   │   ├── models/                       # POJOs
│   │   │   ├── Part.java
│   │   │   ├── PartCategory.java
│   │   │   └── ErrorResponse.java
│   │   ├── endpoints/                    # Endpoint constants
│   │   │   ├── PartEndpoints.java
│   │   │   └── CategoryEndpoints.java
│   │   └── utils/
│   │       ├── AuthHelper.java           # Authentication helper
│   │       ├── TestDataGenerator.java    # Test data builder
│   │       └── JsonSchemaHelper.java     # Schema file loader
│   └── test/
│       ├── java/com/inventree/api/tests/
│       │   ├── BaseApiTest.java          # Test lifecycle management
│       │   └── PartCrudTest.java         # Sample CRUD tests
│       └── resources/
│           ├── config.properties         # Test configuration
│           ├── schemas/                  # JSON schemas
│           │   ├── part-schema.json
│           │   └── part-list-schema.json
│           ├── testdata/                 # Test data
│           │   ├── valid-parts.json
│           │   └── invalid-parts.json
│           └── logback-test.xml          # Logging configuration
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
cd automation/api
mvn clean compile
```

This will:
- Download all dependencies (REST Assured, TestNG, Jackson, etc.)
- Compile the code
- Validate the project structure

## Configuration

Edit `src/test/resources/config.properties` to customize:

```properties
# Base URL and API path
base.url=https://demo.inventree.org
api.base.path=/api

# User credentials (already configured for InvenTree demo)
admin.username=admin
admin.password=inventree
allaccess.username=allaccess
allaccess.password=nolimits
engineer.username=engineer
engineer.password=partsonly
reader.username=reader
reader.password=readonly

# Logging
log.request=true
log.response=true
```

### CI/CD Overrides

Override properties via system properties:
```bash
mvn test -Dbase.url=https://staging.example.com
```

## Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Class

```bash
mvn test -Dtest=PartCrudTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=PartCrudTest#testCreatePart
```

### Run by TestNG Groups

```bash
# Smoke tests only
mvn test -Dgroups=smoke

# Regression tests
mvn test -Dgroups=regression

# Negative tests
mvn test -Dgroups=negative
```

### Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

## Test Data

### Using POJOs

```java
// Create part using builder pattern
Part newPart = Part.builder()
    .name("Test Part")
    .description("Description")
    .category(1)
    .active(true)
    .build();

// Send request
Response response = BaseApi.givenAdmin()
    .body(newPart)
    .post(PartEndpoints.PARTS);

// Deserialize response
Part createdPart = response.as(Part.class);
```

### Using Test Data Generator

```java
// Valid part with required fields
Part part = TestDataGenerator.validPart();

// Valid part with all fields
Part completePart = TestDataGenerator.validPartWithAllFields();

// Invalid parts for negative testing
Part invalidPart = TestDataGenerator.invalidPartMissingName();
```

### Using JSON Files

Test data is available in `src/test/resources/testdata/`:
- `valid-parts.json` - Valid part payloads
- `invalid-parts.json` - Invalid payloads for negative tests

## Authentication

The framework supports multiple authentication methods:

### Token Authentication (Default)

```java
Response response = BaseApi.givenAdmin()
    .get(PartEndpoints.PARTS);
```

Token is automatically fetched and cached.

### Basic Authentication

```java
Response response = BaseApi.givenBasicAuth("admin")
    .get(PartEndpoints.PARTS);
```

### User Roles

```java
// Admin (full access)
BaseApi.givenAdmin()

// AllAccess (full CRUD)
BaseApi.givenAllAccess()

// Engineer (parts/stock only)
BaseApi.givenEngineer()

// Reader (view-only)
BaseApi.givenReader()

// No authentication (for 401 tests)
BaseApi.givenNoAuth()
```

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

Reports include:
- Request/response details (auto-captured by Allure REST Assured filter)
- Test execution timeline
- Test categorization by Epic/Feature/Story
- Failure screenshots and logs

## JSON Schema Validation

Validate response structure using JSON schemas:

```java
Response response = BaseApi.givenAdmin()
    .get(PartEndpoints.PARTS);

// Validate against schema
response.then()
    .body(matchesJsonSchemaInClasspath("schemas/part-list-schema.json"));

// Or using helper
response.then()
    .body(matchesJsonSchema(JsonSchemaHelper.getPartListSchema()));
```

Schemas are located in `src/test/resources/schemas/`.

## Writing New Tests

### 1. Extend BaseApiTest

```java
public class MyApiTest extends BaseApiTest {

    @Test(groups = {"smoke"})
    public void testSomething() {
        // Test logic here
    }
}
```

### 2. Use REST Assured Pattern

```java
@Test
public void testCreatePart() {
    // Prepare data
    Part part = TestDataGenerator.validPart();

    // Send request and verify response
    Response response = BaseApi.givenAdmin()
        .body(part)
        .post(PartEndpoints.PARTS);

    // Assertions
    response.then()
        .statusCode(201)
        .body("name", equalTo(part.getName()))
        .body("pk", notNullValue());

    // Store ID for cleanup
    int partId = response.jsonPath().getInt("pk");
    createdPartIds.add(partId);
}
```

### 3. Use Hamcrest Matchers

```java
response.then()
    .body("name", equalTo("Expected Name"))
    .body("count", greaterThan(0))
    .body("results", hasSize(5))
    .body("active", is(true))
    .body("description", containsString("test"));
```

### 4. Use AssertJ for Complex Assertions

```java
Part part = response.as(Part.class);

assertThat(part.getName()).isEqualTo("Expected Name");
assertThat(part.getActive()).isTrue();
assertThat(part.getCategory()).isGreaterThan(0);
```

### 5. Use DataProvider for Parameterized Tests

```java
@DataProvider(name = "testData")
public Object[][] dataProvider() {
    return new Object[][]{
        {TestDataGenerator.validPart(), "Valid Part"},
        {TestDataGenerator.templatePart(), "Template Part"}
    };
}

@Test(dataProvider = "testData")
public void testWithData(Part part, String description) {
    // Test logic using part
}
```

## Logging

Logs are written to:
- **Console:** Real-time output during test execution
- **File:** `target/logs/api-test-execution.log`

Log levels:
- `com.inventree` package: DEBUG
- REST Assured: DEBUG
- Root: INFO

Configure in `src/test/resources/logback-test.xml`

## Parallel Execution

Configured in `testng.xml`:
```xml
<suite name="Test Suite" parallel="methods" thread-count="3">
```

Supported parallel modes:
- `methods` - Run test methods in parallel
- `classes` - Run test classes in parallel
- `tests` - Run test tags in parallel

Adjust `thread-count` based on system resources and API rate limits.

## Troubleshooting

### Issue: Tests fail with connection timeout

**Solution:**
1. Verify base URL is correct in `config.properties`
2. Check network connectivity to InvenTree demo server
3. Increase timeout in `BaseApiTest.setupSuite()`:
```java
.setParam("http.connection.timeout", 30000)
.setParam("http.socket.timeout", 30000)
```

### Issue: Authentication fails

**Solution:**
1. Verify credentials in `config.properties`
2. Check if demo server is accessible
3. Clear token cache: `AuthHelper.clearTokenCache()`
4. Try Basic auth instead: `BaseApi.givenBasicAuth("admin")`

### Issue: JSON schema validation fails

**Solution:**
1. Update schema file to match actual API response
2. Use `@JsonIgnoreProperties(ignoreUnknown = true)` in POJOs
3. Check API documentation for current response structure

### Issue: Tests pass locally but fail in CI

**Solution:**
1. Use system property overrides: `-Dbase.url=...`
2. Ensure CI has network access to test environment
3. Check for parallel execution conflicts
4. Review test data - avoid hardcoded IDs

## Best Practices

✅ **Use POJOs** - Serialize/deserialize with Jackson, not manual JSON strings
✅ **Cleanup data** - Use `@AfterMethod` to delete created resources
✅ **Unique names** - Use `TestDataGenerator` to avoid name collisions
✅ **Reusable specs** - Use `BaseApi` helpers, not inline auth
✅ **Validate structure** - Use JSON schema validation
✅ **Group tests** - Use `@Test(groups = {"smoke", "regression"})`
✅ **Log strategically** - Debug level for details, Info for milestones
✅ **Handle errors** - Test negative scenarios and error responses
✅ **Parameterize** - Use `@DataProvider` for multiple test cases

## Application Under Test

- **Demo URL:** https://demo.inventree.org
- **API Base URL:** https://demo.inventree.org/api/
- **API Docs:** https://docs.inventree.org/en/stable/api/schema/part/
- **Demo resets daily** - Data created during tests is not persistent

### Test Credentials

| Account | Password | Access Level |
|---------|----------|-------------|
| admin | inventree | Superuser |
| allaccess | nolimits | Full CRUD |
| engineer | partsonly | Parts/stock only |
| reader | readonly | View-only |

## Framework Features

✅ **Token caching** - Avoid repeated auth calls
✅ **Auto cleanup** - Created resources deleted after tests
✅ **POJO mapping** - Type-safe request/response handling
✅ **JSON schema validation** - Verify response structure
✅ **Multiple auth methods** - Token, Basic auth support
✅ **Multiple user roles** - Test with different permissions
✅ **Allure integration** - Request/response auto-captured
✅ **Parallel execution** - Run tests concurrently
✅ **CI-ready** - System property overrides
✅ **Test data generator** - Unique test data on demand

## Sample Test Scenarios

The `PartCrudTest` class includes:
- ✅ Create part (POST)
- ✅ Create part with all fields
- ✅ Get part by ID (GET)
- ✅ Update part (PATCH)
- ✅ Delete part (DELETE)
- ✅ Create part missing required field (validation)
- ✅ Create part missing category (validation)
- ✅ Get non-existent part (404)
- ✅ List all parts with schema validation
- ✅ Unauthorized access (401)
- ✅ Create different part types (parameterized)

## Next Steps

1. ✅ Install Java 17 and Maven
2. ✅ Compile project: `mvn clean compile`
3. ✅ Run smoke tests: `mvn test -Dgroups=smoke`
4. ✅ View Allure reports: `mvn allure:serve`
5. ✅ Write additional test cases:
   - Part categories
   - Part filtering
   - Part parameters
   - Part stock
   - BOM operations
   - Permission tests

## Contact

For issues or questions about the framework, refer to project documentation or contact the QA team.
