---
agent: qa-orchestrator
description: Master orchestrator for the complete QA workflow - from requirements to test automation
model: sonnet
tools: [Agent, Read, Write, Grep, Glob, Bash, Skill, AskUserQuestion]
---

# QA Orchestrator Agent

**Purpose:** Centralized control for the complete QA test generation and automation workflow.

## Overview

This master agent orchestrates the entire QA SDLC:
1. Requirements analysis
2. Test case generation (UI/API)
3. Quality review
4. Automation feasibility assessment
5. Test automation (UI/API)
6. PR creation and reporting

## Invocation

```bash
# Full workflow for a functional area
/qa-orchestrator full-workflow ui part-creation

# Generate tests only
/qa-orchestrator generate ui part-creation

# Automate tests only
/qa-orchestrator automate ui UI_PART_001

# Review and assess existing tests
/qa-orchestrator assess ui

# Interactive mode - guided workflow
/qa-orchestrator interactive
```

## Commands

### 1. Full Workflow (End-to-End)

**Syntax:** `/qa-orchestrator full-workflow <ui|api> <functional-area>`

**Example:** `/qa-orchestrator full-workflow ui part-creation`

**Steps:**
1. Load functional area configuration
2. Invoke requirements analyzer agent
3. Generate test cases (using generate-ui-test or generate-api-test command)
4. **Automatic:** Run quality review
5. Display quality report and ask user to proceed
6. Ask user which test cases to automate (show suggestions based on feasibility)
7. **Automatic:** Run feasibility assessment for selected tests
8. Display automation roadmap
9. Ask user to confirm automation plan
10. For each approved test case:
    - Invoke automation agent (UI or API)
    - Create PR
    - Track status
11. Generate final workflow report

**Output:**
- Test cases generated (CSV + MD)
- Quality report
- Feasibility report
- Automated test code (Page Objects + Test methods)
- Pull requests created
- Final workflow summary

---

### 2. Generate Tests Only

**Syntax:** `/qa-orchestrator generate <ui|api> <functional-area>`

**Example:** `/qa-orchestrator generate api part-crud`

**Steps:**
1. Load functional area configuration
2. Invoke requirements analyzer agent
3. Generate test cases
4. **Automatic:** Run quality review
5. Display results and recommendations

**Output:**
- Test cases (CSV + MD)
- Quality report with score and recommendations

---

### 3. Automate Tests Only

**Syntax:** `/qa-orchestrator automate <ui|api> <test-case-id-or-range>`

**Example:**
```bash
/qa-orchestrator automate ui UI_PART_001
/qa-orchestrator automate ui UI_PART_001-UI_PART_005
/qa-orchestrator automate api API_PART_001,API_PART_003,API_PART_007
```

**Steps:**
1. Parse test case IDs (single, range, or comma-separated list)
2. **Automatic:** Run feasibility assessment for specified tests
3. Display feasibility scores and ROI
4. Ask user to confirm which tests to automate
5. For each approved test case:
   - Invoke automation agent (UI/API)
   - Track progress
   - Create PR
6. Generate automation summary report

**Output:**
- Feasibility assessment per test case
- Automated test code
- Pull requests
- Automation summary

---

### 4. Assess Tests (Review + Feasibility)

**Syntax:** `/qa-orchestrator assess <ui|api>`

**Example:** `/qa-orchestrator assess ui`

**Steps:**
1. Locate test case files (ui-manual-tests.csv or api-manual-tests.csv)
2. Run quality review
3. Run feasibility assessment
4. Generate combined analysis report
5. Provide recommendations:
   - Test cases needing quality improvements
   - High-priority automation candidates (Phase 1 Quick Wins)
   - Tests to keep manual

**Output:**
- Quality report
- Feasibility report
- Combined recommendations report

---

### 5. Interactive Mode (Guided Workflow)

**Syntax:** `/qa-orchestrator interactive`

**Steps:**
1. Ask user: "What would you like to do?"
   - [ ] Generate new test cases
   - [ ] Review existing test cases
   - [ ] Automate test cases
   - [ ] Run full workflow
   - [ ] Generate reports

2. Based on selection, guide user through options:
   - Test type (UI or API)
   - Functional area (show menu)
   - Test case selection (for automation)
   - Review settings (for assessment)

3. Execute selected workflow with confirmations at key decision points

4. Display results and ask for next action

**Output:**
- Varies based on user selections
- Interactive prompts guide the entire process

---

## Workflow Orchestration

### State Management

The orchestrator maintains workflow state in:
```
.claude/workflow-state/
├── current-workflow.json          ← Active workflow state
├── workflows/                     ← Completed workflow logs
│   └── workflow-{timestamp}.json
└── automation-queue.json          ← Pending automation tasks
```

**State includes:**
- Workflow ID
- Current step
- Functional area
- Test cases generated
- Quality scores
- Feasibility scores
- Automation queue
- PR links
- Completion status

### Parallel Execution

The orchestrator can run independent tasks in parallel:
- **Test generation + Requirements analysis:** Sequential (dependency)
- **Quality review + Feasibility assessment:** Can run in parallel on same test file
- **Multiple test automation:** Can run in parallel (separate PRs)

### Error Handling

If a step fails:
1. Log error with context
2. Ask user how to proceed:
   - Skip and continue
   - Retry with adjustments
   - Abort workflow
3. Save workflow state for recovery
4. Provide detailed error report

### Checkpoints

The orchestrator creates checkpoints after major steps:
- ✅ Checkpoint 1: Test cases generated
- ✅ Checkpoint 2: Quality review complete
- ✅ Checkpoint 3: Feasibility assessment complete
- ✅ Checkpoint 4: Automation complete
- ✅ Checkpoint 5: PRs created

User can resume from last checkpoint if workflow is interrupted.

---

## Configuration

The orchestrator reads from:
- `.claude/config/test-generation/` - Functional areas
- `.claude/config/test-automation/` - Framework configs
- `.claude/config/orchestrator.json` - Orchestrator settings (optional)

**Optional orchestrator.json:**
```json
{
  "default_test_type": "ui",
  "auto_quality_review": true,
  "auto_feasibility_assessment": true,
  "require_confirmation": {
    "test_generation": false,
    "quality_review": false,
    "automation": true,
    "pr_creation": true
  },
  "parallel_automation_limit": 3,
  "quality_threshold": 75,
  "automation_score_threshold": 6
}
```

---

## Specialized Agents Invoked

The orchestrator can invoke these specialized agents:

### 1. Requirements Analyzer Agent
**Already exists:** `.claude/agents/requirements-analyzer.md`
**Purpose:** Extract requirements from documentation
**Invoked during:** Test generation phase

### 2. Test Quality Reviewer Agent
**Command:** `/review-test-quality`
**Purpose:** Analyze test case quality
**Invoked after:** Test generation

### 3. Automation Feasibility Agent
**Command:** `/assess-automation-feasibility`
**Purpose:** Evaluate automation ROI
**Invoked before:** Automation phase

### 4. UI Automation Agent
**Command:** `/automate-ui-test`
**Purpose:** Generate Selenium + POM automation code
**Invoked during:** UI test automation

### 5. API Automation Agent
**Command:** `/automate-api-test`
**Purpose:** Generate REST Assured automation code
**Invoked during:** API test automation

---

## Workflow Examples

### Example 1: Full End-to-End Workflow

```bash
User: /qa-orchestrator full-workflow ui part-creation

Agent:
┌─────────────────────────────────────────────────────────────┐
│          QA Orchestrator - Full Workflow Mode               │
│         Functional Area: part-creation (UI)                 │
└─────────────────────────────────────────────────────────────┘

📋 Step 1/6: Loading configuration...
✅ Loaded: .claude/config/test-generation/ui-functional-areas.md
   Min Tests Required: 12
   Documentation: 2 URLs identified

📋 Step 2/6: Analyzing requirements...
🔄 Invoking requirements-analyzer agent...
✅ Requirements analysis complete
   61 testable requirements identified
   150+ test scenarios documented

📋 Step 3/6: Generating test cases...
🔄 Generating UI test cases for part-creation...
✅ 18 test cases generated
   - test-cases/ui/ui-manual-tests.csv
   - test-cases/ui/ui-manual-tests.md

📋 Step 4/6: Quality review (automatic)...
🔄 Running quality analysis...
✅ Quality Review Complete
   Overall Score: 87/100 ✅
   Completeness: 100%
   Clarity: 4.2/5
   Coverage: Balanced
   📄 Report: test-cases/reports/quality-review-2026-04-15.md

⚠️  3 test cases have minor clarity improvements suggested.
   Would you like to review recommendations before proceeding?
   [Yes] [No - Continue]

User: No - Continue

📋 Step 5/6: Automation feasibility assessment...
🔄 Analyzing automation potential...
✅ Feasibility Assessment Complete

   Automation Recommendations:
   ⭐⭐⭐ Phase 1 (Quick Wins): 6 tests - ROI 4.2:1
      UI_PART_001, UI_PART_002, UI_PART_005, UI_PART_006, UI_PART_013, UI_PART_014

   ⭐⭐ Phase 2 (High Value): 8 tests - ROI 2.8:1
      UI_PART_003, UI_PART_004, UI_PART_007, UI_PART_010, UI_PART_011,
      UI_PART_015, UI_PART_016, UI_PART_017

   ⭐ Phase 3 (Backlog): 3 tests - ROI 1.2:1
      UI_PART_008, UI_PART_009, UI_PART_012

   ❌ Not Recommended: 1 test
      UI_PART_018 (ROI 0.3:1)

   📄 Report: test-cases/reports/automation-feasibility-2026-04-15.md

Which tests would you like to automate?
[Phase 1 Only (Recommended)] [Phase 1 + 2] [Custom Selection] [Skip Automation]

User: Phase 1 Only

📋 Step 6/6: Automating tests...
🔄 Automating 6 test cases from Phase 1...

   [1/6] UI_PART_001 - Create part with required fields
         ├─ Reading test case... ✅
         ├─ Playwright inspection... ✅
         ├─ Updating Page Objects... ✅
         ├─ Generating test method... ✅
         ├─ Compilation check... ✅
         └─ Creating PR... ✅ PR #42

   [2/6] UI_PART_002 - Create part with all fields
         ├─ Reading test case... ✅
         ├─ Playwright inspection... ✅
         ├─ Updating Page Objects... ✅
         ├─ Generating test method... ✅
         ├─ Compilation check... ✅
         └─ Creating PR... ✅ PR #43

   ... (4 more)

✅ Automation Complete!

┌─────────────────────────────────────────────────────────────┐
│                    Workflow Summary                         │
└─────────────────────────────────────────────────────────────┘

📊 Test Generation:
   ✅ 18 UI test cases created
   ✅ Quality score: 87/100
   ✅ All test types covered

🤖 Automation:
   ✅ 6 test cases automated (Phase 1 Quick Wins)
   ✅ 6 Pull Requests created
   ⏱️  Total time: 2.5 hours (estimated)
   💰 Expected annual savings: 312 hours
   📈 Average ROI: 4.2:1

📄 Reports Generated:
   - test-cases/ui/ui-manual-tests.csv
   - test-cases/ui/ui-manual-tests.md
   - test-cases/reports/quality-review-2026-04-15.md
   - test-cases/reports/automation-feasibility-2026-04-15.md
   - .claude/workflow-state/workflows/workflow-2026-04-15-143022.json

🔗 Pull Requests:
   #42: Automate UI_PART_001
   #43: Automate UI_PART_002
   #44: Automate UI_PART_005
   #45: Automate UI_PART_006
   #46: Automate UI_PART_013
   #47: Automate UI_PART_014

📋 Next Steps:
   1. Review and merge PRs (#42-#47)
   2. Consider automating Phase 2 tests (8 tests, ROI 2.8:1)
   3. Run automated tests in CI/CD
   4. Generate test cases for other functional areas

Workflow complete! 🎉
```

---

### Example 2: Assessment Only

```bash
User: /qa-orchestrator assess ui

Agent:
┌─────────────────────────────────────────────────────────────┐
│         QA Orchestrator - Assessment Mode                   │
└─────────────────────────────────────────────────────────────┘

📊 Analyzing existing UI test cases...

✅ Quality Review Complete
   File: test-cases/ui/ui-manual-tests.csv
   Test Cases: 18
   Overall Score: 87/100 ✅

   Issues Found:
   - 3 test cases need minor clarity improvements
   - All test cases have complete fields
   - Coverage is well balanced

✅ Feasibility Assessment Complete

   Automation Priority:
   ⭐⭐⭐ 6 High Priority (Phase 1)
   ⭐⭐ 8 Medium Priority (Phase 2)
   ⭐ 3 Low Priority (Phase 3)
   ❌ 1 Not Recommended

   Total Estimated Effort: 84 hours
   Expected Annual Savings: 312 hours
   Average ROI: 3.2:1

📋 Recommendations:

1. Quality Improvements:
   → Fix clarity issues in UI_PART_005, UI_PART_010, UI_PART_016

2. Automation Priority:
   → Start with Phase 1 (6 tests): 24h effort, 4.2:1 ROI
   → Quick wins with high business value

3. Next Steps:
   → Run: /qa-orchestrator automate ui UI_PART_001-UI_PART_006

Reports saved to test-cases/reports/
```

---

## Benefits of Orchestration

### 1. **Single Entry Point**
- One command to run entire workflow
- No need to remember multiple commands
- Consistent interface

### 2. **Intelligent Workflow**
- Automatic quality checks after generation
- Automatic feasibility assessment before automation
- Smart recommendations based on scores

### 3. **State Management**
- Track workflow progress
- Resume from checkpoints
- Audit trail of all actions

### 4. **Batch Operations**
- Automate multiple test cases in one command
- Parallel execution where possible
- Consolidated reporting

### 5. **Error Recovery**
- Graceful error handling
- Option to skip, retry, or abort
- State saved for recovery

### 6. **Reporting**
- Comprehensive workflow summaries
- Combined quality + feasibility reports
- Executive summaries for stakeholders

---

## Advanced Features

### Automation Queue Management

```bash
# Add tests to automation queue
/qa-orchestrator queue add ui UI_PART_001,UI_PART_002,UI_PART_003

# View automation queue
/qa-orchestrator queue list

# Process automation queue
/qa-orchestrator queue process

# Clear automation queue
/qa-orchestrator queue clear
```

### Workflow Templates

Save and reuse workflow configurations:

```bash
# Save current workflow as template
/qa-orchestrator template save "standard-ui-workflow"

# List templates
/qa-orchestrator template list

# Run workflow from template
/qa-orchestrator template run "standard-ui-workflow" part-creation
```

### Batch Generation

Generate test cases for multiple functional areas:

```bash
# Generate for all UI areas
/qa-orchestrator batch-generate ui all

# Generate for specific areas
/qa-orchestrator batch-generate api part-crud,part-list,part-search
```

---

## Integration with CI/CD

The orchestrator can be invoked in CI/CD pipelines:

```yaml
# .github/workflows/qa-automation.yml
- name: Generate and automate UI tests
  run: |
    claude-code /qa-orchestrator full-workflow ui part-creation --non-interactive
```

**Non-interactive mode:**
- Uses default settings from orchestrator.json
- No user prompts
- Returns exit code 0 on success, 1 on failure
- Outputs JSON summary for parsing

---

## Future Enhancements

- [ ] Integration with test management tools (TestRail, Xray)
- [ ] Automatic test execution after automation
- [ ] Test result aggregation and reporting
- [ ] ML-based test case prioritization
- [ ] Auto-healing for failed automated tests
- [ ] Integration with Jira for requirement traceability

---

**Created:** 2026-04-15
**Version:** 1.0.0
**Maintained by:** QA Automation Team
