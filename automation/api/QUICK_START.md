# Quick Start Guide - API Automation

## Prerequisites Check

```bash
# Verify Java 17+
java -version

# Verify Maven
mvn -version
```

If not installed, see installation instructions in README.md

## First-Time Setup

### 1. Compile Project

```bash
cd automation/api
mvn clean compile
```

This downloads all dependencies (REST Assured, TestNG, Jackson, Allure, etc.)

### 2. Run Smoke Tests

```bash
mvn test -Dgroups=smoke
```

### 3. View Results

```bash
# Generate and open Allure report
mvn allure:serve

# Or check logs
cat target/logs/api-test-execution.log
```

## Common Commands

| Command | Description |
|---------|-------------|
| `mvn clean compile` | Compile code |
| `mvn clean test` | Run all tests |
| `mvn test -Dtest=PartCrudTest` | Run specific test class |
| `mvn test -Dgroups=smoke` | Run smoke tests |
| `mvn test -Dgroups=regression` | Run regression tests |
| `mvn test -Dgroups=negative` | Run negative tests |
| `mvn allure:report` | Generate Allure report |
| `mvn allure:serve` | Generate and open report |

## File Locations

| File | Purpose |
|------|---------|
| `config.properties` | Test configuration (URL, credentials) |
| `testng.xml` | Test suite configuration |
| `schemas/*.json` | JSON schemas for validation |
| `testdata/*.json` | Test data files |
| `target/logs/api-test-execution.log` | Test logs |
| `target/allure-results/` | Test results for reporting |

## Quick Test Examples

### Create a Part

```java
Part part = TestDataGenerator.validPart();

Response response = BaseApi.givenAdmin()
    .body(part)
    .post(PartEndpoints.PARTS);

response.then()
    .statusCode(201)
    .body("name", equalTo(part.getName()));
```

### Get Part by ID

```java
Response response = BaseApi.givenAdmin()
    .pathParam("id", 123)
    .get(PartEndpoints.PART_BY_ID);

Part part = response.as(Part.class);
```

### Update Part

```java
Part updateData = Part.builder()
    .description("Updated description")
    .build();

BaseApi.givenAdmin()
    .pathParam("id", 123)
    .body(updateData)
    .patch(PartEndpoints.PART_BY_ID)
    .then()
    .statusCode(200);
```

### Delete Part

```java
BaseApi.givenAdmin()
    .pathParam("id", 123)
    .delete(PartEndpoints.PART_BY_ID)
    .then()
    .statusCode(204);
```

## Configuration

Edit `src/test/resources/config.properties`:

```properties
# Change base URL for different environment
base.url=https://demo.inventree.org
api.base.path=/api

# Credentials already configured
admin.username=admin
admin.password=inventree
```

Or override at runtime:
```bash
mvn test -Dbase.url=https://staging.example.com
```

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

### Tests fail with connection error
- Verify network connectivity
- Check base URL in config.properties
- Ensure https://demo.inventree.org is accessible

### Authentication fails
- Verify credentials in config.properties
- Check demo server is up
- Try: `AuthHelper.clearTokenCache()`

## Test Organization

Tests are organized by groups:
- **smoke** - Critical functionality (fast)
- **regression** - Full coverage (comprehensive)
- **negative** - Error handling and validation

Run specific groups:
```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
mvn test -Dgroups=negative
```

## Sample Test Class

`PartCrudTest.java` includes 11 test scenarios:
1. ✅ Create part (happy path)
2. ✅ Create part with all fields
3. ✅ Get part by ID
4. ✅ Update part (PATCH)
5. ✅ Delete part
6. ✅ Missing required field validation
7. ✅ Missing category validation
8. ✅ Get non-existent part (404)
9. ✅ List parts with schema validation
10. ✅ Unauthorized access (401)
11. ✅ Parameterized test for part types

## Next Steps

1. ✅ Install Java 17 and Maven
2. ✅ Compile project
3. ✅ Run smoke tests
4. ✅ View Allure reports
5. ✅ Write additional test cases

## Need Help?

See full documentation in `README.md`
