# GitHub Actions Workflows

This directory contains CI/CD workflows for automated testing of the InvenTree QA Hub project.

## Available Workflows

### 1. `test-automation.yml` - Main Test Suite
**Triggers:**
- Push to `main` branch
- Pull requests to `main`
- Manual dispatch

**What it does:**
- Runs full API test suite (smoke + regression)
- Runs full UI test suite (smoke + regression)
- Generates Allure reports
- Uploads test results and logs as artifacts
- Creates test summary in GitHub Actions

**Duration:** ~20-30 minutes

**Jobs:**
1. **API Tests** - Runs all API tests using REST Assured
2. **UI Tests** - Runs all UI tests using Selenium (headless Chrome)
3. **Test Summary** - Combines results and checks overall status

### 2. `pr-tests.yml` - Pull Request Validation
**Triggers:**
- Pull requests opened, synchronized, or reopened

**What it does:**
- Runs only smoke tests (fast feedback)
- Checks code compilation
- Comments PR with test results
- Faster than full regression suite

**Duration:** ~10-15 minutes

**Jobs:**
1. **Smoke Tests** - Runs critical test cases only
2. **Code Quality** - Verifies frameworks compile

**Why use this:**
- Faster feedback on PRs
- Catches major issues before merge
- Reduces CI costs

### 3. `nightly-tests.yml` - Scheduled Regression
**Triggers:**
- Scheduled: Daily at 2 AM UTC
- Manual dispatch

**What it does:**
- Runs full regression suite
- Tests run in parallel (API + UI)
- Creates GitHub issue on failure
- Generates trend reports
- Long-term result retention (30 days)

**Duration:** ~40-45 minutes

**Jobs:**
1. **Regression Tests** - Matrix strategy (API + UI in parallel)
2. **Notify** - Creates issue if tests fail
3. **Trend Report** - Tracks test stability over time

**Why use this:**
- Catches issues early (before next day's work)
- Comprehensive coverage without blocking development
- Historical trend tracking

## Workflow Triggers Summary

| Workflow | Push to main | Pull Request | Schedule | Manual |
|----------|-------------|--------------|----------|--------|
| `test-automation.yml` | ✅ | ✅ | ❌ | ✅ |
| `pr-tests.yml` | ❌ | ✅ | ❌ | ❌ |
| `nightly-tests.yml` | ❌ | ❌ | ✅ (2 AM UTC) | ✅ |

## Artifacts Generated

### Test Results
- **Surefire Reports** - TestNG XML results
- **Allure Results** - Raw Allure data
- **Logs** - Test execution logs
- **Screenshots** - UI test failure screenshots (UI tests only)

### Reports
- **Allure Reports** - HTML test reports
- **Trend Reports** - Historical data (nightly only)

### Retention
- PR tests: 14 days
- Main branch tests: 30 days
- Nightly tests: 30 days

## Test Groups

Tests are organized by TestNG groups:

| Group | Description | Included In |
|-------|-------------|-------------|
| `smoke` | Critical functionality | PR tests, Full suite |
| `regression` | Comprehensive coverage | Main branch, Nightly |
| `negative` | Error handling tests | Main branch, Nightly |

## Environment Configuration

### Java & Maven
- **Java Version:** 17 (Temurin distribution)
- **Maven Cache:** Enabled for faster builds
- **Maven Options:** `-Xmx1024m`

### Browsers (UI Tests)
- **Chrome:** Stable version via `browser-actions/setup-chrome`
- **Headless Mode:** Enabled (`-Dheadless=true`)

### Test Configuration
Tests use default configuration from:
- `automation/api/src/test/resources/config.properties`
- `automation/ui/src/test/resources/config.properties`

**Override in CI:**
```yaml
- name: Run Tests with Custom Config
  run: mvn test -Dbase.url=https://staging.example.com -Dheadless=true
```

## Viewing Test Results

### GitHub Actions UI
1. Go to **Actions** tab
2. Select workflow run
3. Click on job (API Tests, UI Tests)
4. View logs and summary

### Test Summary
Each workflow run includes a summary with:
- Total tests executed
- Pass/fail counts
- Links to artifacts

### Download Artifacts
1. Go to workflow run
2. Scroll to **Artifacts** section
3. Download:
   - `api-test-results` - API test data
   - `ui-test-results` - UI test data
   - `api-allure-report` - API Allure HTML report
   - `ui-allure-report` - UI Allure HTML report

### View Allure Reports
```bash
# Download and extract allure-report artifact
cd api-allure-report
python -m http.server 8080
# Open http://localhost:8080 in browser
```

## Manual Workflow Trigger

Run workflows manually from GitHub:

1. Go to **Actions** tab
2. Select workflow (e.g., "Test Automation")
3. Click **Run workflow**
4. Select branch
5. Click **Run workflow** button

Or using GitHub CLI:
```bash
# Trigger main test automation
gh workflow run test-automation.yml

# Trigger nightly tests
gh workflow run nightly-tests.yml
```

## Status Badges

Add to README.md:

```markdown
![Test Automation](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/test-automation.yml/badge.svg)
![PR Tests](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/pr-tests.yml/badge.svg)
![Nightly Tests](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/nightly-tests.yml/badge.svg)
```

## Workflow Success Criteria

### test-automation.yml
✅ All API tests pass
✅ All UI tests pass
✅ Allure reports generated
✅ Artifacts uploaded

### pr-tests.yml
✅ Smoke tests pass
✅ Code compiles
✅ PR commented with results

### nightly-tests.yml
✅ Regression suite completes
✅ Results uploaded
✅ Issue created if failed

## Troubleshooting

### Tests Fail in CI but Pass Locally

**Common causes:**
1. **Environment differences** - Check Chrome version, Java version
2. **Timing issues** - Increase timeouts in CI
3. **Headless mode** - Some UI behaviors differ in headless
4. **Network issues** - Demo server might be slow/down

**Solutions:**
```yaml
# Add retry logic
- name: Run Tests with Retry
  uses: nick-invision/retry@v2
  with:
    timeout_minutes: 15
    max_attempts: 3
    command: mvn test
```

### Workflow Timeout

**Increase timeout:**
```yaml
jobs:
  my-job:
    timeout-minutes: 45  # Default is 360 (6 hours)
```

### Maven Dependency Issues

**Clear cache:**
```yaml
- name: Clear Maven Cache
  run: rm -rf ~/.m2/repository
```

### Chrome Installation Fails

**Use different Chrome setup:**
```yaml
- name: Install Chrome
  run: |
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
    sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
    sudo apt-get update
    sudo apt-get install -y google-chrome-stable
```

## Advanced Configuration

### Parallel Execution

Run API and UI tests in parallel:
```yaml
strategy:
  matrix:
    test-suite: [api, ui]
```

### Conditional Execution

Run only changed tests:
```yaml
- name: Run Changed Tests
  if: contains(github.event.head_commit.message, '[api]')
  run: mvn test -pl automation/api
```

### Slack Notifications

Add Slack notification on failure:
```yaml
- name: Slack Notification
  if: failure()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Tests failed!'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

### Deploy Allure Report to GitHub Pages

```yaml
- name: Deploy Allure Report
  uses: peaceiris/actions-gh-pages@v3
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: automation/api/target/site/allure-maven-plugin
```

## Best Practices

✅ **Use caching** - Cache Maven dependencies
✅ **Fail fast** - Use `fail-fast: false` for parallel jobs
✅ **Timeout limits** - Set reasonable timeouts
✅ **Artifact retention** - Balance cost vs. retention needs
✅ **Test grouping** - Use TestNG groups for selective execution
✅ **Clear naming** - Name jobs and steps clearly
✅ **Error handling** - Use `continue-on-error` appropriately
✅ **Notifications** - Alert team on failures

## Cost Optimization

### GitHub Actions Minutes
- **Public repos:** Unlimited minutes
- **Private repos:** 2,000 minutes/month (free tier)

### Optimize Usage:
1. Run full suite only on main branch
2. Run smoke tests on PRs
3. Use matrix strategy for parallel execution
4. Cache Maven dependencies
5. Set appropriate timeouts

### Estimated Minutes Per Run:
- PR Tests: ~15 minutes
- Full Suite: ~30 minutes
- Nightly: ~45 minutes

**Monthly estimate (private repo):**
- 20 PRs × 15 min = 300 min
- 20 main commits × 30 min = 600 min
- 30 nightly runs × 45 min = 1,350 min
- **Total:** ~2,250 minutes/month

## Security

### Secrets Management
Store sensitive data in GitHub Secrets:
```yaml
env:
  API_TOKEN: ${{ secrets.INVENTREE_API_TOKEN }}
```

### Permissions
Workflows use default `GITHUB_TOKEN` with:
- Read access to repository
- Write access to checks and statuses
- Write access to pull requests (for comments)

## Maintenance

### Update Dependencies
Keep actions up to date:
```yaml
uses: actions/checkout@v4  # Check for newer versions
uses: actions/setup-java@v4
uses: actions/upload-artifact@v4
```

### Monitor Failures
- Review nightly test issues
- Check for flaky tests
- Update timeouts as needed
- Adjust test groups

### Review Artifacts
- Periodically check artifact storage
- Delete old artifacts if needed
- Adjust retention periods

## Support

- **Workflow issues:** Check Actions tab logs
- **Test failures:** Download artifacts and review Allure reports
- **Configuration:** See framework READMEs
- **GitHub Actions docs:** https://docs.github.com/actions

---

**Created:** 2026-04-14
**Frameworks:** Selenium 4.27.0 + REST Assured 5.5.0
**CI/CD:** GitHub Actions
