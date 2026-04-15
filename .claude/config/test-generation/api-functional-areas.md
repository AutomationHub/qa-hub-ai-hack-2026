# API Functional Areas — InvenTree Parts Module

## Area Definitions

### 1. parts-crud
- **Description:** CRUD operations on Parts — POST, GET, PUT, PATCH, DELETE /api/part/
- **Min Tests:** 15
- **Key Coverage:** Create, read single, read list, full update, partial update, delete
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/
  - https://docs.inventree.org/en/stable/api/

### 2. categories-crud
- **Description:** CRUD operations on Part Categories — /api/part/category/
- **Min Tests:** 10
- **Key Coverage:** Create, nested create, list, tree, update, delete with/without children
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/
  - https://docs.inventree.org/en/stable/api/

### 3. filtering-pagination
- **Description:** Filtering, pagination, search, ordering on list endpoints
- **Min Tests:** 10
- **Key Coverage:** search, ordering, limit/offset, category filter, boolean filters, combo filters, boundary pages
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/
  - https://docs.inventree.org/en/stable/api/

### 4. field-validation
- **Description:** Field-level validation — required fields, max lengths, nullable, read-only, defaults, data types
- **Min Tests:** 12
- **Key Coverage:** Required fields missing, max length exceeded, wrong types, nulls, read-only writes, boolean defaults
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/

### 5. relational-integrity
- **Description:** Relational integrity — category assignment, default locations, supplier linkage, BOM, parameters
- **Min Tests:** 10
- **Key Coverage:** Valid/invalid category IDs, location IDs, supplier IDs, BOM parent-child, parameter templates
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/
  - https://docs.inventree.org/en/stable/api/schema/

### 6. auth-permissions
- **Description:** Authentication and role-based authorisation
- **Min Tests:** 8
- **Key Coverage:** No auth, bad token, reader on write ops, engineer scope limits, admin full access
- **Docs:**
  - https://docs.inventree.org/en/stable/api/

### 7. edge-cases
- **Description:** Edge cases and error handling
- **Min Tests:** 10
- **Key Coverage:** Bad JSON, empty body, 404, duplicate IPN, stock deletion, long values, unicode, injection
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/

### 8. response-schema
- **Description:** Response structure validation
- **Min Tests:** 8
- **Key Coverage:** All fields present, correct types, nested objects, list pagination metadata
- **Docs:**
  - https://docs.inventree.org/en/stable/api/schema/part/

## Menu Text

Use this when `$ARGUMENTS` is empty:

```
Available functional areas for /api-test-generator:

  1. parts-crud           — CRUD on Parts (POST, GET, PUT, PATCH, DELETE /api/part/)
  2. categories-crud      — CRUD on Part Categories (/api/part/category/)
  3. filtering-pagination — Filtering, pagination, search, ordering
  4. field-validation     — Required fields, max lengths, nullable, read-only, defaults
  5. relational-integrity — Category assignment, locations, suppliers, BOM, parameters
  6. auth-permissions     — Auth (no auth, bad token) and role-based access
  7. edge-cases           — Invalid payloads, 404s, duplicate IPN, injection, unicode
  8. response-schema      — Response structure, data types, nested objects, pagination
  9. all                  — Generate for ALL areas

Usage: /api-test-generator parts-crud
```
