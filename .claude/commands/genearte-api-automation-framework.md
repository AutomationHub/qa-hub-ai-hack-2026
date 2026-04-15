---
description: "Scaffold the API automation framework (REST Assured + Java + TestNG) in automation/api/. Creates Maven project, dependencies, base classes, POJOs, config, and test runner."
disable-model-invocation: true
allowed-tools: Write, Read, Glob, Grep, Bash, Agent
---

# API Automation Framework Setup

You are a Senior SDET setting up a **REST Assured + Java + TestNG** API test automation framework.

## Configuration

- **AUT details and credentials** are in `CLAUDE.md` (already in context).
- **Framework spec** (stack, project structure, dependencies, plugins, config, design decisions) is in `.claude/config/test-automation/api-framework.md` — **read this file first**.

## Workflow

### Step 1 — Read Config
Read `.claude/config/test-automation/api-framework.md` to load the full framework specification.

### Step 2 — Create Project Structure
Create the **exact directory and file structure** defined in the config under `automation/api/`. Create every directory and every file listed.

### Step 3 — Generate pom.xml
Create `automation/api/pom.xml` with:
- `groupId`: `com.inventree`
- `artifactId`: `inventree-api-tests`
- All dependencies from the config (exact versions specified)
- All plugins from the config
- Java 17 compiler settings
- UTF-8 encoding properties

### Step 4 — Generate Base Classes

**ApiConfig.java** — Configuration loader:
- Loads from `config.properties`
- System property overrides (`-Dbase.url=...` for CI)
- Typed getters: `getString()`, `getInt()`, `getBoolean()`
- Builds full base URI from `base.url` + `api.base.path`

**AuthHelper.java** — Authentication helper:
- `getBasicAuthSpec(String username, String password)` — returns RequestSpecification with Basic auth
- `getToken(String username, String password)` — calls InvenTree token endpoint, caches token
- `getTokenAuthSpec(String username, String password)` — returns RequestSpecification with Token auth
- Support for all four user roles from CLAUDE.md

**BaseApi.java** — Shared request specification builder:
- Builds `RequestSpecification` with: base URI, content type (JSON), Allure filter, logging
- Methods: `givenAuth(String user)` — returns spec with auth for given user role
- Methods: `givenNoAuth()` — returns spec without auth (for 401 tests)

**BaseApiTest.java** — Test lifecycle:
- `@BeforeSuite` — load config, setup REST Assured defaults (base URI, logging, relaxed HTTPS)
- `@BeforeMethod` — log test name
- `@AfterMethod` — log result
- Helper: `createTestPart()` — creates a part via API and returns its ID (for tests that need existing data)
- Helper: `deleteTestPart(int id)` — cleanup helper

### Step 5 — Generate POJOs

**Part.java** — Part model:
- Fields: name, description, category, IPN, keywords, active, virtual, template, assembly, component, trackable, purchaseable, saleable, units, minimum_stock, notes
- Jackson annotations (`@JsonProperty`, `@JsonIgnoreProperties(ignoreUnknown = true)`)
- Builder pattern or fluent setters
- `toString()` for logging

**PartCategory.java** — Category model:
- Fields: name, description, parent, pathstring, level
- Jackson annotations

**ErrorResponse.java** — Error model:
- Handles InvenTree error response format
- Jackson annotations

### Step 6 — Generate Endpoint Constants

**PartEndpoints.java**:
```java
public static final String PARTS = "/part/";
public static final String PART_BY_ID = "/part/{id}/";
public static final String PART_METADATA = "/part/{id}/metadata/";
```

**CategoryEndpoints.java**:
```java
public static final String CATEGORIES = "/part/category/";
public static final String CATEGORY_BY_ID = "/part/category/{id}/";
```

### Step 7 — Generate Utility Classes

**TestDataGenerator.java**:
- `validPart()` — returns Part with all required fields populated
- `validPartWithAllFields()` — returns Part with every field populated
- `invalidPartMissingName()` — returns Part without name
- `invalidPartMissingCategory()` — returns Part without category
- `partWithLongName(int length)` — for boundary tests
- Uses random suffixes to avoid name collisions on the demo server

**JsonSchemaHelper.java**:
- `getSchema(String schemaFileName)` — loads JSON schema from resources/schemas/

### Step 8 — Generate Config Files

- `config.properties` — from the config spec
- `testng.xml` — suite with test groups (smoke, regression, negative), parallel mode
- `logback-test.xml` — console + file appenders, INFO level, REST Assured request/response logging
- `schemas/part-schema.json` — JSON schema for Part response validation
- `schemas/part-list-schema.json` — JSON schema for Part list response
- `testdata/valid-parts.json` — 2-3 valid part payloads
- `testdata/invalid-parts.json` — 2-3 invalid payloads (missing fields, wrong types)

### Step 9 — Generate Sample Test Class

Create `PartCrudTest.java` with:
- `@Test` create part (POST) — positive
- `@Test` get part by ID (GET) — positive
- `@Test` update part (PATCH) — positive
- `@Test` delete part (DELETE) — positive
- `@Test` create part missing required field (POST) — negative
- Uses POJOs for request/response
- Status code assertions + body field assertions
- `@DataProvider` example for parameterised test
- `@AfterMethod` cleanup (delete created parts)

### Step 10 — Verify Build

Run `mvn compile -f automation/api/pom.xml` to verify the project compiles. Fix any compilation errors.

## Quality Criteria

- All files must compile without errors
- REST Assured specs must be reusable (not hardcoded per test)
- Auth helper must support all four user roles
- POJOs must correctly map to InvenTree API responses
- Config must support CI overrides via system properties
- Test data generator must produce unique names (avoid demo server collisions)
- JSON schemas should match the actual InvenTree API response structure
