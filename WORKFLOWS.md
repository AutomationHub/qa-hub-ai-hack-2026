# QA Hub - Complete SDLC Workflows

Two approaches to execute the complete QA testing workflow: **Orchestrated** (single command) or **Manual** (step-by-step).

---

## Workflow 1: Orchestrated (Automated)

### UI Testing

```bash
/qa-orchestrator full-workflow ui part-creation
```

**Flow Diagram:**

```
┌─────────────────────────────────────────────────────────────────────┐
│                    USER INITIATES WORKFLOW                          │
│            /qa-orchestrator full-workflow ui part-creation          │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    QA ORCHESTRATOR AGENT                            │
│                  (Master Control & State Management)                │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                    PHASE 1: TEST GENERATION                         ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  Spawn: ui-test-generator agent               │
                │  → Load functional area config                │
                │  → Spawn requirements-analyzer                │
                │  → Generate 12+ test cases                    │
                │  → Auto-invoke /review-test-quality           │
                │  → Save to test-cases/ui/                     │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: generation    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║               PHASE 2: FEASIBILITY ASSESSMENT                       ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  Invoke: /assess-automation-feasibility       │
                │  → Score each test (0-10)                     │
                │  → Calculate ROI                              │
                │  → Prioritize Phase 1/2/3                     │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: assessment    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                 PHASE 3: TEST AUTOMATION                            ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  For each Phase 1 test:                       │
                │  Spawn: ui-test-automator agent               │
                │  → Read test case                             │
                │  → Create git branch                          │
                │  → Playwright element inspection              │
                │  → Update Page Objects                        │
                │  → Generate test method                       │
                │  → Compile & verify                           │
                │  → Commit & create PR                         │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: automation    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                         COMPLETION                                  ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────┐
                │  Display Summary:             │
                │  - Tests generated            │
                │  - Quality score              │
                │  - Tests automated            │
                │  - PRs created                │
                └───────────────────────────────┘
```

---

### API Testing

```bash
/qa-orchestrator full-workflow api part-crud
```

**Flow Diagram:**

```
┌─────────────────────────────────────────────────────────────────────┐
│                    USER INITIATES WORKFLOW                          │
│            /qa-orchestrator full-workflow api part-crud             │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    QA ORCHESTRATOR AGENT                            │
│                  (Master Control & State Management)                │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                PHASE 1: API TEST GENERATION                         ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  Spawn: api-test-generator agent              │
                │  → Load API functional area config            │
                │  → Spawn requirements-analyzer (API schema)   │
                │  → Extract endpoints, request/response schema │
                │  → Generate 15+ API test cases                │
                │  → Auto-invoke /review-test-quality           │
                │  → Save to test-cases/api/                    │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: generation    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║           PHASE 2: API AUTOMATION FEASIBILITY                       ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  Invoke: /assess-automation-feasibility       │
                │  → API suitability scoring                    │
                │  → Request/response complexity                │
                │  → Prioritize Phase 1/2/3                     │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: assessment    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                 PHASE 3: API TEST AUTOMATION                        ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────────────────────┐
                │  For each Phase 1 API test:                   │
                │  Spawn: api-test-automator agent              │
                │  → Read API test case                         │
                │  → Create git branch                          │
                │  → Analyze live API response                  │
                │  → Generate POJOs (request/response DTOs)     │
                │  → Generate REST Assured test method          │
                │  → Compile & verify                           │
                │  → Commit & create PR                         │
                └───────────────┬───────────────────────────────┘
                                │
                ┌───────────────▼───────────────┐
                │  ✅ Checkpoint: automation    │
                └───────────────┬───────────────┘
                                │
╔═════════════════════════════════════════════════════════════════════╗
║                         COMPLETION                                  ║
╚═════════════════════════════════════════════════════════════════════╝
                                │
                ┌───────────────▼───────────────┐
                │  Display Summary:             │
                │  - API tests generated        │
                │  - Endpoints covered          │
                │  - Tests automated            │
                │  - POJOs created              │
                │  - PRs created                │
                └───────────────────────────────┘
```

---

## Workflow 2: Manual (Step-by-Step)

### UI Testing

**Step 1: Generate Tests**
```bash
/generate-ui-test part-creation
```

**Flow:**
```
USER COMMAND: /generate-ui-test part-creation
       ↓
Command Wrapper (parse & validate)
       ↓
ui-test-generator Agent
  → Load config
  → Spawn requirements-analyzer
  → Generate 12+ test cases
  → Auto-invoke /review-test-quality
  → Save to test-cases/ui/
       ↓
OUTPUT: Test cases + Quality report
```

**Step 2: Assess Feasibility**
```bash
/assess-automation-feasibility test-cases/ui/ui-manual-tests.csv
```

**Flow:**
```
USER COMMAND: /assess-automation-feasibility
       ↓
Skill Execution
  → Read test cases
  → Score each test (0-10)
  → Multi-factor analysis
  → Prioritize Phase 1/2/3
       ↓
OUTPUT: Feasibility roadmap
```

**Step 3: Automate Tests**
```bash
/automate-ui-test UI_PART_001
/automate-ui-test UI_PART_002
/automate-ui-test UI_PART_005
# [Repeat for each Phase 1 test]
```

**Flow:**
```
USER COMMAND: /automate-ui-test UI_PART_001
       ↓
Command Wrapper (parse & validate)
       ↓
ui-test-automator Agent
  → Read test case
  → Auto-invoke feasibility check
  → Create git branch
  → Playwright element inspection
  → Update Page Objects
  → Generate test method
  → Compile with Maven
  → Commit & create PR
       ↓
OUTPUT: Branch + PR URL
```

---

### API Testing

**Step 1: Generate Tests**
```bash
/generate-api-test part-crud
```

**Flow:**
```
USER COMMAND: /generate-api-test part-crud
       ↓
Command Wrapper (parse & validate)
       ↓
api-test-generator Agent
  → Load API config
  → Spawn requirements-analyzer (API schema)
  → Extract endpoints & schemas
  → Generate 15+ API test cases
  → Auto-invoke /review-test-quality
  → Save to test-cases/api/
       ↓
OUTPUT: API test cases + Quality report
```

**Step 2: Assess Feasibility**
```bash
/assess-automation-feasibility test-cases/api/api-manual-tests.csv
```

**Flow:**
```
USER COMMAND: /assess-automation-feasibility
       ↓
Skill Execution (API-specific)
  → Read API test cases
  → Score complexity
  → Analyze POJO requirements
  → Prioritize Phase 1/2/3
       ↓
OUTPUT: API feasibility roadmap
```

**Step 3: Automate Tests**
```bash
/automate-api-test API_PART_001
/automate-api-test API_PART_002
/automate-api-test API_PART_003
# [Repeat for each Phase 1 test]
```

**Flow:**
```
USER COMMAND: /automate-api-test API_PART_001
       ↓
Command Wrapper (parse & validate)
       ↓
api-test-automator Agent
  → Read API test case
  → Auto-invoke feasibility check
  → Create git branch
  → Make live API call (schema discovery)
  → Generate POJOs (if needed)
  → Generate REST Assured test
  → Compile with Maven
  → Commit & create PR
       ↓
OUTPUT: Branch + PR URL + POJOs
```

---

## Quick Reference

### Orchestrated Commands

```bash
# UI workflow
/qa-orchestrator full-workflow ui part-creation

# API workflow
/qa-orchestrator full-workflow api part-crud

# Partial workflows
/qa-orchestrator generate ui part-creation     # Generate only
/qa-orchestrator assess ui                     # Assess only
/qa-orchestrator automate ui UI_PART_001       # Automate only
```

### Manual Commands

```bash
# UI workflow
/generate-ui-test part-creation
/assess-automation-feasibility test-cases/ui/ui-manual-tests.csv
/automate-ui-test UI_PART_001

# API workflow
/generate-api-test part-crud
/assess-automation-feasibility test-cases/api/api-manual-tests.csv
/automate-api-test API_PART_001
```

### Run Automated Tests

```bash
# UI tests
cd automation/ui
mvn clean test -Dtest=PartCreationTest
mvn allure:serve

# API tests
cd automation/api
mvn clean test -Dtest=PartCrudTest
mvn allure:serve

# All tests
cd automation
mvn clean test
mvn allure:serve
```

---

## Output Locations

| Item | Path |
|------|------|
| UI Test Cases | `test-cases/ui/ui-manual-tests.csv` |
| API Test Cases | `test-cases/api/api-manual-tests.csv` |
| Quality Reports | `test-cases/reports/quality-review-*.md` |
| Feasibility Reports | `test-cases/reports/automation-feasibility-*.md` |
| Workflow State | `.claude/workflow-state/*.json` |
| UI Automation Code | `automation/ui/src/test/java/` |
| API Automation Code | `automation/api/src/test/java/` |
