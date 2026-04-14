# Project Setup Guide

Complete setup guide for the QA Hub AI Hackathon 2026 test automation project.

## Project Structure

```
qa-hub-ai-hack-2026/
├── .claude/
│   ├── commands/                      # Automation command skills
│   │   ├── automate-ui-test.md
│   │   └── automate-api-test.md
│   ├── config/                        # Configuration and guides
│   │   ├── AUTOMATION_COMMANDS.md     # Commands documentation
│   │   ├── PLAYWRIGHT_MCP_SETUP.md    # Playwright setup guide
│   │   └── SETUP_GUIDE.md            # This file
│   ├── input-config/                  # Framework configurations
│   │   ├── ui-automation-config.md
│   │   └── api-automation-config.md
│   └── settings.local.json            # Project-specific settings
├── automation/
│   ├── ui/                            # Selenium UI framework
│   └── api/                           # REST Assured API framework
├── test-cases/
│   ├── ui/                            # UI test case specifications
│   └── api/                           # API test case specifications
├── CLAUDE.md                          # Project instructions for Claude
└── AUTOMATION_SETUP_SUMMARY.md        # Complete framework summary
```

## Initial Setup

### 1. Prerequisites

```bash
# Java 17+
java -version  # Should show 17 or higher

# Maven
mvn -version  # Should show 3.6+

# Node.js (for Playwright MCP)
node --version  # Should show 18+

# GitHub CLI
gh --version  # Should show 2.0+
gh auth status  # Should be authenticated
```

If any are missing:
```bash
# MacOS
brew install openjdk@17 maven node gh

# Authenticate GitHub CLI
gh auth login
```

### 2. Compile Frameworks

```bash
# UI Framework
cd automation/ui
mvn clean compile

# API Framework
cd automation/api
mvn clean compile
```

### 3. Enable Playwright MCP (Critical for UI Automation)

**Option A: Via Settings UI**
1. Open Claude Code settings: `/settings` or `Cmd+,`
2. Go to **MCP Servers** tab
3. Click **Add Server**
4. Select **Playwright**
5. Click **Enable**
6. Restart Claude Code

**Option B: Manual Configuration**

Edit `.claude/settings.local.json`:
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  },
  "permissions": {
    "allow": [
      // ... existing permissions
    ]
  }
}
```

**Verify:**
```bash
# Test in Claude Code conversation
"Use playwright to navigate to https://example.com and take a screenshot"
```

See `.claude/config/PLAYWRIGHT_MCP_SETUP.md` for detailed instructions.

## Using Automation Commands

### `/automate-ui-test <test-case-id>`

**Automates UI test cases:**
1. Reads test case from `test-cases/ui/`
2. Uses Playwright MCP to inspect elements
3. Updates Page Objects with real locators
4. Generates test method
5. Creates PR

**Example:**
```bash
# Create test case in test-cases/ui/part-tests.md
# Then run:
/automate-ui-test UI_PART_001
```

**Output:**
```
✅ UI Test Case UI_PART_001 Automated Successfully
🌿 Branch: automate-ui-ui_part_001
🔗 Pull Request: https://github.com/user/repo/pull/1
```

### `/automate-api-test <test-case-id>`

**Automates API test cases:**
1. Reads test case from `test-cases/api/`
2. Creates/updates POJOs
3. Generates REST Assured test
4. Verifies test passes
5. Creates PR

**Example:**
```bash
# Create test case in test-cases/api/part-tests.md
# Then run:
/automate-api-test API_PART_001
```

**Output:**
```
✅ API Test Case API_PART_001 Automated Successfully
🌿 Branch: automate-api-api_part_001
🔗 Pull Request: https://github.com/user/repo/pull/2
```

## Test Case Format

### UI Test Case Template

Create in `test-cases/ui/`:

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

### API Test Case Template

Create in `test-cases/api/`:

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
  "active": true
}
```

**Validations:**
- Response contains pk (not null)
- Name matches request
- Description matches request
```

## Running Tests

### UI Tests
```bash
cd automation/ui

# Run all tests
mvn clean test

# Run smoke tests
mvn test -Dgroups=smoke -Dheadless=true

# Run specific test
mvn test -Dtest=PartCreationTest#testCreatePart -Dheadless=false

# Generate Allure report
mvn allure:serve
```

### API Tests
```bash
cd automation/api

# Run all tests
mvn clean test

# Run smoke tests
mvn test -Dgroups=smoke

# Run specific test
mvn test -Dtest=PartCrudTest#testCreatePart

# Generate Allure report
mvn allure:serve
```

## Configuration

### UI Framework Configuration
Edit `automation/ui/src/test/resources/config.properties`:
```properties
base.url=https://demo.inventree.org
browser=chrome
headless=true
admin.username=admin
admin.password=inventree
```

### API Framework Configuration
Edit `automation/api/src/test/resources/config.properties`:
```properties
base.url=https://demo.inventree.org
api.base.path=/api
admin.username=admin
admin.password=inventree
```

### CI/CD Overrides
```bash
# Override at runtime
mvn test -Dbase.url=https://staging.inventree.org -Dheadless=true
```

## Workflow

### Standard Test Automation Workflow

1. **Write Test Case**
   ```bash
   # Create test-cases/ui/feature-tests.md or test-cases/api/feature-tests.md
   ```

2. **Run Automation Command**
   ```bash
   /automate-ui-test UI_PART_001
   # or
   /automate-api-test API_PART_001
   ```

3. **Review PR**
   - Check generated code
   - Verify test logic
   - Ensure assertions are correct

4. **Merge PR**
   ```bash
   gh pr merge <PR-number> --squash
   ```

5. **Run in CI**
   - Tests automatically run in CI/CD pipeline

## Troubleshooting

### Playwright MCP Issues

**Problem:** "Playwright tools not available"

**Solution:**
```bash
# Install Playwright browsers
npx playwright install

# Test Playwright
npx @playwright/mcp@latest

# Enable in settings (see step 3 above)
```

See `.claude/config/PLAYWRIGHT_MCP_SETUP.md` for detailed troubleshooting.

### Maven Compilation Errors

**Problem:** "mvn: command not found"

**Solution:**
```bash
# MacOS
brew install maven

# Verify
mvn -version
```

**Problem:** "Java version mismatch"

**Solution:**
```bash
# Check version
java -version

# Install Java 17
brew install openjdk@17

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### Git/GitHub Issues

**Problem:** "gh: command not found"

**Solution:**
```bash
# Install GitHub CLI
brew install gh

# Authenticate
gh auth login
```

**Problem:** "Permission denied (publickey)"

**Solution:**
```bash
# Check SSH keys
ssh -T git@github.com

# Generate new key if needed
ssh-keygen -t ed25519 -C "your_email@example.com"
gh ssh-key add ~/.ssh/id_ed25519.pub
```

### Test Execution Issues

**Problem:** UI tests fail with "element not found"

**Solution:**
- Update locators in Page Objects
- Use Playwright MCP to find correct selectors
- Check if demo server is accessible

**Problem:** API tests fail with 401

**Solution:**
- Verify credentials in config.properties
- Check token generation
- Ensure demo server is up

## Directory Structure Details

### `.claude/commands/`
Contains automation command skill definitions:
- `automate-ui-test.md` - UI automation workflow
- `automate-api-test.md` - API automation workflow

### `.claude/config/`
Contains configuration and documentation:
- `AUTOMATION_COMMANDS.md` - Detailed command documentation
- `PLAYWRIGHT_MCP_SETUP.md` - Playwright setup guide
- `SETUP_GUIDE.md` - This file

### `.claude/input-config/`
Contains framework specifications used during generation:
- `ui-automation-config.md` - UI framework spec
- `api-automation-config.md` - API framework spec

### `automation/ui/`
Selenium + Java + TestNG + POM framework:
- `src/main/java/` - Page Objects, utilities, base classes
- `src/test/java/` - Test classes
- `src/test/resources/` - Configuration, test data
- `pom.xml` - Maven configuration
- `testng.xml` - TestNG suite
- `README.md` - Framework documentation

### `automation/api/`
REST Assured + Java + TestNG framework:
- `src/main/java/` - POJOs, utilities, base classes
- `src/test/java/` - Test classes
- `src/test/resources/` - Configuration, schemas, test data
- `pom.xml` - Maven configuration
- `testng.xml` - TestNG suite
- `README.md` - Framework documentation

### `test-cases/`
Test case specifications in Markdown:
- `ui/` - UI test cases (format: UI_PART_XXX)
- `api/` - API test cases (format: API_PART_XXX)

## Quick Reference

### Commands
| Command | Purpose |
|---------|---------|
| `/automate-ui-test <id>` | Automate UI test case |
| `/automate-api-test <id>` | Automate API test case |
| `/settings` | Open Claude Code settings |

### Test Execution
| Framework | Command |
|-----------|---------|
| UI (all) | `cd automation/ui && mvn test` |
| UI (smoke) | `mvn test -Dgroups=smoke` |
| API (all) | `cd automation/api && mvn test` |
| API (smoke) | `mvn test -Dgroups=smoke` |

### Documentation
| Document | Location |
|----------|----------|
| Command docs | `.claude/config/AUTOMATION_COMMANDS.md` |
| Playwright setup | `.claude/config/PLAYWRIGHT_MCP_SETUP.md` |
| UI framework | `automation/ui/README.md` |
| API framework | `automation/api/README.md` |
| Project overview | `AUTOMATION_SETUP_SUMMARY.md` |

## Next Steps

1. ✅ Complete initial setup (steps 1-3)
2. ✅ Write first test case
3. ✅ Run automation command
4. ✅ Review and merge PR
5. ✅ Set up CI/CD pipeline
6. ✅ Write more test cases

## Support

- **Framework issues:** See framework READMEs
- **Playwright MCP:** See PLAYWRIGHT_MCP_SETUP.md
- **Commands:** See AUTOMATION_COMMANDS.md
- **General questions:** Ask Claude directly

---

**Created:** 2026-04-14
**Project:** QA Hub AI Hackathon 2026
**Application Under Test:** InvenTree Parts Module
