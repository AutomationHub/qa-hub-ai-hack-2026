---
description: "Generate UI manual test cases (invokes ui-test-generator agent). Usage: /generate-ui-test <functional-area>. Run without arguments to see available areas."
disable-model-invocation: true
allowed-tools: Agent, Read
---

# UI Test Generation Command

**Lightweight wrapper for the UI Test Generator agent.**

## Usage

```bash
/generate-ui-test <functional-area>
```

**Examples:**
```bash
/generate-ui-test part-creation
/generate-ui-test part-detail-view
/generate-ui-test part-attributes
```

**Without arguments:**
```bash
/generate-ui-test
```
Shows menu of available functional areas.

---

## What This Command Does

This command is a **lightweight wrapper** that invokes the `ui-test-generator` agent to handle the complete test generation workflow.

### Workflow Overview

The agent will:
1. ✅ Load functional area configuration
2. ✅ Spawn requirements-analyzer agent
3. ✅ Generate test cases (min 12+ per area)
4. ✅ **Automatically** run quality review
5. ✅ Display results and quality report

---

## Implementation

### Step 1: Parse Arguments

Extract functional area from `$ARGUMENTS`:
- If empty: Read `.claude/config/test-generation/ui-functional-areas.md` and display menu
- If provided: Pass to agent

---

### Step 2: Invoke UI Test Generator Agent

Use the **Agent** tool:

```
Agent(
  description: "Generate UI test cases",
  prompt: """
    You are the UI Test Generator agent defined in .claude/agents/ui-test-generator.md.

    **Task:** Generate UI test cases for functional area: {functional_area}

    **Instructions:**
    Follow the complete workflow defined in your agent file:
    1. Load configuration from .claude/config/test-generation/ui-functional-areas.md
    2. Invoke requirements-analyzer agent for documentation URLs
    3. Generate comprehensive test cases (respect minimum test count)
    4. Automatically run quality review
    5. Display results and recommendations

    **Configuration:**
    - Config: .claude/config/test-generation/ui-functional-areas.md
    - Output: test-cases/ui/ui-manual-tests.csv (and .md)

    Execute the complete generation workflow and report results.
  """
)
```

---

### Step 3: Display Results

The agent handles all output and quality review display.

---

## Agent Features

The `ui-test-generator` agent provides:

### ✅ Requirements Analysis
- Spawns requirements-analyzer agent
- Extracts 50-150+ testable requirements
- Categorizes by type

### ✅ Comprehensive Test Generation
- Positive, negative, boundary, permission tests
- Balanced distribution
- Specific UI element references
- Observable expected results

### ✅ Automatic Quality Review
- Completeness check
- Clarity scoring
- Coverage analysis
- Immediate feedback

---

## See Also

- **Agent definition:** `.claude/agents/ui-test-generator.md`
- **Config:** `.claude/config/test-generation/ui-functional-areas.md`
- **Orchestrator:** `/qa-orchestrator generate ui part-creation`

---

**Note:** This is a wrapper - the agent does all the work.
