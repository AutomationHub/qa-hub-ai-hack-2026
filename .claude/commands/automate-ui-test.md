---
skill: automate-ui-test
description: Automate a UI test case (invokes ui-test-automator agent)
args: test-case-id
triggers:
  - "automate ui test"
  - "generate ui test automation"
  - "implement ui test case"
---

# UI Test Automation Command

**Lightweight wrapper for the UI Test Automator agent.**

## Usage

```bash
/automate-ui-test <test-case-id>
```

**Examples:**
```bash
/automate-ui-test UI_PART_001
/automate-ui-test UI_PART_014
```

---

## What This Command Does

This command is a **lightweight wrapper** that invokes the `ui-test-automator` agent to handle the complete UI test automation workflow.

### Workflow Overview

The agent will:
1. ✅ Read the test case from `test-cases/ui/`
2. ✅ **Automatically** assess automation feasibility (ROI, complexity)
3. ✅ Analyze the Selenium + POM framework
4. ✅ Create a git branch
5. ✅ Use Playwright MCP to inspect UI elements
6. ✅ Update Page Objects with real locators
7. ✅ Generate test methods following POM pattern
8. ✅ Verify compilation
9. ✅ Commit and push changes
10. ✅ Create pull request
11. ✅ Display comprehensive summary

---

## Implementation

### Step 1: Parse and Validate Arguments

Extract the test case ID from `$ARGUMENTS`:
- Format expected: `UI_PART_XXX` (e.g., UI_PART_001, UI_PART_014)
- Validate format with regex: `^UI_PART_\d{3}$`

**If no arguments provided:**
```
❌ Error: Missing test case ID

Usage: /automate-ui-test <test-case-id>

Example: /automate-ui-test UI_PART_001

Available test cases:
  Run: /qa-orchestrator assess ui
  Or check: test-cases/ui/ui-manual-tests.csv
```

**If invalid format:**
```
❌ Error: Invalid test case ID format

Expected: UI_PART_XXX (e.g., UI_PART_001)
Received: {invalid_input}

Please use the correct format and try again.
```

---

### Step 2: Invoke UI Test Automator Agent

Use the **Agent** tool to invoke the `ui-test-automator` agent:

```
Agent(
  description: "Automate UI test case",
  prompt: """
    You are the UI Test Automator agent defined in .claude/agents/ui-test-automator.md.

    **Task:** Automate UI test case {test_case_id}

    **Test Case ID:** {test_case_id}

    **Instructions:**
    Follow the complete workflow defined in your agent file:
    1. Validate and read the test case
    2. Assess automation feasibility (automatic)
    3. Get user confirmation if needed
    4. Analyze the framework structure
    5. Create git branch
    6. Use Playwright MCP to inspect elements
    7. Update Page Objects with real locators
    8. Generate test method
    9. Verify compilation
    10. Commit changes
    11. Create pull request
    12. Display comprehensive summary

    **Framework:**
    - Location: automation/ui/
    - Pattern: Page Object Model (Selenium + Java + TestNG)
    - Config: .claude/config/test-automation/ui-framework.md

    **Test Cases:**
    - Location: test-cases/ui/
    - Files: ui-manual-tests.csv, ui-manual-tests.md

    Execute the complete automation workflow and report results.
  """
)
```

---

### Step 3: Display Results

The agent will handle all output and user interaction.

This command simply invokes the agent and lets it control the workflow.

---

## Agent Features

The `ui-test-automator` agent provides:

### ✅ Automatic Feasibility Assessment
Before starting automation, the agent automatically:
- Calculates automation score (0-10)
- Computes ROI (time saved vs. effort)
- Assesses technical complexity
- Evaluates maintenance effort

**Decision points:**
- Score ≥ 6: Proceed automatically
- Score 4-6: Ask user confirmation (show warning)
- Score < 4: Recommend against automation (but allow override)

### ✅ Playwright Inspection
- Spawns specialized agent for element inspection
- Opens live InvenTree demo app
- Captures CSS selectors for all UI elements
- Prefers: ID > Name > CSS class > XPath

### ✅ Smart Code Generation
- Follows existing POM pattern
- Updates only relevant Page Objects
- Generates clean, testable code
- Includes proper TestNG annotations
- Adds comprehensive assertions

### ✅ Quality Checks
- Verifies Maven compilation
- Ensures POM pattern compliance
- Validates test structure
- Checks for proper logging

### ✅ Git Integration
- Creates feature branch
- Commits with descriptive message
- Pushes to remote
- Creates pull request with detailed description

---

## Error Handling

All error handling is managed by the agent:

- **Test case not found:** Lists available test cases
- **Low automation score:** Warns and asks confirmation
- **Playwright unavailable:** Asks for manual selectors
- **Compilation failed:** Attempts fixes, shows errors
- **PR creation failed:** Provides manual instructions

---

## Example Session

```bash
User: /automate-ui-test UI_PART_001

Output:
┌────────────────────────────────────────────────────────┐
│  UI Test Automation - Agent Invoked                   │
└────────────────────────────────────────────────────────┘

📋 Test Case: UI_PART_001
🤖 Agent: ui-test-automator
🔧 Framework: Selenium + POM

Invoking automation agent...

[Agent takes over execution]

📖 Reading test case UI_PART_001...
✅ Test case found: "Create part with required fields only"

🤖 Automation Feasibility Assessment...
✅ Score: 8.5/10 ⭐⭐⭐ (High Priority)
   ROI: 4.2:1 (Excellent)
   Complexity: Low (3/10)
   Estimated Effort: 2.5 hours

✅ Proceeding with automation...

🌿 Creating branch: automate-ui-ui_part_001...
✅ Branch created

🔍 Inspecting UI elements with Playwright...
   [Playwright agent spawned]
   ✅ 8 elements inspected
   ✅ CSS selectors generated

✏️ Updating Page Objects...
   ✅ CreatePartPage.java (3 locators updated)
   ✅ PartsListPage.java (2 locators updated)

🧪 Generating test method...
   ✅ testCreatePartWithRequiredFieldsOnly() added
   ✅ Assertions included
   ✅ POM pattern followed

🔨 Verifying compilation...
   ✅ mvn clean compile successful

💾 Committing changes...
   ✅ Committed: Automate UI test case UI_PART_001

📤 Pushing to remote...
   ✅ Pushed: origin/automate-ui-ui_part_001

🔗 Creating pull request...
   ✅ PR #42 created

╔════════════════════════════════════════════════════╗
║  ✅ UI TEST AUTOMATION COMPLETE                    ║
╚════════════════════════════════════════════════════╝

📋 Test Case: UI_PART_001
🔗 PR: https://github.com/.../pull/42
🌿 Branch: automate-ui-ui_part_001

🧪 Run locally:
   cd automation/ui
   mvn test -Dtest=PartCreationTest#testCreatePartWithRequiredFieldsOnly
```

---

## Benefits of Agent Pattern

### ✅ Separation of Concerns
- Command: Simple argument parsing and validation
- Agent: Complex automation workflow

### ✅ Reusability
- Agent can be invoked by orchestrator
- Agent can be invoked directly
- Agent can spawn sub-agents

### ✅ Maintainability
- Complex logic in agent file
- Easy to update workflow
- Clear responsibilities

### ✅ Async Capability
- Agent can run in background
- Long-running operations handled properly
- Progress tracking available

---

## See Also

- **Agent definition:** `.claude/agents/ui-test-automator.md`
- **Framework config:** `.claude/config/test-automation/ui-framework.md`
- **Test cases:** `test-cases/ui/ui-manual-tests.csv`
- **Orchestrator:** `/qa-orchestrator automate ui UI_PART_001`

---

## Notes

- This command is a **wrapper** - the agent does all the work
- The agent follows the workflow defined in its file
- All user interaction is handled by the agent
- This command simply validates input and invokes the agent

**For detailed workflow steps, see:** `.claude/agents/ui-test-automator.md`
