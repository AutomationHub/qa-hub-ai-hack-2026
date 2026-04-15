# Configuration Directory - QA Hub AI Hackathon 2026

## Overview

This directory contains all configuration files for the QA test generation and automation workflow. Organized by purpose for easy navigation and maintenance.

---

## Directory Structure

```
config/
├── README.md                           ← You are here
├── test-generation/                    ← Test case generation configurations
│   ├── ui-functional-areas.md          ← UI functional area definitions
│   └── api-functional-areas.md         ← API functional area definitions
├── test-automation/                    ← Automation framework configurations
│   ├── ui-framework.md                 ← Selenium + POM framework config
│   └── api-framework.md                ← REST Assured framework config
└── setup/                              ← Setup guides and references
    ├── setup-guide.md                  ← Complete setup instructions
    ├── playwright-mcp.md               ← Playwright MCP configuration
    └── automation-reference.md         ← Automation commands reference
```

---

## Configuration Files

### Test Generation Configs

#### `test-generation/ui-functional-areas.md`
**Purpose:** Defines UI functional areas for test case generation

**Contains:**
- Functional area names and descriptions
- Minimum test count requirements
- Documentation URLs for requirements
- Menu text for user selection

**Used by:**
- `/generate-ui-test` command
- UI test generation workflow

**Example:**
```markdown
### 1. part-creation
- Description: Part creation — manual entry, required vs optional fields
- Min Tests: 12
- Docs:
  - https://docs.inventree.org/en/stable/part/create/
  - https://docs.inventree.org/en/stable/part/part/
```

---

#### `test-generation/api-functional-areas.md`
**Purpose:** Defines API functional areas for test case generation

**Contains:**
- API endpoint groupings
- Minimum test count requirements
- Key coverage areas (CRUD operations, validation, auth)
- API schema documentation URLs

**Used by:**
- `/generate-api-test` command
- API test generation workflow

**Example:**
```markdown
### 1. part-crud
- Description: Create, Read, Update, Delete operations on /api/part/
- Min Tests: 15
- Key Coverage: Create validation, field constraints, permissions
- Docs:
  - https://docs.inventree.org/en/stable/api/schema/part/
```

---

### Automation Framework Configs

#### `test-automation/ui-framework.md`
**Purpose:** Configuration for Selenium + Java + POM automation framework

**Contains:**
- Framework structure and patterns
- Page Object Model conventions
- Selenium WebDriver configuration
- TestNG test organization
- Allure reporting setup
- Best practices and coding standards

**Used by:**
- `/automate-ui-test` command
- UI automation workflow
- Framework maintenance

**Key sections:**
- Page Objects structure
- Locator strategies
- Wait strategies
- Test data management
- Reporting configuration

---

#### `test-automation/api-framework.md`
**Purpose:** Configuration for REST Assured + Java + TestNG framework

**Contains:**
- Framework structure and patterns
- REST Assured request specification
- POJO model conventions
- TestNG test organization
- Allure reporting setup
- Authentication strategies

**Used by:**
- `/automate-api-test` command
- API automation workflow
- Framework maintenance

**Key sections:**
- API endpoint organization
- Request/response models
- Authentication handling
- Validation patterns
- Error handling

---

### Setup & Reference Docs

#### `setup/setup-guide.md`
**Purpose:** Complete setup instructions for the project

**Contains:**
- Prerequisites and dependencies
- Installation steps
- Configuration instructions
- Verification steps
- Troubleshooting

**Audience:** New team members, onboarding

**Topics:**
- Java and Maven setup
- IDE configuration
- Framework dependencies
- Demo environment access
- Git workflow

---

#### `setup/playwright-mcp.md`
**Purpose:** Playwright MCP (Model Context Protocol) setup and usage

**Contains:**
- MCP server configuration
- Playwright integration setup
- Element inspection workflow
- Locator generation process
- Troubleshooting MCP issues

**Used by:**
- `/automate-ui-test` command (for locator inspection)
- Element inspection workflow

**Key features:**
- Live browser inspection
- Dynamic element detection
- CSS selector generation
- Screenshot capabilities

---

#### `setup/automation-reference.md`
**Purpose:** Quick reference for all automation commands

**Contains:**
- Complete command list
- Usage examples
- Parameter descriptions
- Workflow diagrams
- Common patterns

**Audience:** All users - quick reference guide

**Commands covered:**
- `/generate-ui-test`
- `/generate-api-test`
- `/automate-ui-test`
- `/automate-api-test`
- `/review-test-quality`
- `/assess-automation-feasibility`

---

## Configuration Management

### Adding New Functional Areas

**For UI:**
1. Edit `test-generation/ui-functional-areas.md`
2. Add new functional area following existing format
3. Include: name, description, min test count, documentation URLs
4. Update menu text section
5. Document in this README

**For API:**
1. Edit `test-generation/api-functional-areas.md`
2. Add new endpoint grouping following existing format
3. Include: name, description, min test count, key coverage, documentation URLs
4. Update menu text section
5. Document in this README

### Updating Framework Configurations

**UI Framework Updates:**
1. Edit `test-automation/ui-framework.md`
2. Update relevant sections (Page Objects, locators, etc.)
3. Test changes with `/automate-ui-test` command
4. Document breaking changes

**API Framework Updates:**
1. Edit `test-automation/api-framework.md`
2. Update relevant sections (POJOs, endpoints, etc.)
3. Test changes with `/automate-api-test` command
4. Document breaking changes

### Configuration Versioning

- All config files are version controlled in Git
- Use semantic versioning for major changes
- Document changes in commit messages
- Notify team of breaking changes

---

## File Naming Conventions

### General Rules
- Use lowercase with hyphens (kebab-case)
- Be descriptive but concise
- Group related configs in subdirectories
- Use `.md` extension for documentation configs

### Examples
- ✅ `ui-functional-areas.md` - Clear and descriptive
- ✅ `api-framework.md` - Concise and purposeful
- ❌ `uiAreas.md` - Avoid camelCase
- ❌ `config1.md` - Not descriptive

---

## Integration Points

### Commands That Use These Configs

| Command | Config Files Used |
|---------|------------------|
| `/generate-ui-test` | `test-generation/ui-functional-areas.md` |
| `/generate-api-test` | `test-generation/api-functional-areas.md` |
| `/automate-ui-test` | `test-automation/ui-framework.md`, `setup/playwright-mcp.md` |
| `/automate-api-test` | `test-automation/api-framework.md` |
| `/review-test-quality` | None (analyzes test case files directly) |
| `/assess-automation-feasibility` | None (analyzes test case files directly) |

### Config Loading Mechanism

Commands load configs using these patterns:
```markdown
# In command files:
Read `.claude/config/test-generation/ui-functional-areas.md`
```

All paths are relative to the `.claude/` directory.

---

## Maintenance Guidelines

### Regular Reviews
- **Monthly:** Review functional areas for new features
- **Quarterly:** Update framework configurations
- **As needed:** Setup guides when dependencies change

### Quality Checks
- Ensure all links are valid and accessible
- Verify minimum test counts are realistic
- Keep documentation URLs up to date
- Test commands after config changes

### Deprecation Process
1. Mark section as deprecated in config
2. Update commands to handle both old and new configs
3. Notify users via README or changelog
4. Remove deprecated configs after migration period

---

## Troubleshooting

### Config Not Found Errors

**Symptom:** Command fails with "file not found"

**Solution:**
1. Verify file exists at expected path
2. Check file permissions (`chmod 644`)
3. Ensure correct relative path from `.claude/`
4. Check for typos in file name

### Invalid Config Format

**Symptom:** Command parses config incorrectly

**Solution:**
1. Validate Markdown syntax
2. Check frontmatter format (if applicable)
3. Ensure consistent heading levels
4. Verify list formatting

### Outdated Documentation URLs

**Symptom:** Requirements analyzer finds 404 errors

**Solution:**
1. Update URLs in functional area configs
2. Test new URLs manually
3. Document URL changes in commit
4. Notify team of documentation moves

---

## Best Practices

### Writing Config Files

1. **Be Explicit:** Don't assume knowledge - spell out all details
2. **Use Examples:** Show concrete examples, not just descriptions
3. **Keep Updated:** Review configs when application changes
4. **Version Control:** Always commit config changes
5. **Document Changes:** Use descriptive commit messages

### Organizing Configs

1. **Group by Purpose:** Related configs in same directory
2. **Consistent Naming:** Follow naming conventions
3. **Single Source of Truth:** Avoid duplicating information
4. **Clear Hierarchy:** Use subdirectories for logical grouping

### Testing Config Changes

1. **Test Locally:** Run affected commands after changes
2. **Validate Syntax:** Check Markdown/JSON syntax
3. **Review Links:** Ensure all URLs are accessible
4. **Document Impact:** Note which commands are affected

---

## Migration History

### 2026-04-15: Major Reorganization
**Changes:**
- Consolidated `config/` and `input-config/` into single `config/` directory
- Created logical subdirectories: `test-generation/`, `test-automation/`, `setup/`
- Renamed files for clarity:
  - `ui-areas.md` → `ui-functional-areas.md`
  - `api-areas.md` → `api-functional-areas.md`
  - `ui-automation-config.md` → `ui-framework.md`
  - `api-automation-config.md` → `api-framework.md`
  - `SETUP_GUIDE.md` → `setup-guide.md`
  - `PLAYWRIGHT_MCP_SETUP.md` → `playwright-mcp.md`
  - `AUTOMATION_COMMANDS.md` → `automation-reference.md`
- Updated all command references to new paths
- Created this master README

**Impact:** All test generation and automation commands
**Breaking:** Yes - updated all path references

---

## Related Documentation

- **Project Root:** `/CLAUDE.md` - Project instructions and conventions
- **Commands:** `/.claude/commands/` - All available slash commands
- **Agents:** `/.claude/agents/` - Agent definitions
- **Test Cases:** `/test-cases/` - Generated test cases (UI and API)
- **Automation:** `/automation/` - Automation framework code

---

## Contributing

### Adding New Configs
1. Determine appropriate subdirectory (`test-generation/`, `test-automation/`, `setup/`)
2. Follow naming conventions (lowercase with hyphens)
3. Use Markdown format with clear structure
4. Add entry to this README
5. Update relevant commands to use new config
6. Test thoroughly
7. Submit PR with clear description

### Updating Existing Configs
1. Make changes in appropriate config file
2. Update "last modified" date if present
3. Document changes in this README (if significant)
4. Test affected commands
5. Submit PR

---

**Last Updated:** 2026-04-15
**Maintained by:** QA Automation Team
**Questions?** Check individual config files or contact the team.
