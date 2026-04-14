# API Automation Framework Configuration

## Framework Stack

- **Language:** Java 17+
- **Build Tool:** Maven
- **Test Runner:** TestNG
- **API Testing:** REST Assured 5.x
- **JSON Schema Validation:** REST Assured JSON Schema Validator
- **Serialisation:** Jackson (POJO mapping)
- **Reporting:** Allure Reports
- **Logging:** SLF4J + Logback
- **Assertions:** TestNG + Hamcrest (REST Assured built-in) + AssertJ
- **Data-Driven:** TestNG @DataProvider + JSON files

## Project Structure

```
automation/api/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/java/com/inventree/api/
│   │   ├── base/
│   │   │   └── BaseApi.java               # REST Assured RequestSpecification builder
│   │   ├── config/
│   │   │   └── ApiConfig.java             # Reads config.properties, builds base specs
│   │   ├── models/
│   │   │   ├── Part.java                  # POJO for Part
│   │   │   ├── PartCategory.java          # POJO for PartCategory
│   │   │   └── ErrorResponse.java         # POJO for error responses
│   │   ├── endpoints/
│   │   │   ├── PartEndpoints.java         # Static endpoint path constants
│   │   │   └── CategoryEndpoints.java
│   │   └── utils/
│   │       ├── AuthHelper.java            # Token/Basic auth helper
│   │       ├── TestDataGenerator.java     # Builder for test payloads
│   │       └── JsonSchemaHelper.java      # Schema file loader
│   └── test/
│       ├── java/com/inventree/api/tests/
│       │   ├── BaseApiTest.java           # @BeforeSuite — setup specs, auth tokens
│       │   ├── PartCrudTest.java
│       │   ├── PartCategoryCrudTest.java
│       │   ├── PartFilteringTest.java
│       │   ├── PartValidationTest.java
│       │   ├── PartRelationalTest.java
│       │   ├── AuthPermissionTest.java
│       │   └── EdgeCaseTest.java
│       └── resources/
│           ├── config.properties
│           ├── schemas/
│           │   ├── part-schema.json
│           │   └── part-list-schema.json
│           ├── testdata/
│           │   ├── valid-parts.json
│           │   └── invalid-parts.json
│           └── logback-test.xml
```

## Maven Dependencies

```xml
<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>

<!-- REST Assured JSON Schema Validator -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>

<!-- TestNG -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.10.2</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.26.3</version>
    <scope>test</scope>
</dependency>

<!-- Allure TestNG -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>2.29.0</version>
    <scope>test</scope>
</dependency>

<!-- Allure REST Assured -->
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-rest-assured</artifactId>
    <version>2.29.0</version>
    <scope>test</scope>
</dependency>

<!-- Jackson for POJO mapping -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.18.2</version>
</dependency>

<!-- SLF4J + Logback -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.12</version>
</dependency>
```

## Maven Plugins

```xml
<!-- Surefire for TestNG -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.2</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>

<!-- Allure Maven -->
<plugin>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-maven</artifactId>
    <version>2.14.0</version>
</plugin>

<!-- Compiler -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
    </configuration>
</plugin>
```

## config.properties

```properties
base.url=https://demo.inventree.org
api.base.path=/api
admin.username=admin
admin.password=inventree
allaccess.username=allaccess
allaccess.password=nolimits
engineer.username=engineer
engineer.password=partsonly
reader.username=reader
reader.password=readonly
log.request=true
log.response=true
```

## Key Design Decisions

- **BaseApi** builds a shared `RequestSpecification` with base URI, content type, auth, and Allure filter
- **ApiConfig** reads `config.properties` with system property overrides (for CI: `-Dbase.url=...`)
- **AuthHelper** supports both Basic auth and Token auth; caches tokens per user
- **POJOs** use Jackson annotations; used for both request serialisation and response deserialisation
- **TestDataGenerator** provides builder methods for valid/invalid payloads (avoids hardcoding in tests)
- **@DataProvider** used for parameterised tests (e.g., multiple invalid field combinations)
- **JSON schema files** in `resources/schemas/` for response structure validation
- **Allure REST Assured filter** auto-captures request/response in reports
- Tests use `groups` (smoke, regression, negative) for selective execution
