# QA Hub AI Hackathon 2026

## Project
QA test generation and automation for **InvenTree Parts module** — an open-source inventory management system (Python/Django).

## Application Under Test

- **Demo URL:** https://demo.inventree.org
- **API Base URL:** https://demo.inventree.org/api/
- **Demo resets daily.**

### Credentials

| Account | Password | Access Level |
|---------|----------|-------------|
| admin | inventree | Superuser — full access |
| allaccess | nolimits | Full CRUD |
| engineer | partsonly | Parts & stock only |
| reader | readonly | View-only |

## Documentation Sources

- **UI Requirements:** https://docs.inventree.org/en/stable/part/ (and sub-pages)
- **API Schema:** https://docs.inventree.org/en/stable/api/schema/part/

## Output Conventions

- UI test cases: `test-cases/ui/`
- API test cases: `test-cases/api/`
- UI Test Case ID format: `UI_PART_XXX`
- API Test Case ID format: `API_PART_XXX`
- Append to existing files (continue ID sequence) when they exist
