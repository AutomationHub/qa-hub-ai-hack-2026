---
skill: automate-api-test
description: Automate an API test case (invokes api-test-automator agent)
args: test-case-id
triggers:
  - "automate api test"
  - "generate api test automation"
  - "implement api test case"
---

# API Test Automation Command

**Lightweight wrapper for the API Test Automator agent.**

## Usage

```bash
/automate-api-test <test-case-id>
```

**Examples:**
```bash
/automate-api-test API_PART_001
/automate-api-test API_PART_015
```

---

## What This Command Does

This command is a **lightweight wrapper** that invokes the `api-test-automator` agent to handle the complete API test automation workflow.

### Workflow Overview

The agent will:
1. ✅ Read the test case from `test-cases/api/`
2. ✅ **Automatically** assess automation feasibility (ROI, complexity)
3. ✅ Analyze the REST Assured framework
4. ✅ Create a git branch
5. ✅ Analyze API responses (if needed)
6. ✅ Generate/update POJO models
7. ✅ Generate test methods using REST Assured
8. ✅ Verify compilation
9. ✅ Commit and push changes
10. ✅ Create pull request
11. ✅ Display comprehensive summary

---

## Implementation

### Step 1: Parse and Validate Arguments

Extract the test case ID from `$ARGUMENTS`:
- Format expected: `API_PART_XXX` (e.g., API_PART_001, API_PART_015)
- Validate format with regex: `^API_PART_\d{3}$`

**If no arguments provided:**
```
❌ Error: Missing test case ID

Usage: /automate-api-test <test-case-id>

Example: /automate-api-test API_PART_001

Available test cases:
  Run: /qa-orchestrator assess api
  Or check: test-cases/api/api-manual-tests.csv
```

**If invalid format:**
```
❌ Error: Invalid test case ID format

Expected: API_PART_XXX (e.g., API_PART_001)
Received: {invalid_input}

Please use the correct format and try again.
```

---

### Step 2: Invoke API Test Automator Agent

Use the **Agent** tool to invoke the `api-test-automator` agent:

```
Agent(
  description: "Automate API test case",
  prompt: """
    You are the API Test Automator agent defined in .claude/agents/api-test-automator.md.

    **Task:** Automate API test case {test_case_id}

    **Test Case ID:** {test_case_id}

    **Instructions:**
    Follow the complete workflow defined in your agent file:
    1. Validate and read the test case
    2. Assess automation feasibility (automatic)
    3. Get user confirmation if needed
    4. Analyze the framework structure
    5. Create git branch
    6. Analyze API response (if needed)
    7. Generate/update POJO models
    8. Generate test method using REST Assured
    9. Verify compilation
    10. Commit changes
    11. Create pull request
    12. Display comprehensive summary

    **Framework:**
    - Location: automation/api/
    - Pattern: REST Assured + POJOs + TestNG
    - Config: .claude/config/test-automation/api-framework.md

    **Test Cases:**
    - Location: test-cases/api/
    - Files: api-manual-tests.csv, api-manual-tests.md

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

The `api-test-automator` agent provides:

### ✅ Automatic Feasibility Assessment
Before starting automation, the agent automatically:
- Calculates automation score (typically 7-10 for API tests)
- Computes ROI (API tests usually have excellent ROI)
- Assesses technical complexity (usually lower than UI)
- Evaluates maintenance effort

**Note:** API tests typically score HIGHER than UI tests because:
- More stable interfaces
- Easier to automate
- Less brittle
- Better maintainability

### ✅ API Response Analysis
- Optionally spawns agent to call live API
- Documents response structure
- Identifies all fields and types
- Tests error scenarios

### ✅ Smart POJO Generation
- Creates type-safe Java models
- Handles nested objects
- Uses Jackson annotations
- Follows framework conventions

### ✅ REST Assured Test Generation
- Follows framework patterns
- Comprehensive assertions (status, headers, body)
- Tests positive and negative scenarios
- Includes proper TestNG annotations

### ✅ Quality Checks
- Verifies Maven compilation
- Ensures framework pattern compliance
- Validates POJO serialization
- Checks test structure

### ✅ Git Integration
- Creates feature branch
- Commits with descriptive message
- Pushes to remote
- Creates detailed pull request

---

## Error Handling

All error handling is managed by the agent:

- **Test case not found:** Lists available test cases
- **Low automation score:** Warns (rare for API tests)
- **API unavailable:** Provides alternatives
- **Compilation failed:** Attempts fixes, shows errors
- **PR creation failed:** Provides manual instructions

---

## Example Session

```bash
User: /automate-api-test API_PART_001

Output:
┌────────────────────────────────────────────────────────┐
│  API Test Automation - Agent Invoked                  │
└────────────────────────────────────────────────────────┘

📋 Test Case: API_PART_001
🤖 Agent: api-test-automator
🔧 Framework: REST Assured

Invoking automation agent...

[Agent takes over execution]

📖 Reading test case API_PART_001...
✅ Test case found: "Create part with valid data"
   Endpoint: POST /api/part/
   Expected Status: 201

🤖 Automation Feasibility Assessment...
✅ Score: 9.2/10 ⭐⭐⭐ (Excellent)
   ROI: 6.5:1 (Outstanding)
   Complexity: Low (2/10)
   Estimated Effort: 1.5 hours

✅ Proceeding with automation...

🌿 Creating branch: automate-api-api_part_001...
✅ Branch created

🔍 Analyzing API response...
   ✅ Response structure documented
   ✅ 12 fields identified

📝 Generating POJO models...
   ✅ Part.java created
   ✅ PartCreateRequest.java created

🧪 Generating test method...
   ✅ testCreatePartWithValidData() added
   ✅ Status code assertion included
   ✅ Response body assertions included
   ✅ REST Assured pattern followed

🔨 Verifying compilation...
   ✅ mvn clean compile successful

💾 Committing changes...
   ✅ Committed: Automate API test case API_PART_001

📤 Pushing to remote...
   ✅ Pushed: origin/automate-api-api_part_001

🔗 Creating pull request...
   ✅ PR #43 created

╔════════════════════════════════════════════════════════╗
║  ✅ API TEST AUTOMATION COMPLETE                       ║
╚════════════════════════════════════════════════════════╝

📋 Test Case: API_PART_001
🔗 PR: https://github.com/.../pull/43
🌿 Branch: automate-api-api_part_001

🧪 Run locally:
   cd automation/api
   mvn test -Dtest=PartApiTest#testCreatePartWithValidData
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

- **Agent definition:** `.claude/agents/api-test-automator.md`
- **Framework config:** `.claude/config/test-automation/api-framework.md`
- **Test cases:** `test-cases/api/api-manual-tests.csv`
- **Orchestrator:** `/qa-orchestrator automate api API_PART_001`

---

## Notes

- This command is a **wrapper** - the agent does all the work
- The agent follows the workflow defined in its file
- All user interaction is handled by the agent
- This command simply validates input and invokes the agent
- API tests typically have higher automation scores than UI tests

**For detailed workflow steps, see:** `.claude/agents/api-test-automator.md`
