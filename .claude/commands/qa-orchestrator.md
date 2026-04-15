---
skill: qa-orchestrator
description: Master orchestrator for complete QA workflow - test generation, quality review, feasibility assessment, and automation
args: subcommand arguments
triggers:
  - "qa orchestrator"
  - "orchestrate qa"
  - "qa workflow"
  - "full qa workflow"
---

# QA Orchestrator Command

**Quick start for the master QA orchestrator agent.**

## Usage

```bash
# Full end-to-end workflow
/qa-orchestrator full-workflow ui part-creation

# Generate tests only
/qa-orchestrator generate ui part-creation

# Automate specific tests
/qa-orchestrator automate ui UI_PART_001

# Assess existing tests
/qa-orchestrator assess ui

# Interactive guided mode
/qa-orchestrator interactive
```

## What This Does

The orchestrator provides **centralized control** over your entire QA workflow:

1. **full-workflow** - Complete SDLC from requirements to automation
2. **generate** - Test case generation + automatic quality review
3. **automate** - Test automation + automatic feasibility check
4. **assess** - Combined quality + feasibility assessment
5. **interactive** - Guided workflow with prompts

## Implementation

This command invokes the `qa-orchestrator` agent defined in `.claude/agents/qa-orchestrator.md`.

The agent will:
- Parse your command and arguments
- Orchestrate the appropriate workflow
- Invoke specialized agents/commands as needed
- Provide progress updates and summaries
- Generate comprehensive reports

## Workflow

### Step 1 — Parse Arguments

Extract subcommand and arguments from `$ARGUMENTS`:
- Subcommand: `full-workflow`, `generate`, `automate`, `assess`, `interactive`
- Test type: `ui` or `api`
- Additional arguments (functional area, test IDs, etc.)

### Step 2 — Invoke Orchestrator Agent

Use the Agent tool to spawn the qa-orchestrator agent:

```
Agent(
  subagent_type: "general-purpose",
  description: "QA workflow orchestration",
  prompt: """
    You are the QA Orchestrator agent.

    Command: {subcommand}
    Test Type: {test_type}
    Arguments: {additional_args}

    Execute the workflow as defined in .claude/agents/qa-orchestrator.md

    Follow these steps:
    {steps based on subcommand}

    Provide progress updates and comprehensive summaries.
  """
)
```

### Step 3 — Display Results

The orchestrator agent will handle all execution and reporting. This command wrapper simply:
- Validates arguments
- Invokes the agent
- Displays the agent's output

## Examples

### Example 1: Full Workflow

```bash
/qa-orchestrator full-workflow ui part-creation

# This will:
# 1. Analyze requirements from InvenTree docs
# 2. Generate 12+ UI test cases
# 3. Run quality review (automatic)
# 4. Run feasibility assessment (automatic)
# 5. Ask which tests to automate
# 6. Automate approved tests
# 7. Create PRs
# 8. Generate workflow summary
```

### Example 2: Generate Tests Only

```bash
/qa-orchestrator generate api part-crud

# This will:
# 1. Analyze API schema
# 2. Generate 15+ API test cases
# 3. Run quality review (automatic)
# 4. Display results and recommendations
```

### Example 3: Automate Multiple Tests

```bash
/qa-orchestrator automate ui UI_PART_001-UI_PART_006

# This will:
# 1. Run feasibility assessment for tests 001-006
# 2. Show ROI and complexity scores
# 3. Ask for confirmation
# 4. Automate approved tests in parallel
# 5. Create PRs for each
# 6. Generate automation summary
```

### Example 4: Assessment

```bash
/qa-orchestrator assess ui

# This will:
# 1. Run quality review on existing test cases
# 2. Run feasibility assessment
# 3. Generate combined analysis
# 4. Provide recommendations:
#    - Tests needing quality improvements
#    - High-priority automation candidates
#    - Tests to keep manual
```

### Example 5: Interactive Mode

```bash
/qa-orchestrator interactive

# This will:
# 1. Ask what you want to do
# 2. Guide you through options
# 3. Execute selected workflow
# 4. Ask for next action
```

## Configuration

The orchestrator reads configurations from:
- `.claude/config/test-generation/` - Functional areas
- `.claude/config/test-automation/` - Framework configs
- `.claude/agents/qa-orchestrator.md` - Orchestrator definition

## Advanced Usage

### Batch Operations

```bash
# Generate for multiple areas
/qa-orchestrator batch-generate ui all

# Automate comma-separated list
/qa-orchestrator automate ui UI_PART_001,UI_PART_005,UI_PART_010
```

### Queue Management

```bash
# Add to automation queue
/qa-orchestrator queue add ui UI_PART_001,UI_PART_002

# Process queue
/qa-orchestrator queue process

# View queue
/qa-orchestrator queue list
```

### Templates

```bash
# Save workflow template
/qa-orchestrator template save "my-workflow"

# Run from template
/qa-orchestrator template run "my-workflow" part-creation
```

## Benefits

### vs. Individual Commands

**Before (manual coordination):**
```bash
/generate-ui-test part-creation
# ... wait ...
/review-test-quality test-cases/ui/ui-manual-tests.csv
# ... review results ...
/assess-automation-feasibility test-cases/ui/ui-manual-tests.csv
# ... check scores ...
/automate-ui-test UI_PART_001
# ... repeat for each test ...
```

**After (orchestrated):**
```bash
/qa-orchestrator full-workflow ui part-creation
# ... everything happens automatically with smart checkpoints ...
```

### Key Advantages

✅ **Single entry point** - One command for entire workflow
✅ **Automatic quality gates** - Quality review and feasibility checks built-in
✅ **Smart recommendations** - Data-driven automation decisions
✅ **Progress tracking** - Know where you are in the workflow
✅ **Error recovery** - Resume from checkpoints if interrupted
✅ **Batch operations** - Automate multiple tests in one command
✅ **Comprehensive reporting** - Workflow summaries and audit trails
✅ **State management** - Track what's done, what's pending

## Troubleshooting

### Orchestrator Not Found

**Error:** "Unknown skill: qa-orchestrator"

**Solution:** The orchestrator agent is defined in `.claude/agents/qa-orchestrator.md`. If missing, recreate it.

### Invalid Arguments

**Error:** "Invalid subcommand"

**Solution:** Use one of the valid subcommands:
- `full-workflow`
- `generate`
- `automate`
- `assess`
- `interactive`

### Workflow Interrupted

**Solution:** The orchestrator saves state to `.claude/workflow-state/current-workflow.json`. You can resume with:
```bash
/qa-orchestrator resume
```

## See Also

- `.claude/agents/qa-orchestrator.md` - Full orchestrator documentation
- `.claude/commands/README-QA-WORKFLOW.md` - QA workflow integration guide
- `.claude/config/README.md` - Configuration documentation

---

**Note:** This is a wrapper command that invokes the qa-orchestrator agent. See the agent definition for complete workflow details.
