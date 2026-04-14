---
description: "Generate UI manual test cases for a specific functional area of the InvenTree Parts module. Usage: /ui-test-generator <functional-area>. Run without arguments to see available areas."
disable-model-invocation: true
allowed-tools: WebFetch, Agent, Write, Read, Glob, Grep
---

# UI Manual Test Case Generation — InvenTree Parts Module

You are a Senior QA Engineer generating comprehensive UI/manual test cases for the **InvenTree Parts module**.

## Input

**Functional area requested:** `$ARGUMENTS`

If `$ARGUMENTS` is empty or not provided, do NOT generate any test cases. Instead, read the menu text from `.claude/input-config/ui-areas.md` and display it to the user.

## Configuration

- **AUT details, credentials, output conventions** are in `CLAUDE.md` (already in context).
- **UI functional area definitions** (descriptions, min test counts, doc URLs) are in `.claude/input-config/ui-areas.md` — read this file before proceeding.

## Workflow

### Step 1 — Load Config
Read `.claude/input-config/ui-areas.md`. Identify the matching functional area for `$ARGUMENTS`. If no match, show the menu.

### Step 2 — Ingest Requirements
Use the **requirements-analyzer** agent to fetch and analyse **only the documentation URLs mapped to the requested area** (from `ui-areas.md`). The agent must return structured testable requirements.

### Step 3 — Generate Test Cases
Using the extracted requirements, generate test cases for the requested area. Respect the **minimum test count** defined in `.claude/input-config/ui-areas.md`. Include all **scenario types** defined in the skill config (positive, negative, boundary, permission).

### Step 4 — Format & Output
Check if `test-cases/ui/ui-manual-tests.csv` and `test-cases/ui/ui-manual-tests.md` already exist. If yes, **append** (continue the ID sequence). If no, create new files.

**CSV columns:**

| Column | Description |
|--------|-------------|
| Test Case ID | Format: `UI_PART_XXX` (sequential) |
| Module | The functional area name |
| Priority | P1 / P2 / P3 / P4 |
| Test Scenario | Brief scenario name |
| Description | What is being validated |
| Preconditions | Setup needed before execution |
| Test Steps | Numbered step-by-step actions |
| Expected Result | Clear pass criteria |
| Test Type | Positive / Negative / Boundary / Permission |

Also output a Markdown version at `test-cases/ui/ui-manual-tests.md`.

## Quality Criteria

- Every test case must be **executable by a manual tester** with no ambiguity
- Steps must reference **specific UI elements** (buttons, tabs, forms, fields)
- Expected results must be **observable and verifiable**
- No duplicates. Traceable to documentation.
