---
skill: review-test-quality
description: Analyzes test cases for quality, completeness, clarity, and adherence to best practices
args: test-file-path
triggers:
  - "review test quality"
  - "check test quality"
  - "analyze test cases"
  - "audit test cases"
---

# Test Quality Review Skill

Performs comprehensive quality analysis of manual or automated test cases to ensure they meet professional QA standards.

## Usage

```
/review-test-quality <test-file-path>
```

Examples:
```
/review-test-quality test-cases/ui/ui-manual-tests.csv
/review-test-quality test-cases/ui/ui-manual-tests.md
/review-test-quality test-cases/api/api-manual-tests.csv
```

## Quality Criteria

### 1. Completeness (Critical)
- **Test Case ID**: Unique, follows naming convention (UI_PART_XXX or API_PART_XXX)
- **Module/Functional Area**: Clearly identified
- **Priority**: P1/P2/P3/P4 assigned appropriately
- **Test Scenario**: Brief, descriptive title
- **Description**: Clear explanation of what's being validated
- **Preconditions**: All setup requirements documented
- **Test Steps**: Numbered, actionable steps
- **Expected Results**: Observable, verifiable outcomes
- **Test Type**: Classified (Positive/Negative/Boundary/Permission)

### 2. Clarity & Executability (Critical)
- **Unambiguous Steps**: No vague terms like "check", "verify" without specifics
- **Specific UI Elements**: References buttons, fields, tabs by exact name
- **Concrete Values**: Uses actual test data, not placeholders
- **Navigation Path**: Clear path from preconditions to test execution
- **Assertions**: Expected results are measurable (not "system works correctly")
- **Error Messages**: Exact expected error text when validation fails

### 3. Best Practices (Important)
- **Independence**: Test can run without dependency on other tests
- **Repeatability**: Can be executed multiple times with same result
- **Test Data**: Uses unique identifiers (timestamps, UUIDs) to avoid conflicts
- **Cleanup**: Considers cleanup needs (or notes daily demo reset)
- **Security**: No hardcoded sensitive data in test steps
- **Cross-browser/Platform**: Notes any platform-specific considerations

### 4. Coverage Analysis (Important)
- **Scenario Distribution**:
  - Positive tests: Happy path scenarios (30-40%)
  - Negative tests: Error handling, validation (30-40%)
  - Boundary tests: Edge cases, limits (15-25%)
  - Permission tests: Access control (10-15%)
- **Requirement Traceability**: Links to requirements or documentation
- **Risk-Based Priority**: P1 for critical workflows, P2 for important features, P3 for edge cases

### 5. Automation Readiness (Nice to Have)
- **Stable Identifiers**: References UI elements that likely have IDs/names
- **Deterministic**: No time-dependent or randomly failing conditions
- **Data Isolation**: Test data strategy supports automation
- **API Setup**: Can preconditions be automated via API?

## Workflow

### Step 1 — Parse Arguments & Load Test File
1. Extract the test file path from arguments
2. Read the test file (CSV or Markdown format)
3. Parse test cases into structured format
4. Count total test cases found

### Step 2 — Analyze Each Test Case
For each test case, evaluate:

**Completeness Check** (Pass/Fail for each field):
- [ ] Test Case ID present and follows convention
- [ ] Module/functional area identified
- [ ] Priority assigned (P1/P2/P3/P4)
- [ ] Test Scenario descriptive
- [ ] Description explains purpose
- [ ] Preconditions documented
- [ ] Test Steps numbered and present
- [ ] Expected Results defined
- [ ] Test Type classified

**Clarity Score** (1-5 scale):
- 5: Perfectly clear, executable by anyone
- 4: Clear with minor ambiguities
- 3: Mostly clear, some vague terms
- 2: Multiple ambiguous areas
- 1: Difficult to execute without clarification

**Issues Identified**:
- Vague language (e.g., "verify it works", "check the page")
- Missing specific UI element names
- Placeholder data instead of concrete values
- Unclear expected results
- Missing preconditions
- Logical gaps in test flow

### Step 3 — Coverage Analysis
Analyze the test suite as a whole:
- **Test Type Distribution**: Count and percentage of each type
- **Priority Distribution**: Count by priority level
- **Coverage Gaps**: Identify missing scenarios
- **Functional Coverage**: Map to requirements (if available)

### Step 4 — Generate Quality Report

Output a comprehensive report in Markdown format:

```markdown
# Test Quality Review Report

**Test File**: {file_path}
**Review Date**: {current_date}
**Total Test Cases**: {count}
**Overall Quality Score**: {score}/100

---

## Executive Summary

- **Completeness**: {percentage}% of test cases have all required fields
- **Clarity**: Average clarity score {avg_score}/5
- **Critical Issues**: {count} test cases need immediate attention
- **Warnings**: {count} test cases have minor issues
- **Recommendations**: {count} improvement suggestions

---

## Test Case Analysis

### ✅ High Quality Test Cases ({count})
Test cases that meet all quality criteria:
- {Test Case ID}: {Brief reason}

### ⚠️ Test Cases Needing Improvement ({count})

#### {Test Case ID}: {Test Scenario}
**Priority**: {priority}
**Completeness**: {pass_count}/{total_fields} fields complete
**Clarity Score**: {score}/5

**Issues**:
- [ ] {Issue description}
- [ ] {Issue description}

**Recommendations**:
1. {Specific recommendation}
2. {Specific recommendation}

---

## Coverage Analysis

### Test Type Distribution
| Type | Count | Percentage | Target | Status |
|------|-------|------------|--------|--------|
| Positive | {count} | {pct}% | 30-40% | ✅/⚠️/❌ |
| Negative | {count} | {pct}% | 30-40% | ✅/⚠️/❌ |
| Boundary | {count} | {pct}% | 15-25% | ✅/⚠️/❌ |
| Permission | {count} | {pct}% | 10-15% | ✅/⚠️/❌ |

### Priority Distribution
| Priority | Count | Percentage | Typical Range |
|----------|-------|------------|---------------|
| P1 | {count} | {pct}% | 20-30% |
| P2 | {count} | {pct}% | 40-50% |
| P3 | {count} | {pct}% | 20-30% |
| P4 | {count} | {pct}% | 5-10% |

---

## Recommendations

### Critical Actions (Fix Immediately)
1. {Specific action with test case IDs}
2. {Specific action with test case IDs}

### Improvements (Address Soon)
1. {Improvement suggestion}
2. {Improvement suggestion}

### Best Practices (Consider)
1. {Best practice suggestion}
2. {Best practice suggestion}

---

## Quality Metrics

### Completeness Metrics
- Test cases with all required fields: {count}/{total} ({pct}%)
- Average fields per test case: {avg}
- Test cases missing critical fields: {count}

### Clarity Metrics
- Test cases with clarity score ≥ 4: {count}/{total} ({pct}%)
- Test cases needing clarity improvement (score < 3): {count}
- Common clarity issues: {list}

### Traceability Metrics
- Test cases with requirement links: {count}/{total}
- Test cases with documentation references: {count}/{total}

---

## Detailed Findings

{Detailed analysis for each test case or each issue category}

---

## Conclusion

{Summary paragraph with overall assessment and key next steps}
```

### Step 5 — Save Report
Save the quality review report to:
- `test-cases/reports/quality-review-{timestamp}.md`

### Step 6 — Summary
Display a concise summary to the user with:
- Overall quality score
- Number of critical issues
- Top 3 recommendations
- Link to detailed report

## Output Format

Present results clearly:
```
📊 Test Quality Review Complete

File: test-cases/ui/ui-manual-tests.csv
Total Test Cases: 18
Overall Quality Score: 87/100 ✅

Summary:
✅ Completeness: 100% (18/18 test cases have all required fields)
⚠️  Clarity: 4.2/5 average (3 test cases need minor improvements)
✅ Coverage: Well balanced across test types

Critical Issues: 0
Warnings: 3
Recommendations: 5

📄 Detailed report saved to: test-cases/reports/quality-review-2026-04-15.md
```

## Quality Scoring Formula

**Overall Score (0-100)**:
- Completeness: 40 points (all required fields present)
- Clarity: 30 points (average clarity score × 6)
- Coverage: 20 points (balanced test type distribution)
- Best Practices: 10 points (traceability, independence, data strategy)

**Thresholds**:
- 90-100: Excellent ✅
- 75-89: Good ⚠️ (minor improvements needed)
- 60-74: Fair ⚠️ (several improvements needed)
- <60: Poor ❌ (significant rework required)

## Special Cases

### Empty or Missing File
If file doesn't exist or has no test cases:
```
❌ Error: No test cases found in {file_path}

Please check:
- File path is correct
- File contains test cases in expected format (CSV or Markdown)
- File is not empty
```

### Multiple Formats
If both CSV and MD files exist, review both and compare for consistency.

### API vs UI Test Cases
Adjust criteria slightly for API tests:
- UI-specific criteria (button names, navigation) → API endpoints, payloads
- Focus on request/response validation
- Emphasize status codes and error responses

---

**Note**: This skill provides analysis only. It does not modify test cases. Use the report to manually improve test quality or create follow-up tasks.
