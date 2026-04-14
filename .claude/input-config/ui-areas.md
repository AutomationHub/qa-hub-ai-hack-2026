# UI Functional Areas — InvenTree Parts Module

## Area Definitions

### 1. part-creation
- **Description:** Part creation — manual entry, required vs optional fields, default values, import flows
- **Min Tests:** 12
- **Docs:**
  - https://docs.inventree.org/en/stable/part/create/
  - https://docs.inventree.org/en/stable/part/part/

### 2. part-detail-view
- **Description:** Part detail view tabs — Stock, BOM, Allocated, Build Orders, Parameters, Variants, Revisions, Attachments, Related Parts, Test Templates
- **Min Tests:** 15
- **Docs:**
  - https://docs.inventree.org/en/stable/part/views/
  - https://docs.inventree.org/en/stable/part/part/

### 3. part-categories
- **Description:** Category hierarchy navigation, create/edit/delete categories, filtering, parametric tables
- **Min Tests:** 10
- **Docs:**
  - https://docs.inventree.org/en/stable/part/part/

### 4. part-attributes
- **Description:** Virtual, Template, Assembly, Component, Trackable, Purchaseable, Salable, Active/Inactive toggle behaviour
- **Min Tests:** 10
- **Docs:**
  - https://docs.inventree.org/en/stable/part/part/
  - https://docs.inventree.org/en/stable/part/trackable/

### 5. units-of-measure
- **Description:** UoM configuration, assignment to parts, conversion display
- **Min Tests:** 6
- **Docs:**
  - https://docs.inventree.org/en/stable/part/part/

### 6. part-parameters
- **Description:** Add/edit/delete parameters, parameter templates, value validation
- **Min Tests:** 8
- **Docs:**
  - https://docs.inventree.org/en/stable/part/parameters/

### 7. templates-variants
- **Description:** Create template, create variant from template, variant inheritance
- **Min Tests:** 10
- **Docs:**
  - https://docs.inventree.org/en/stable/part/template/

### 8. part-revisions
- **Description:** Create revision, constraints (circular reference prevention, unique codes, template restrictions, revision-of-revision prevention)
- **Min Tests:** 10
- **Docs:**
  - https://docs.inventree.org/en/stable/part/revision/

### 9. search-filtering
- **Description:** Global search, category filter, attribute filter, parametric table filtering
- **Min Tests:** 8
- **Docs:**
  - https://docs.inventree.org/en/stable/part/views/
  - https://docs.inventree.org/en/stable/part/part/

### 10. attachments-related
- **Description:** Upload/link/remove attachments, related parts management
- **Min Tests:** 6
- **Docs:**
  - https://docs.inventree.org/en/stable/part/views/
  - https://docs.inventree.org/en/stable/part/notification/

## Menu Text

Use this when `$ARGUMENTS` is empty:

```
Available functional areas for /ui-test-generator:

  1. part-creation        — Part creation (manual entry, fields, defaults, import)
  2. part-detail-view     — Detail view tabs (Stock, BOM, Build Orders, Parameters, etc.)
  3. part-categories      — Category hierarchy, create/edit/delete, filtering
  4. part-attributes      — Virtual, Template, Assembly, Trackable, Purchaseable, etc.
  5. units-of-measure     — UoM config, assignment, conversion display
  6. part-parameters      — Parameters, templates, value validation
  7. templates-variants   — Templates, variants, inheritance
  8. part-revisions       — Revisions, constraints, circular ref prevention
  9. search-filtering     — Search, filters, parametric tables
  10. attachments-related — Attachments, related parts
  11. all                 — Generate for ALL areas

Usage: /ui-test-generator part-creation
```
