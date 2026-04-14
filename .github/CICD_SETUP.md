# CI/CD Setup Guide

Quick guide to enable and configure GitHub Actions for automated testing.

## Prerequisites

- ✅ GitHub repository created
- ✅ Code pushed to repository
- ✅ GitHub Actions enabled (enabled by default)

## Quick Setup

### Step 1: Verify Workflows Exist

Workflows are already in `.github/workflows/`:
- `test-automation.yml` - Main test suite
- `pr-tests.yml` - Pull request validation
- `nightly-tests.yml` - Scheduled regression tests

### Step 2: Push to GitHub

```bash
git add .github/
git commit -m "Add GitHub Actions workflows for automated testing"
git push origin main
```

### Step 3: Verify Workflows Run

1. Go to your repository on GitHub
2. Click **Actions** tab
3. You should see workflows running

### Step 4: (Optional) Configure Secrets

If you need custom credentials:

1. Go to repository **Settings**
2. Click **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add secrets (e.g., `INVENTREE_API_TOKEN`)

## Workflow Overview

### 1. Main Test Automation (`test-automation.yml`)

**Triggers:**
- Push to `main` branch
- Pull requests to `main`
- Manual trigger

**What it runs:**
- All API tests (smoke + regression)
- All UI tests (smoke + regression)

**Duration:** ~20-30 minutes

**First run will:**
1. Set up Java 17
2. Download Maven dependencies (cached for future runs)
3. Install Chrome browser
4. Run all tests
5. Generate Allure reports
6. Upload artifacts

### 2. PR Tests (`pr-tests.yml`)

**Triggers:**
- When PR is opened/updated

**What it runs:**
- Smoke tests only (fast)
- Code compilation check

**Duration:** ~10-15 minutes

**Automatically comments on PR with results!**

### 3. Nightly Tests (`nightly-tests.yml`)

**Triggers:**
- Every day at 2 AM UTC
- Manual trigger

**What it runs:**
- Full regression suite
- API and UI tests in parallel

**Duration:** ~40-45 minutes

**Creates GitHub issue if tests fail!**

## Viewing Test Results

### In GitHub Actions UI

1. Go to **Actions** tab
2. Click on a workflow run
3. View summary and logs

### Test Summary

Each run shows:
```
## 🧪 Test Automation Summary
Commit: abc1234
Branch: main

## 🚀 API Tests
Total: 11 | Passed: ✅ 10 | Failed: ❌ 1

## 🎭 UI Tests
Total: 3 | Passed: ✅ 3 | Failed: ❌ 0
```

### Download Artifacts

Scroll to **Artifacts** section in workflow run:
- `api-test-results` - API test reports
- `ui-test-results` - UI test reports
- `api-allure-report` - Allure HTML report (API)
- `ui-allure-report` - Allure HTML report (UI)

### View Allure Report

```bash
# Download artifact
unzip api-allure-report.zip
cd api-allure-report

# Serve locally
python -m http.server 8080
# Open http://localhost:8080
```

## Manual Workflow Trigger

### Via GitHub UI

1. Go to **Actions** tab
2. Select workflow (e.g., "Test Automation")
3. Click **Run workflow** dropdown
4. Select branch
5. Click green **Run workflow** button

### Via GitHub CLI

```bash
# Trigger main test automation
gh workflow run test-automation.yml

# Trigger on specific branch
gh workflow run test-automation.yml --ref feature-branch

# Trigger nightly tests manually
gh workflow run nightly-tests.yml
```

## Status Badges

Add to your README.md:

```markdown
![Test Automation](https://github.com/USERNAME/REPO/actions/workflows/test-automation.yml/badge.svg)
![PR Tests](https://github.com/USERNAME/REPO/actions/workflows/pr-tests.yml/badge.svg)
![Nightly Tests](https://github.com/USERNAME/REPO/actions/workflows/nightly-tests.yml/badge.svg)
```

Replace `USERNAME` and `REPO` with your GitHub username and repository name.

## Customization

### Change Schedule

Edit `.github/workflows/nightly-tests.yml`:
```yaml
schedule:
  # Run at 2 AM UTC
  - cron: '0 2 * * *'

  # Run at 6 PM EST (11 PM UTC)
  # - cron: '0 23 * * *'

  # Run twice daily (6 AM and 6 PM UTC)
  # - cron: '0 6,18 * * *'
```

### Change Test Groups

Edit workflow files:
```yaml
# Run only smoke tests
- run: mvn test -Dgroups=smoke

# Run smoke + regression
- run: mvn test -Dgroups=smoke,regression

# Run all tests
- run: mvn test
```

### Change Timeout

Edit workflow files:
```yaml
jobs:
  api-tests:
    timeout-minutes: 30  # Increase from default 20
```

### Add Slack Notifications

Add to workflow (requires Slack webhook secret):
```yaml
- name: Slack Notification
  if: failure()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

### Run on Specific Branches

Edit workflow trigger:
```yaml
on:
  push:
    branches:
      - main
      - develop
      - release/*
```

## Troubleshooting

### Workflow Not Running

**Check:**
1. Workflow file is in `.github/workflows/`
2. File has `.yml` or `.yaml` extension
3. YAML syntax is valid
4. GitHub Actions is enabled in repository settings

### Tests Fail in CI but Pass Locally

**Common causes:**
1. Different environment (Chrome version, Java version)
2. Timing issues (increase timeouts)
3. Headless mode differences
4. Demo server availability

**Solutions:**
```yaml
# Add retry logic
- name: Run Tests
  uses: nick-invision/retry@v2
  with:
    timeout_minutes: 15
    max_attempts: 3
    command: mvn test
```

### Out of GitHub Actions Minutes

**Check usage:**
1. Go to repository **Settings**
2. Click **Billing and plans**
3. View Actions minutes used

**Optimize:**
- Run full suite only on main branch
- Run smoke tests on PRs
- Reduce nightly test frequency
- Use matrix strategy for parallel execution

## Cost Estimates

### Public Repository
- **GitHub Actions minutes:** Unlimited ✅

### Private Repository
- **Free tier:** 2,000 minutes/month
- **Estimated usage:**
  - 20 PRs × 15 min = 300 min
  - 20 main commits × 30 min = 600 min
  - 30 nightly runs × 45 min = 1,350 min
  - **Total:** ~2,250 min/month (~$0.50 overage)

### GitHub Packages (Artifacts)
- **Free tier:** 500 MB storage
- **Estimated usage:** ~100 MB/month with 30-day retention

## Best Practices

✅ **Cache Maven dependencies** - Saves ~2-3 minutes per run
✅ **Use test groups** - Run smoke on PR, full on main
✅ **Set timeouts** - Prevent hung workflows
✅ **Retain artifacts appropriately** - Balance cost vs. need
✅ **Monitor flaky tests** - Fix unreliable tests
✅ **Review nightly failures** - Check issues daily

## Next Steps

1. ✅ Push workflows to GitHub
2. ✅ Verify first run succeeds
3. ✅ Add status badges to README
4. ✅ Configure Slack notifications (optional)
5. ✅ Set up branch protection rules
6. ✅ Monitor test results daily

## Support

- **GitHub Actions docs:** https://docs.github.com/actions
- **Workflow syntax:** https://docs.github.com/actions/reference/workflow-syntax-for-github-actions
- **Actions marketplace:** https://github.com/marketplace?type=actions

---

**Ready to go!** Just push to GitHub and watch your tests run automatically. 🚀
