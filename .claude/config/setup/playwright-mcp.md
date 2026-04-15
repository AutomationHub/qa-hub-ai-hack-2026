# Playwright MCP Setup Guide

Playwright MCP (Model Context Protocol) enables Claude to control a browser for inspecting web applications and generating accurate element locators.

## What is Playwright MCP?

Playwright MCP is a tool that allows Claude to:
- Navigate to web pages
- Take screenshots
- Inspect elements
- Find CSS selectors and XPath
- Interact with the page

This is **critical** for the `/automate-ui-test` command to generate real locators instead of placeholders.

## Installation

### Step 1: Install Node.js (if not already installed)

```bash
# MacOS
brew install node

# Verify
node --version  # Should be 18+
npm --version
```

### Step 2: Test Playwright MCP

The Playwright MCP server is already available globally. Test it:

```bash
npx @playwright/mcp@latest
```

If this runs without errors, Playwright MCP is ready!

## Enable Playwright MCP for This Project

### Option 1: Enable via Settings UI (Recommended)

1. In Claude Code, type `/settings` or press `Cmd+,` (Mac) / `Ctrl+,` (Windows)
2. Go to **MCP Servers** tab
3. Click **Add Server**
4. Select **Playwright** from the list
5. Click **Enable**

### Option 2: Manual Configuration

Add Playwright MCP to `settings.local.json`:

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

Then restart Claude Code.

## Verify Installation

After enabling, verify Playwright MCP is available:

1. In Claude Code, start a new conversation
2. Type: "Use playwright to navigate to https://example.com and take a screenshot"
3. Claude should be able to use Playwright tools

You should see tools like:
- `playwright_navigate`
- `playwright_screenshot`
- `playwright_click`
- `playwright_fill`
- `playwright_evaluate`

## For `/automate-ui-test` Command

Once Playwright MCP is enabled, the `/automate-ui-test` command will:

1. Launch a Playwright sub-agent
2. Navigate to InvenTree demo (https://demo.inventree.org)
3. Login with admin credentials
4. Inspect elements mentioned in test case
5. Generate real CSS selectors and XPath
6. Update Page Objects with discovered locators

**Without Playwright MCP:** The command will skip element inspection and use placeholder locators (requires manual update).

**With Playwright MCP:** The command generates production-ready locators automatically.

## Troubleshooting

### Issue: "Playwright MCP not found"

**Solution:**
```bash
# Install globally
npm install -g @playwright/mcp

# Or use npx (downloads automatically)
npx @playwright/mcp@latest
```

### Issue: Browser not launching

**Solution:**
```bash
# Install Playwright browsers
npx playwright install
```

### Issue: Permission denied

**Solution:**
```bash
# Give execute permission
chmod +x $(which npx)
```

### Issue: Node.js version too old

**Solution:**
```bash
# Update Node.js
brew upgrade node  # MacOS
# Or download from nodejs.org
```

## Testing Playwright MCP

Create a test conversation:

```
You: Use Playwright to navigate to https://demo.inventree.org and find the login form
Claude: [Uses playwright_navigate and playwright_screenshot]
```

If Claude can use Playwright tools, setup is successful!

## Configuration Files

### Global Configuration
Located at: `~/.claude/plugins/marketplaces/claude-plugins-official/external_plugins/playwright/.mcp.json`

```json
{
  "playwright": {
    "command": "npx",
    "args": ["@playwright/mcp@latest"]
  }
}
```

### Project Configuration
Located at: `.claude/settings.local.json`

Add MCP servers configuration:
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

## Advanced Configuration

### Headless Mode

Run Playwright in headless mode (no visible browser):
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"],
      "env": {
        "PLAYWRIGHT_HEADLESS": "true"
      }
    }
  }
}
```

### Custom Browser

Use a specific browser (chromium, firefox, webkit):
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"],
      "env": {
        "PLAYWRIGHT_BROWSER": "chromium"
      }
    }
  }
}
```

### Screenshot Directory

Save screenshots to a custom directory:
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"],
      "env": {
        "PLAYWRIGHT_SCREENSHOTS_PATH": "./screenshots"
      }
    }
  }
}
```

## Usage in Automation Commands

### `/automate-ui-test` with Playwright MCP

When you run:
```bash
/automate-ui-test UI_PART_001
```

The command will:
1. Read test case from `test-cases/ui/`
2. **Launch Playwright Inspection Agent**
3. Agent uses Playwright MCP to:
   - Navigate to https://demo.inventree.org
   - Login as admin
   - Find elements: "Add Part" button, input fields, etc.
   - Generate selectors: `button[data-testid="add-part"]`
4. Update Page Objects with real locators
5. Generate test method
6. Create PR

### Without Playwright MCP

If Playwright MCP is not available:
- Command will generate placeholder locators
- You'll need to manually inspect elements
- Page Objects will have `// TODO: update selector` comments
- Still functional, just requires manual work

## Best Practices

✅ **Enable Playwright MCP** before running `/automate-ui-test`
✅ **Test Playwright** independently before automation
✅ **Use headless mode** in CI/CD environments
✅ **Keep Playwright updated**: `npm update -g @playwright/mcp`
✅ **Review generated locators** - Playwright may suggest multiple strategies

## Security Notes

- Playwright MCP runs in a sandboxed environment
- Only accesses URLs explicitly provided
- Screenshots are temporary and local
- No data is sent to external services

## Alternative: Manual Locator Finding

If you prefer not to use Playwright MCP:

1. Open browser DevTools (F12)
2. Use element picker
3. Find CSS selector or XPath
4. Manually update Page Objects

Example:
```java
// Find element in browser
// Right-click → Inspect
// Copy selector: button[data-testid="add-part"]

// Update Page Object
private static final By ADD_PART_BUTTON = By.cssSelector("button[data-testid='add-part']");
```

## Resources

- **Playwright MCP Docs:** https://github.com/microsoft/playwright-mcp
- **Playwright Docs:** https://playwright.dev
- **MCP Protocol:** https://modelcontextprotocol.io
- **Claude Code MCP Guide:** Run `/help mcp` in Claude Code

## Quick Reference

### Enable Playwright MCP
```bash
# In Claude Code
/settings → MCP Servers → Add → Playwright → Enable
```

### Verify Playwright MCP
```bash
# In conversation
"Use playwright to navigate to https://example.com"
```

### Test Automation Command
```bash
# Create test case, then run:
/automate-ui-test UI_PART_001
```

### Check Available Tools
```bash
# In conversation
"What playwright tools are available?"
```

---

**Need help?** Ask Claude: "How do I set up Playwright MCP?" or check the documentation above.
