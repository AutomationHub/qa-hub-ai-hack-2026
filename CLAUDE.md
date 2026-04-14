# QA Hub AI Hackathon 2026

## Project
QA test generation and automation for **InvenTree Parts module** — an open-source inventory management system (Python/Django).

## Application Under Test

- **Demo URL:** https://demo.inventree.org
- **API Base URL:** https://demo.inventree.org/api/
- **Demo resets daily.**

### Credentials

| Account | Password | Access Level |
|---------|----------|-------------|
| admin | inventree | Superuser — full access |
| allaccess | nolimits | Full CRUD |
| engineer | partsonly | Parts & stock only |
| reader | readonly | View-only |

## Documentation Sources

- **UI Requirements:** https://docs.inventree.org/en/stable/part/ (and sub-pages)
- **API Schema:** https://docs.inventree.org/en/stable/api/schema/part/

## Output Conventions

- UI test cases: `test-cases/ui/`
- API test cases: `test-cases/api/`
- UI Test Case ID format: `UI_PART_XXX`
- API Test Case ID format: `API_PART_XXX`
- Append to existing files (continue ID sequence) when they exist

## Automation Frameworks

### UI Automation
- **Location:** `automation/ui/`
- **Stack:** Selenium WebDriver 4.x + Java 17 + TestNG + Page Object Model
- **Build:** Maven
- **Reporting:** Allure Reports
- **Docs:** `automation/ui/README.md`

### API Automation
- **Location:** `automation/api/`
- **Stack:** REST Assured 5.5.0 + Java 17 + TestNG + POJOs
- **Build:** Maven
- **Reporting:** Allure Reports
- **Docs:** `automation/api/README.md`

## Automation Commands

### `/automate-ui-test <test-case-id>`
Automates a UI test case by:
1. Reading test case from `test-cases/ui/`
2. Using Playwright MCP to inspect elements and generate locators
3. Updating Page Objects with real locators
4. Generating test method following POM pattern
5. Creating a git branch and PR

**Example:** `/automate-ui-test UI_PART_001`

### `/automate-api-test <test-case-id>`
Automates an API test case by:
1. Reading test case from `test-cases/api/`
2. Analyzing framework and existing POJOs
3. Generating REST Assured test code with proper assertions
4. Creating a git branch and PR

**Example:** `/automate-api-test API_PART_001`
