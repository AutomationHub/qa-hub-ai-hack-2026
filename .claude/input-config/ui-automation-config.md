# UI Automation Framework Configuration

## Framework Stack

- **Language:** Java 17+
- **Build Tool:** Maven
- **Test Runner:** TestNG
- **Browser Automation:** Selenium WebDriver 4.x
- **Driver Management:** WebDriverManager (io.github.bonigarcia)
- **Design Pattern:** Page Object Model (POM)
- **Reporting:** Allure Reports
- **Logging:** SLF4J + Logback
- **Assertions:** TestNG assertions + AssertJ (fluent)
- **Data:** JSON test data files via Jackson

## Project Structure

```
automation/ui/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/java/com/inventree/ui/
│   │   ├── base/
│   │   │   └── BasePage.java              # Common POM methods (click, type, wait, etc.)
│   │   ├── pages/
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   ├── PartsListPage.java
│   │   │   ├── PartDetailPage.java
│   │   │   ├── CreatePartPage.java
│   │   │   └── PartCategoryPage.java
│   │   ├── config/
│   │   │   └── ConfigReader.java          # Reads config.properties
│   │   └── utils/
│   │       ├── DriverFactory.java         # WebDriver setup via WebDriverManager
│   │       ├── WaitUtils.java             # Explicit wait helpers
│   │       └── TestDataReader.java        # JSON test data loader
│   └── test/
│       ├── java/com/inventree/ui/tests/
│       │   ├── BaseTest.java              # @BeforeMethod/@AfterMethod — driver lifecycle
│       │   ├── PartCreationTest.java
│       │   ├── PartDetailViewTest.java
│       │   ├── PartCategoryTest.java
│       │   └── CrossFunctionalTest.java   # E2E: create part → add params → stock → verify
│       └── resources/
│           ├── config.properties
│           ├── testdata/
│           │   └── parts.json
│           └── logback-test.xml
```

## Maven Dependencies

```xml
<!-- Selenium -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.27.0</version>
</dependency>

<!-- WebDriverManager -->
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.9.2</version>
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

<!-- Jackson for JSON test data -->
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
browser=chrome
headless=true
implicit.wait=10
explicit.wait=15
admin.username=admin
admin.password=inventree
allaccess.username=allaccess
allaccess.password=nolimits
reader.username=reader
reader.password=readonly
```

## Key Design Decisions

- **DriverFactory** uses WebDriverManager — no manual driver downloads
- **BasePage** provides reusable methods: `click()`, `type()`, `waitForVisible()`, `getText()`, `isDisplayed()`
- **BaseTest** handles driver init in `@BeforeMethod`, quit in `@AfterMethod`, login in setup
- **ConfigReader** loads from `config.properties` with system property overrides (for CI)
- **headless=true** by default for CI compatibility
- All page classes extend `BasePage`
- Tests use `@Test` annotations with `groups` for categorisation (smoke, regression, etc.)
