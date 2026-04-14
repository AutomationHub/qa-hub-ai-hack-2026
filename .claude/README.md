# .claude Directory Structure

This directory contains Claude Code configuration, automation commands, and project-specific settings for the QA Hub test automation project.

## Directory Structure

```
.claude/
├── commands/                      # Automation command skills
│   ├── automate-ui-test.md       # UI test automation command
│   └── automate-api-test.md      # API test automation command
├── config/                        # Configuration and guides
│   ├── AUTOMATION_COMMANDS.md    # Complete command documentation
│   ├── PLAYWRIGHT_MCP_SETUP.md   # Playwright MCP setup guide
│   └── SETUP_GUIDE.md           # Project setup guide
├── input-config/                  # Framework specifications
│   ├── ui-automation-config.md   # UI framework spec
│   └── api-automation-config.md  # API framework spec
├── settings.local.json            # Project-specific settings
└── README.md                      # This file
```

## Directory Purpose

### `/commands/`
**Purpose:** Custom automation command skills for Claude Code

Contains skill definitions that can be invoked with `/` commands:
- `/automate-ui-test` - Automates UI test cases with Playwright inspection
- `/automate-api-test` - Automates API test cases with REST Assured

**File Format:** Markdown with frontmatter
```markdown
---
skill: command-name
description: What the command does
args: argument-name
---
# Command workflow...
```

### `/config/`
**Purpose:** Documentation and configuration guides

Contains comprehensive guides for:
- **AUTOMATION_COMMANDS.md** - Complete command documentation (1,200+ lines)
  - Command workflows
  - Sub-agent architecture
  - Usage examples
  - Test case formats
  - Troubleshooting

- **PLAYWRIGHT_MCP_SETUP.md** - Playwright MCP setup and configuration
  - Installation steps
  - Configuration options
  - Troubleshooting
  - Testing guide

- **SETUP_GUIDE.md** - Complete project setup guide
  - Prerequisites
  - Initial setup
  - Running tests
  - Workflow
  - Quick reference

### `/input-config/`
**Purpose:** Framework specifications used during code generation

Contains the detailed specifications used by the framework generation commands:
- **ui-automation-config.md** - UI framework stack, dependencies, structure
- **api-automation-config.md** - API framework stack, dependencies, structure

These files were used during initial framework setup and serve as reference.

### `settings.local.json`
**Purpose:** Project-specific Claude Code settings

Contains:
- MCP server configurations (Playwright, etc.)
- Permissions for automated actions
- Tool allowlists
- Custom behaviors

**Example:**
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
      "Bash(docker --version)",
      "Bash(mv .claude/*)"
    ]
  }
}
```

## Key Files

### Automation Commands

| File | Command | Purpose |
|------|---------|---------|
| `commands/automate-ui-test.md` | `/automate-ui-test UI_PART_XXX` | Automate UI test cases |
| `commands/automate-api-test.md` | `/automate-api-test API_PART_XXX` | Automate API test cases |

### Documentation

| File | Purpose |
|------|---------|
| `config/AUTOMATION_COMMANDS.md` | Complete command documentation |
| `config/PLAYWRIGHT_MCP_SETUP.md` | Playwright MCP setup guide |
| `config/SETUP_GUIDE.md` | Complete project setup |

### Framework Specs

| File | Purpose |
|------|---------|
| `input-config/ui-automation-config.md` | UI framework specification |
| `input-config/api-automation-config.md` | API framework specification |

## Using the Commands

### Step 1: Ensure Setup is Complete

```bash
# Verify prerequisites
java -version  # 17+
mvn -version   # 3.6+
node --version # 18+
gh --version   # 2.0+

# Enable Playwright MCP (see config/PLAYWRIGHT_MCP_SETUP.md)
/settings → MCP Servers → Add → Playwright → Enable
```

### Step 2: Create Test Case

Create in `test-cases/ui/` or `test-cases/api/`:

**UI Test Case (test-cases/ui/feature.md):**
```markdown
## Test Case ID: UI_PART_001
**Title:** Create Part
**Steps:**
1. Click "Add Part"
2. Enter name
3. Save
**Expected:** Part created
```

**API Test Case (test-cases/api/feature.md):**
```markdown
## Test Case ID: API_PART_001
**Endpoint:** POST /api/part/
**Request:** {"name": "Test", "category": 1}
**Expected:** 201 Created
```

### Step 3: Run Command

```bash
/automate-ui-test UI_PART_001
# or
/automate-api-test API_PART_001
```

### Step 4: Review PR

The command will:
1. Create git branch
2. Generate test code
3. Update Page Objects/POJOs
4. Verify compilation
5. Create Pull Request

Review and merge the PR!

## Configuration

### Enabling MCP Servers

To enable Playwright MCP or other MCP servers:

**Option 1: Via UI**
```bash
/settings → MCP Servers → Add → Select Server → Enable
```

**Option 2: Edit settings.local.json**
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  }
}
```

See `config/PLAYWRIGHT_MCP_SETUP.md` for detailed instructions.

### Adding Permissions

To allow specific Bash commands without prompts:

Edit `settings.local.json`:
```json
{
  "permissions": {
    "allow": [
      "Bash(git status)",
      "Bash(mvn compile)"
    ]
  }
}
```

## Sub-Agent Architecture

The automation commands use specialized sub-agents:

### UI Command Sub-Agents
**Playwright Inspection Agent**
- Navigates to live application
- Takes screenshots
- Inspects elements
- Generates CSS selectors and XPath
- Returns locator mappings

### API Command Sub-Agents
**API Analysis Agent** (optional)
- Calls live API endpoints
- Analyzes response structure
- Documents field types
- Tests error scenarios
- Returns API behavior documentation

## Workflow Diagrams

### UI Test Automation Flow
```
Test Case → Read → Playwright Agent → Find Locators → Update Page Objects → Generate Test → PR
```

### API Test Automation Flow
```
Test Case → Read → Analyze POJOs → (Optional: API Agent) → Update POJOs → Generate Test → PR
```

## Troubleshooting

### Command Not Found

**Problem:** `/automate-ui-test` command not recognized

**Solution:** Commands are defined in `.claude/commands/`. Restart Claude Code or check that files exist.

### Playwright MCP Not Available

**Problem:** Playwright tools not available during UI automation

**Solution:** See `config/PLAYWRIGHT_MCP_SETUP.md` for setup instructions.

### Permission Denied

**Problem:** Command fails with permission error

**Solution:** Add permission to `settings.local.json`:
```json
{
  "permissions": {
    "allow": ["Bash(your-command)"]
  }
}
```

## Best Practices

✅ **Read documentation** in `config/` before first use
✅ **Enable Playwright MCP** before running UI automation
✅ **Review generated PRs** carefully before merging
✅ **Keep test cases well-formatted** for better code generation
✅ **Update settings.local.json** for project-specific needs
✅ **Don't commit secrets** to settings files

## Documentation Index

| Topic | File |
|-------|------|
| Command usage | `config/AUTOMATION_COMMANDS.md` |
| Playwright setup | `config/PLAYWRIGHT_MCP_SETUP.md` |
| Complete setup | `config/SETUP_GUIDE.md` |
| UI framework spec | `input-config/ui-automation-config.md` |
| API framework spec | `input-config/api-automation-config.md` |
| Command definitions | `commands/*.md` |

## Contributing

To add new automation commands:

1. Create skill file in `commands/`
2. Follow existing format (frontmatter + workflow)
3. Document in `config/AUTOMATION_COMMANDS.md`
4. Test the command
5. Update this README

## Version History

- **2026-04-14** - Initial setup
  - Created directory structure
  - Added UI and API automation commands
  - Created documentation guides
  - Configured Playwright MCP

## Related Documentation

- **Project root:** `../AUTOMATION_SETUP_SUMMARY.md` - Complete project summary
- **UI framework:** `../automation/ui/README.md` - UI framework docs
- **API framework:** `../automation/api/README.md` - API framework docs
- **Project instructions:** `../CLAUDE.md` - Claude instructions

---

**For support:** See guides in `config/` or ask Claude directly.
