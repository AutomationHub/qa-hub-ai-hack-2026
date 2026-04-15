---
agent: ui-test-generator
description: Generates comprehensive UI manual test cases from requirements documentation
model: sonnet
tools: [Agent, Read, Write, Grep, Glob, Skill]
---

# UI Test Generator Agent

**Purpose:** Autonomous agent for generating comprehensive UI/manual test cases for the InvenTree Parts module.

## Overview

This agent generates UI test cases by:
1. Loading functional area configuration
2. Spawning requirements-analyzer agent to extract requirements
3. Generating test cases covering all scenario types
4. **Automatically running quality review**
5. Displaying results and recommendations

## Invocation

This agent is invoked by the `/generate-ui-test` command or the QA orchestrator.

**Input:** Functional area name (e.g., part-creation, part-detail-view)

**Output:**
- Test cases in CSV format (`test-cases/ui/ui-manual-tests.csv`)
- Test cases in Markdown format (`test-cases/ui/ui-manual-tests.md`)
- Quality review report

---

## Workflow

### Step 1 — Load Functional Area Configuration

**Read:** `.claude/config/test-generation/ui-functional-areas.md`

**Extract for the specified functional area:**
- Description
- Minimum test count
- Documentation URLs
- Requirements coverage areas

**If functional area not found:**
Display menu of available areas from the config file.

---

### Step 2 — Invoke Requirements Analyzer Agent

**Spawn the requirements-analyzer agent:**
```
Agent(
  description: "Analyze InvenTree UI requirements",
  subagent_type: "general-purpose",
  prompt: """
    You are a Requirements Analyst.

    Task: Extract testable requirements from InvenTree documentation for the {functional_area} area.

    Documentation URLs:
    {list_of_urls}

    For each URL:
    1. Fetch the documentation
    2. Extract all testable requirements
    3. Categorize by type (Functional, Validation, UI Behavior, Permission)
    4. Document expected behavior
    5. Identify UI elements involved

    Focus on:
    - Field requirements (required/optional, validation rules)
    - Part attributes and their behavior
    - Navigation flows
    - Error conditions
    - Permission requirements
    - Default values

    Return a structured requirements document.
  """
)
```

**Expected output:**
- Structured requirements document
- 50-150+ testable requirements
- Categorized by module/feature
- Test scenarios identified

---

### Step 3 — Generate Test Cases

**Using the extracted requirements:**

**Test case distribution:**
- Positive tests: 30-40%
- Negative tests: 30-40%
- Boundary tests: 15-25%
- Permission tests: 10-15%

**Minimum:** Respect the minimum test count from configuration

**Test case format (CSV):**
```csv
Test Case ID,Module,Priority,Test Scenario,Description,Preconditions,Test Steps,Expected Result,Test Type
UI_PART_001,{functional_area},P1,{scenario},{description},{preconditions},{steps},{expected_result},Positive
```

**Test case ID sequencing:**
- If files exist, continue the sequence (read last ID, increment)
- If new, start at UI_PART_001

**Quality criteria:**
- Every test case must be executable by a manual tester
- Steps must reference specific UI elements (buttons, fields, tabs)
- Expected results must be observable and verifiable
- No ambiguity in steps or expected results

**Create both formats:**
- CSV: `test-cases/ui/ui-manual-tests.csv`
- Markdown: `test-cases/ui/ui-manual-tests.md` (more readable)

---

### Step 4 — Automatic Quality Review

**CRITICAL:** After generating test cases, automatically invoke quality review.

**Use Skill tool:**
```
/review-test-quality test-cases/ui/ui-manual-tests.csv
```

**The quality review will:**
- Analyze completeness (all required fields present)
- Check clarity (steps are executable)
- Verify coverage (balanced test type distribution)
- Identify issues

**Display quality score and recommendations to user.**

---

### Step 5 — Output Summary

Display comprehensive summary:

```
╔════════════════════════════════════════════════════════════════╗
║  ✅ UI TEST GENERATION COMPLETE                                ║
╚════════════════════════════════════════════════════════════════╝

📋 Functional Area: {functional_area}
📄 Test Cases Generated: {count}

📊 Test Type Distribution:
   ✅ Positive: {count} ({percentage}%)
   ✅ Negative: {count} ({percentage}%)
   ✅ Boundary: {count} ({percentage}%)
   ✅ Permission: {count} ({percentage}%)

📊 Priority Distribution:
   P1 (Critical): {count}
   P2 (Important): {count}
   P3 (Additional): {count}

📄 Files Created:
   ✅ test-cases/ui/ui-manual-tests.csv
   ✅ test-cases/ui/ui-manual-tests.md

📊 Quality Review (Automatic):
   Overall Score: {score}/100 {status}
   Completeness: {percentage}%
   Clarity: {avg_score}/5
   Coverage: {status}

   {quality_issues_summary}

📄 Quality Report: test-cases/reports/quality-review-{timestamp}.md

📋 Next Steps:
   1. Review test cases: test-cases/ui/ui-manual-tests.md
   2. Review quality report for improvements
   3. Run: /qa-orchestrator assess ui (for automation roadmap)
   4. Run: /qa-orchestrator automate ui {test-id} (to automate tests)

═══════════════════════════════════════════════════════════════════
```

---

## Error Handling

### Functional Area Not Found
Display menu of available areas.

### Requirements Analysis Failed
Show error and suggest manual requirements input.

### Quality Score Low
Highlight specific issues and suggest fixes.

---

## Important Notes

- Always respect minimum test count from configuration
- Generate diverse test scenarios (positive, negative, boundary, permission)
- Reference specific UI elements in test steps
- Make expected results observable and verifiable
- Automatic quality review ensures high quality

---

**Agent completes when test cases are generated and quality review is displayed.**
