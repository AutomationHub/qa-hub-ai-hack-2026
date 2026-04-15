---
skill: assess-automation-feasibility
description: Evaluates test cases for automation potential, complexity, ROI, and provides prioritized automation roadmap
args: test-file-path
triggers:
  - "assess automation feasibility"
  - "automation feasibility"
  - "which tests to automate"
  - "automation priority"
  - "automation roadmap"
---

# Automation Feasibility Assessment Skill

Analyzes manual test cases to determine automation potential, complexity, ROI, and generates a prioritized automation roadmap.

## Usage

```
/assess-automation-feasibility <test-file-path>
```

Examples:
```
/assess-automation-feasibility test-cases/ui/ui-manual-tests.csv
/assess-automation-feasibility test-cases/api/api-manual-tests.md
```

## Feasibility Criteria

### 1. Automation Suitability (0-10 scale)

**High Suitability (8-10)**: Excellent automation candidates
- Repetitive tests executed frequently
- Stable functionality with few changes
- Clear, deterministic steps
- Well-defined pass/fail criteria
- No complex human judgment required

**Medium Suitability (5-7)**: Good candidates with some challenges
- Moderately complex workflows
- Some dynamic elements or timing dependencies
- Occasional UI changes expected
- Mostly deterministic outcomes

**Low Suitability (0-4)**: Poor automation candidates
- Highly exploratory or ad-hoc testing
- Requires significant human judgment (visual design review)
- Constantly changing functionality
- One-time tests
- Complex setup with high maintenance cost

### 2. Technical Complexity (0-10 scale)

**Low Complexity (0-3)**:
- Simple form fills and button clicks
- Straightforward navigation (2-5 steps)
- Standard web elements (input, button, select)
- No file uploads, downloads, or third-party integrations
- Single-page interaction

**Medium Complexity (4-6)**:
- Multi-step workflows (6-10 steps)
- Dynamic content (AJAX, SPAs)
- Simple waits and synchronization
- File uploads/downloads
- Dropdown and autocomplete interactions
- Tab/window switching

**High Complexity (7-10)**:
- Long workflows (>10 steps)
- Complex dynamic elements (drag-drop, canvas)
- Third-party authentication flows
- iframes or shadow DOM
- Date pickers, rich text editors
- Browser-specific behavior
- Complex timing and synchronization

### 3. Data Setup Complexity (0-10 scale)

**Low (0-3)**:
- No preconditions or minimal setup
- Uses existing test data
- Demo environment resets daily (no cleanup needed)
- Independent test execution

**Medium (4-6)**:
- Requires specific test data creation
- Some API setup possible
- Needs cleanup after execution
- Moderate dependencies

**High (7-10)**:
- Complex multi-step data setup
- Cross-module dependencies
- Database manipulation required
- State management across tests
- Complex cleanup requirements

### 4. Maintenance Effort (0-10 scale)

**Low Maintenance (0-3)**:
- Stable selectors (IDs, data-test attributes)
- Mature, rarely changing UI
- Simple assertions
- Good framework support (POM pattern)

**Medium Maintenance (4-6)**:
- Some dynamic selectors (classes, relative paths)
- Occasional UI updates
- Multiple environments to maintain
- Moderate assertion logic

**High Maintenance (7-10)**:
- Fragile selectors (complex XPath)
- Frequently changing UI
- Hard-coded waits
- Complex conditional logic
- Multi-environment configuration

### 5. Business Value / ROI (0-10 scale)

**High Value (8-10)**:
- Critical user workflows (login, checkout, payments)
- High execution frequency (regression suite, smoke tests)
- Time-consuming manual execution (>15 min)
- High risk of regression bugs
- Customer-facing functionality

**Medium Value (5-7)**:
- Important but not critical features
- Moderate execution frequency
- Medium manual execution time (5-15 min)
- Moderate business impact

**Low Value (0-4)**:
- Edge cases or rarely used features
- Infrequent execution
- Quick manual execution (<5 min)
- Low business impact
- One-time tests

## Workflow

### Step 1 — Parse Arguments & Load Test File
1. Extract the test file path from arguments
2. Determine test type (UI or API) from file path or content
3. Read and parse test cases
4. Count total test cases

### Step 2 — Analyze Each Test Case

For each test case, evaluate:

#### A. Automation Suitability Analysis
- **Repetition**: Is this test executed regularly? (smoke, regression, daily)
- **Stability**: Is the feature stable or frequently changing?
- **Determinism**: Are outcomes predictable and verifiable programmatically?
- **Human Judgment**: Does it require visual assessment or complex decision-making?

Assign **Suitability Score (0-10)**

#### B. Technical Complexity Analysis
- **Step Count**: Number of test steps
- **Element Types**: Standard vs complex UI elements
- **Workflow Type**: Linear vs branching logic
- **Third-party Integration**: External services, OAuth, payment gateways
- **Synchronization**: Dynamic content, AJAX calls, loading states
- **File Operations**: Uploads, downloads, file validation

Assign **Complexity Score (0-10)**

#### C. Data Setup Analysis
- **Preconditions**: Number and complexity of preconditions
- **API Setup**: Can data be created via API?
- **State Management**: Cross-test dependencies
- **Cleanup**: Post-execution cleanup requirements

Assign **Data Setup Score (0-10)**

#### D. Maintenance Effort Analysis
- **Selector Stability**: ID/name vs dynamic classes/XPath
- **UI Change Frequency**: Based on functional area
- **Framework Support**: POM pattern, reusable components
- **Assertions**: Simple vs complex validation logic

Assign **Maintenance Score (0-10)**

#### E. Business Value Analysis
- **Execution Frequency**: Daily/weekly/monthly/one-time
- **Manual Time**: Minutes required for manual execution
- **Business Criticality**: P1/P2/P3/P4 priority mapping
- **Risk Impact**: What happens if this functionality breaks?

Assign **Business Value Score (0-10)**

#### F. Calculate Overall Automation Score

```
Automation Score = (
  (Suitability × 0.30) +
  (Business Value × 0.25) +
  ((10 - Complexity) × 0.20) +
  ((10 - Data Setup) × 0.15) +
  ((10 - Maintenance) × 0.10)
)

Range: 0-10
```

**Interpretation**:
- **8-10**: Must Automate (High Priority)
- **6-7.9**: Should Automate (Medium Priority)
- **4-5.9**: Consider Automating (Low Priority)
- **0-3.9**: Not Recommended for Automation

#### G. Estimate Effort

**Initial Implementation Effort** (hours):
```
Effort = Base_Time + (Complexity_Score × 0.5) + (Data_Setup_Score × 0.3)

Base_Time:
- UI Simple test: 2 hours
- UI Medium test: 4 hours
- UI Complex test: 8 hours
- API Simple test: 1 hour
- API Medium test: 2 hours
- API Complex test: 4 hours
```

**Maintenance Effort** (hours per year):
```
Annual_Maintenance = Maintenance_Score × Execution_Frequency_Factor

Execution_Frequency_Factor:
- Daily (smoke): 12 hours/year
- Weekly (regression): 8 hours/year
- Monthly: 4 hours/year
- Quarterly: 2 hours/year
```

**Time Saved** (hours per year):
```
Time_Saved = Manual_Execution_Time × Execution_Count_Per_Year
```

**ROI** (value ratio):
```
ROI = (Time_Saved - Annual_Maintenance) / Initial_Implementation_Effort

ROI > 3: Excellent ROI
ROI 1-3: Good ROI
ROI 0.5-1: Marginal ROI
ROI < 0.5: Negative ROI (not recommended)
```

### Step 3 — Generate Automation Roadmap

Prioritize test cases into automation phases:

**Phase 1 - Quick Wins** (Automate First):
- High automation score (≥ 8)
- High ROI (≥ 3)
- Low to medium complexity
- Critical business value (P1 tests)

**Phase 2 - High Value** (Automate Second):
- Medium to high automation score (6-7.9)
- Good ROI (1-3)
- Any complexity
- Important features (P1-P2 tests)

**Phase 3 - Backlog** (Automate Later):
- Medium automation score (4-5.9)
- Marginal ROI (0.5-1)
- Lower priority features (P2-P3 tests)

**Not Recommended**:
- Low automation score (<4)
- Negative ROI (<0.5)
- One-time or exploratory tests
- Extremely high maintenance risk

### Step 4 — Generate Assessment Report

Create a comprehensive Markdown report:

```markdown
# Automation Feasibility Assessment Report

**Test File**: {file_path}
**Assessment Date**: {current_date}
**Total Test Cases Analyzed**: {count}
**Test Type**: {UI/API}

---

## Executive Summary

### Automation Potential
- **Recommended for Automation**: {count} test cases ({percentage}%)
- **Not Recommended**: {count} test cases ({percentage}%)
- **Estimated Total Effort**: {hours} hours
- **Estimated Annual Time Savings**: {hours} hours
- **Average ROI**: {ratio}:1

### Prioritization
| Phase | Test Cases | Avg Score | Total Effort | Expected ROI |
|-------|------------|-----------|--------------|--------------|
| Phase 1 - Quick Wins | {count} | {avg_score} | {hours}h | {roi} |
| Phase 2 - High Value | {count} | {avg_score} | {hours}h | {roi} |
| Phase 3 - Backlog | {count} | {avg_score} | {hours}h | {roi} |
| Not Recommended | {count} | {avg_score} | — | — |

---

## Detailed Test Case Analysis

### Phase 1 — Quick Wins (Automate Immediately)

#### {Test Case ID}: {Test Scenario}
**Priority**: {P1/P2/P3}
**Automation Score**: {score}/10 ⭐⭐⭐⭐⭐

**Scores Breakdown**:
- Suitability: {score}/10 — {reason}
- Business Value: {score}/10 — {reason}
- Technical Complexity: {score}/10 — {assessment}
- Data Setup: {score}/10 — {assessment}
- Maintenance Effort: {score}/10 — {assessment}

**Effort Estimate**:
- Initial Implementation: {hours} hours
- Annual Maintenance: {hours} hours
- Annual Time Saved: {hours} hours
- **ROI**: {ratio}:1 ✅

**Automation Approach**:
- Framework: {Selenium/REST Assured}
- Test Type: {Positive/Negative/etc}
- Page Objects Needed: {list}
- Data Setup: {approach}
- Challenges: {if any}

**Recommendations**:
1. {Specific recommendation}
2. {Specific recommendation}

---

### Phase 2 — High Value (Automate Next)

{Similar detailed analysis}

---

### Phase 3 — Backlog (Automate Later)

{Similar detailed analysis}

---

### Not Recommended for Automation

#### {Test Case ID}: {Test Scenario}
**Automation Score**: {score}/10

**Why Not Recommended**:
- {Primary reason}
- {Secondary reason}
- **ROI**: {ratio}:1 ❌

**Alternative Approach**:
- Keep as manual test
- Consider for exploratory testing
- {Other suggestions}

---

## Automation Roadmap

### Sprint 1 (Week 1-2): Foundation & Quick Wins
**Goal**: Set up framework, automate highest ROI tests

**Test Cases** ({count}):
- {Test Case ID}: {Test Scenario} — {effort}h
- {Test Case ID}: {Test Scenario} — {effort}h

**Total Effort**: {hours} hours
**Deliverables**:
- {List of deliverables}

### Sprint 2 (Week 3-4): High Value Tests
{Similar structure}

### Sprint 3 (Week 5-6): Medium Priority
{Similar structure}

---

## Technical Considerations

### Framework Readiness
- ✅ Selenium WebDriver with POM pattern in place
- ✅ REST Assured for API testing configured
- ⚠️ Playwright MCP available for element inspection
- ❌ CI/CD integration not yet configured

### Infrastructure Needs
- [ ] {Infrastructure requirement}
- [ ] {Infrastructure requirement}

### Skills Required
- Selenium WebDriver (Java)
- Page Object Model pattern
- TestNG test framework
- REST Assured (for API tests)
- Allure reporting

---

## Risk Assessment

### High Risk Items
1. **{Risk}**: {Description}
   - Mitigation: {Strategy}
   - Impact: High/Medium/Low

### Medium Risk Items
{Similar structure}

---

## Cost-Benefit Analysis

### Investment Required
| Category | Hours | Cost (@ $50/hr) |
|----------|-------|-----------------|
| Initial Development | {hours} | ${cost} |
| Framework Enhancement | {hours} | ${cost} |
| CI/CD Setup | {hours} | ${cost} |
| **Total Initial Investment** | **{hours}** | **${cost}** |

### Expected Returns (Annual)
| Category | Hours Saved | Value (@ $50/hr) |
|----------|-------------|------------------|
| Regression Testing | {hours} | ${value} |
| Smoke Testing | {hours} | ${value} |
| Bug Detection (early) | {hours} | ${value} |
| **Total Annual Return** | **{hours}** | **${value}** |

### Break-Even Analysis
- **Payback Period**: {months} months
- **3-Year Net Benefit**: ${value}

---

## Recommendations

### Immediate Actions
1. **Start with Phase 1 Quick Wins**: {count} test cases, {hours} hours
2. **Set up CI/CD integration**: {recommendation}
3. **Establish test data strategy**: {recommendation}

### Best Practices
1. {Best practice}
2. {Best practice}
3. {Best practice}

### Success Metrics
Track these KPIs:
- Test execution time reduction
- Test coverage percentage
- Bug detection rate (automated vs manual)
- Flaky test rate
- Maintenance time per test

---

## Appendix: All Test Cases Summary

| ID | Scenario | Score | Phase | Effort | ROI | Status |
|----|----------|-------|-------|--------|-----|--------|
| {ID} | {name} | {score} | {phase} | {hours}h | {roi} | ✅/⚠️/❌ |

---

## Conclusion

{Summary with key recommendations and next steps}
```

### Step 5 — Save Report
Save the assessment report to:
- `test-cases/reports/automation-feasibility-{timestamp}.md`

### Step 6 — Display Summary
Show concise summary to user:

```
🤖 Automation Feasibility Assessment Complete

File: test-cases/ui/ui-manual-tests.csv
Total Test Cases: 18

Automation Recommendations:
✅ Phase 1 (Quick Wins): 6 test cases — 24 hours effort, 4.5:1 ROI
⭐ Phase 2 (High Value): 8 test cases — 42 hours effort, 2.8:1 ROI
📋 Phase 3 (Backlog): 3 test cases — 18 hours effort, 1.2:1 ROI
❌ Not Recommended: 1 test case

Total Investment: 84 hours
Annual Time Savings: 312 hours
Average ROI: 3.2:1 ✅

📄 Detailed roadmap saved to: test-cases/reports/automation-feasibility-2026-04-15.md

💡 Recommendation: Start with UI_PART_001, UI_PART_002, UI_PART_005 (highest ROI)
```

## Special Considerations

### UI vs API Tests
- **API tests** typically have higher automation scores (simpler, more stable)
- **UI tests** have higher complexity but also higher business value
- Adjust complexity scoring based on test type

### Framework Maturity
Consider existing framework capabilities:
- Page Objects already implemented? Lower effort.
- Reusable utilities available? Lower complexity.
- CI/CD pipeline ready? Better ROI.

### Environment Stability
- **Demo environment** (resets daily): Easier data setup, no cleanup
- **Stable test environment**: More complex data management
- **Production-like environment**: Higher confidence, higher setup cost

---

**Note**: This assessment provides data-driven recommendations. Final automation decisions should consider team capacity, project timeline, and strategic priorities.
