---
description: "Generate API manual test cases (invokes api-test-generator agent). Usage: /generate-api-test <functional-area>. Run without arguments to see available areas."
disable-model-invocation: true
allowed-tools: Agent, Read
---

# API Test Generation Command

**Lightweight wrapper for the API Test Generator agent.**

## Usage

```bash
/generate-api-test <functional-area>
```

**Examples:**
```bash
/generate-api-test part-crud
/generate-api-test part-list
/generate-api-test part-filter
```

**Without arguments:**
```bash
/generate-api-test
```
Shows menu of available functional areas.

---

## What This Command Does

This command is a **lightweight wrapper** that invokes the `api-test-generator` agent to handle the complete test generation workflow.

### Workflow Overview

The agent will:
1. ✅ Load functional area configuration
2. ✅ Spawn requirements-analyzer agent
3. ✅ Generate test cases (min 15+ per area)
4. ✅ **Automatically** run quality review
5. ✅ Display results and quality report

---

## Implementation

### Step 1: Parse Arguments

Extract functional area from `$ARGUMENTS`:
- If empty: Read `.claude/config/test-generation/api-functional-areas.md` and display menu
- If provided: Pass to agent

---

### Step 2: Invoke API Test Generator Agent

Use the **Agent** tool:

```
Agent(
  description: "Generate API test cases",
  prompt: """
    You are the API Test Generator agent defined in .claude/agents/api-test-generator.md.

    **Task:** Generate API test cases for functional area: {functional_area}

    **Instructions:**
    Follow the complete workflow defined in your agent file:
    1. Load configuration from .claude/config/test-generation/api-functional-areas.md
    2. Invoke requirements-analyzer agent for API schema URLs
    3. Generate comprehensive test cases (respect minimum test count)
    4. Automatically run quality review
    5. Display results and recommendations

    **Configuration:**
    - Config: .claude/config/test-generation/api-functional-areas.md
    - Output: test-cases/api/api-manual-tests.csv (and .md)

    Execute the complete generation workflow and report results.
  """
)
```

---

### Step 3: Display Results

The agent handles all output and quality review display.

---

## Agent Features

The `api-test-generator` agent provides:

### ✅ API Schema Analysis
- Spawns requirements-analyzer agent
- Extracts endpoint documentation
- Documents request/response schemas

### ✅ Comprehensive Test Generation
- Positive, negative, boundary, security tests
- CRUD operation coverage
- Concrete request payloads
- Exact expected responses

### ✅ Automatic Quality Review
- Completeness check
- Clarity scoring
- Coverage analysis
- Immediate feedback

---

## See Also

- **Agent definition:** `.claude/agents/api-test-generator.md`
- **Config:** `.claude/config/test-generation/api-functional-areas.md`
- **Orchestrator:** `/qa-orchestrator generate api part-crud`

---

**Note:** This is a wrapper - the agent does all the work.
