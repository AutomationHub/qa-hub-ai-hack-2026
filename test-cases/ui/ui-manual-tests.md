# UI Manual Test Cases - InvenTree Parts Module

**Generated:** 2026-04-14
**Module:** InvenTree Parts - part-creation
**Total Test Cases:** 18
**Application Under Test:** https://demo.inventree.org

---

## Test Case Summary

| Priority | Count | Test Types |
|----------|-------|------------|
| P1 | 6 | Critical functionality and validation |
| P2 | 8 | Important features and edge cases |
| P3 | 4 | Additional validation and boundary tests |

| Test Type | Count |
|-----------|-------|
| Positive | 7 |
| Negative | 6 |
| Boundary | 4 |
| Permission | 2 |

---

## Test Cases

### UI_PART_001: Create part with required fields only
**Module:** part-creation
**Priority:** P1
**Test Type:** Positive

**Test Scenario:** Verify that a part can be created with only the mandatory Name field populated

**Description:** Validate the minimal path to create a part using only required fields to ensure the system accepts parts with default/empty optional fields.

**Preconditions:**
1. User is logged in as 'admin' or 'allaccess'
2. Navigate to Parts section
3. Demo instance is accessible at https://demo.inventree.org

**Test Steps:**
1. Click the 'Create Part' or 'New Part' button from the parts list page
2. In the part creation form, enter 'Test Resistor 100K' in the 'Name' field (required)
3. Leave all other fields (IPN, Description, Category, etc.) blank or at default values
4. Ensure 'Active' attribute is checked (default)
5. Click 'Save' or 'Submit' button

**Expected Result:**
1. Part is created successfully
2. User is redirected to the newly created part's detail page
3. Part name 'Test Resistor 100K' is displayed in the header
4. Part has 'Active' status
5. All optional fields are empty or show default values
6. Success notification/toast message appears
7. Part is searchable in the parts list

---

### UI_PART_002: Create part with all fields populated
**Module:** part-creation
**Priority:** P1
**Test Type:** Positive

**Test Scenario:** Verify that a part can be created with all available fields (required and optional) populated with valid data

**Description:** Comprehensive validation that all part creation fields work correctly when fully populated with valid data.

**Preconditions:**
1. User is logged in as 'admin' or 'allaccess'
2. At least one category exists in the system
3. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Capacitor 10uF Ceramic' in Name field (required)
3. Enter 'CAP-10UF-001' in IPN field
4. Enter 'Ceramic capacitor, 10uF, 50V, X7R dielectric' in Description field
5. Select 'Electronics > Passive Components > Capacitors' from Category dropdown
6. Enter 'Rev A' in Revision field
7. Enter 'capacitor, ceramic, 10uF' in Keywords field
8. Enter 'https://example.com/datasheet/cap-10uf.pdf' in Link field
9. Select 'pcs' (pieces) from Units dropdown
10. Set Minimum Stock to '100'
11. Upload a valid image (JPG/PNG, <5MB)
12. Check 'Purchaseable' attribute
13. Click 'Save' button

**Expected Result:**
1. Part is created successfully with all provided data
2. User redirected to part detail page
3. All fields display entered values correctly
4. Image preview/thumbnail is visible
5. Part appears under selected category
6. IPN 'CAP-10UF-001' is unique and saved
7. 'Purchaseable' attribute shows as enabled
8. Success message confirms creation

---

### UI_PART_003: Create Assembly part with attributes
**Module:** part-creation
**Priority:** P2
**Test Type:** Positive

**Test Scenario:** Verify that an Assembly part can be created with multiple attributes enabled

**Description:** Validate creation of assembly parts with multiple attributes (Assembly, Component, Trackable, Salable) to ensure attribute combinations work correctly.

**Preconditions:**
1. User logged in with part creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'PCB Assembly - Main Board v2.1' in Name field
3. Enter 'ASM-PCB-MB-2.1' in IPN field
4. Enter 'Main board assembly with all components' in Description field
5. Select a category (e.g., 'Assemblies')
6. Check 'Assembly' attribute checkbox
7. Check 'Component' attribute checkbox (sub-assembly)
8. Check 'Trackable' attribute checkbox
9. Check 'Salable' attribute checkbox
10. Set Minimum Stock to '10'
11. Click 'Save'

**Expected Result:**
1. Part created successfully
2. Part detail page shows all four attributes as enabled (Assembly, Component, Trackable, Salable)
3. Part has 'Active' status by default
4. Assembly-specific tabs/sections become available (e.g., BOM, Build Orders)
5. Part appears in relevant filtered views (assemblies, trackable items)
6. All attribute combinations are accepted without conflicts

---

### UI_PART_004: Create Virtual part for services
**Module:** part-creation
**Priority:** P2
**Test Type:** Positive

**Test Scenario:** Verify that a Virtual part can be created for non-physical items like software or services

**Description:** Test the creation of virtual parts (software, services) to ensure the Virtual attribute works correctly and appropriate features are enabled/disabled.

**Preconditions:**
1. User logged in as 'admin' or 'allaccess'
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Software License - CAD Pro Annual' in Name field
3. Enter 'SVC-CAD-LIC-001' in IPN field
4. Enter 'Annual subscription for CAD Pro software' in Description
5. Select category 'Services' or 'Software' (if available)
6. Check 'Virtual' attribute checkbox
7. Check 'Salable' attribute checkbox
8. Leave 'Trackable' unchecked (virtual parts typically not tracked physically)
9. Click 'Save'

**Expected Result:**
1. Part created successfully as Virtual part
2. 'Virtual' attribute is displayed prominently on part detail page
3. Stock-related features may be limited or hidden (as virtual parts don't have physical stock)
4. Part is marked as Salable and appears in sales workflows
5. No warnings about Virtual + Salable combination (valid combination)
6. Part can be used in orders and invoices

---

### UI_PART_005: Submit form with missing required field
**Module:** part-creation
**Priority:** P1
**Test Type:** Negative

**Test Scenario:** Verify that part creation fails when the required Name field is empty

**Description:** Validate that the system enforces required field validation and prevents part creation without the mandatory Name field.

**Preconditions:**
1. User logged in with part creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Leave the 'Name' field completely empty (or enter only whitespace)
3. Fill in optional fields like IPN 'TEST-001', Description 'Test part'
4. Attempt to click 'Save' button

**Expected Result:**
1. Form validation triggers before submission
2. Error message appears: 'Part name is required' or similar
3. Name field is highlighted in red or with error indicator
4. Submit button may be disabled, or form submission is blocked
5. No part is created in the system
6. Form data in other fields is preserved (no data loss)
7. Focus may move to the Name field
8. User remains on the part creation form

---

### UI_PART_006: Create part with duplicate IPN
**Module:** part-creation
**Priority:** P1
**Test Type:** Negative

**Test Scenario:** Verify that the system prevents creation of parts with duplicate Internal Part Numbers

**Description:** Test IPN uniqueness constraint to ensure no two parts can have the same Internal Part Number.

**Preconditions:**
1. User logged in as 'admin' or 'allaccess'
2. A part with IPN 'RES-100K-001' already exists in the system
3. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Resistor 100K 5%' in Name field
3. Enter 'RES-100K-001' in IPN field (exact duplicate of existing part)
4. Fill in other optional fields
5. Click 'Save' button

**Expected Result:**
1. Form submission fails with validation error
2. Error message displays: 'Part with this IPN already exists' or 'IPN must be unique'
3. IPN field is highlighted with error indicator
4. Error message may include a link to the existing part with same IPN
5. No duplicate part is created
6. User remains on creation form with all entered data preserved
7. User can modify IPN and retry submission

---

### UI_PART_007: Create part with duplicate IPN - case insensitive
**Module:** part-creation
**Priority:** P2
**Test Type:** Negative

**Test Scenario:** Verify that IPN uniqueness validation is case-insensitive

**Description:** Validate that the IPN uniqueness check treats uppercase and lowercase versions as duplicates.

**Preconditions:**
1. User logged in with creation permissions
2. A part with IPN 'CAP-001' exists in the system
3. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Test Capacitor' in Name field
3. Enter 'cap-001' in IPN field (lowercase version of existing 'CAP-001')
4. Click 'Save'

**Expected Result:**
1. Validation error occurs
2. Error message indicates IPN already exists (case-insensitive match)
3. System treats 'cap-001' and 'CAP-001' as duplicate
4. Part creation fails
5. Form data preserved for correction

---

### UI_PART_008: Create part with excessively long Name field
**Module:** part-creation
**Priority:** P2
**Test Type:** Boundary

**Test Scenario:** Verify field length validation for the Name field

**Description:** Test the maximum character limit for the Name field to ensure proper validation and error handling.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter a part name exceeding 250 characters (e.g., 'A' repeated 300 times)
3. Attempt to fill other fields
4. Click 'Save'

**Expected Result:**
1. Client-side validation may truncate input at 250 characters OR prevent further character entry
2. Character counter (if present) shows limit reached
3. If submission attempted with excess characters, server-side validation error appears
4. Error message: 'Maximum length is 250 characters' or similar
5. Part creation fails if validation error occurs
6. Name field is highlighted with error indicator
7. User must reduce character count to proceed

---

### UI_PART_009: Create part with special characters in Name
**Module:** part-creation
**Priority:** P3
**Test Type:** Boundary

**Test Scenario:** Verify that the Name field accepts special characters and Unicode

**Description:** Test that the Name field properly handles special characters, accented characters, and Unicode symbols.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Résistör 10KΩ ±5% (RoHS)' in Name field (includes accented characters, Greek symbol, special chars)
3. Enter 'RES-SPEC-001' in IPN field
4. Click 'Save'

**Expected Result:**
1. Part is created successfully
2. Name is stored exactly as entered, preserving all special characters and Unicode
3. Part detail page displays name correctly with all special characters: 'Résistör 10KΩ ±5% (RoHS)'
4. Part is searchable using special characters
5. No character sanitization that removes valid characters
6. Name appears correctly in parts list and search results

---

### UI_PART_010: Create part with minimum and maximum numeric values
**Module:** part-creation
**Priority:** P2
**Test Type:** Boundary

**Test Scenario:** Verify boundary values for Minimum Stock numeric field

**Description:** Test minimum (0), maximum, and invalid (negative) values for the Minimum Stock numeric field.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Test Part for Stock Min' in Name field
3. Enter '0' in Minimum Stock field (minimum boundary)
4. Click 'Save'
5. Verify part creation success
6. Create another part: 'Test Part for Stock Max'
7. Enter '999999999' (very large number) in Minimum Stock field
8. Click 'Save'
9. Create third part: 'Test Part Negative Stock'
10. Enter '-10' in Minimum Stock field
11. Attempt to save

**Expected Result:**
1. Part with Minimum Stock = 0 is created successfully (valid minimum)
2. Part with Minimum Stock = 999999999 is created if within system limits, or shows appropriate error if exceeds limit
3. Part with Minimum Stock = -10 fails validation
4. Error message: 'Minimum stock must be a non-negative number'
5. Negative value is rejected by client-side or server-side validation
6. Field highlights error for negative value

---

### UI_PART_011: Create part with multiple attribute combinations
**Module:** part-creation
**Priority:** P3
**Test Type:** Boundary

**Test Scenario:** Verify that valid attribute combinations are accepted (Assembly + Purchaseable + Salable)

**Description:** Test that multiple attributes can be enabled simultaneously and that valid combinations work correctly (buy vs build scenario).

**Preconditions:**
1. User logged in as 'admin' or 'allaccess'
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Multi-Attribute Test Part' in Name field
3. Enter 'MULTI-001' in IPN field
4. Check 'Assembly' attribute (can be built from BOM)
5. Check 'Purchaseable' attribute (can also be purchased pre-built)
6. Check 'Salable' attribute (can be sold to customers)
7. Check 'Trackable' attribute (requires serial/batch tracking)
8. Click 'Save'

**Expected Result:**
1. Part is created successfully with all four attributes enabled
2. No validation errors or warnings for this attribute combination
3. Part detail page shows all attributes as active
4. Assembly-specific features are available (BOM tab)
5. Purchaseable-specific features available (supplier pricing)
6. Salable-specific features available (customer pricing, sales orders)
7. Trackable features available (serial number assignment)
8. This represents a 'buy vs build' scenario (common in manufacturing)

---

### UI_PART_012: Create Template part with restricted attributes
**Module:** part-creation
**Priority:** P3
**Test Type:** Boundary

**Test Scenario:** Verify that Template parts have appropriate restrictions or warnings

**Description:** Test the Template attribute and verify any restrictions or special behavior for template parts.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Resistor Template' in Name field
3. Enter 'TPL-RES-001' in IPN field
4. Check 'Template' attribute checkbox
5. Attempt to check 'Component' attribute as well
6. Attempt to check 'Trackable' attribute
7. Enter description: 'Template for resistor variants'
8. Click 'Save'

**Expected Result:**
1. Part is created as Template
2. System may show warning or restriction messages for certain attribute combinations with Template
3. Template parts should not have stock items directly (they are used to create variants)
4. Part detail page indicates Template status prominently
5. 'Create Variant' option becomes available
6. Template part appears in template-specific views/filters
7. Template-specific constraints are enforced (may not appear in BOMs directly)

---

### UI_PART_013: Create part without any category assignment
**Module:** part-creation
**Priority:** P2
**Test Type:** Positive

**Test Scenario:** Verify that parts can be created without category assignment (uncategorized parts)

**Description:** Test that the Category field is truly optional and parts can exist without category assignment.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Uncategorized Test Part' in Name field
3. Enter 'UNCAT-001' in IPN field
4. Leave Category field blank/unselected
5. Fill in Description: 'Part without category for testing'
6. Click 'Save'

**Expected Result:**
1. Part is created successfully without category assignment
2. Part appears in 'Uncategorized' view or main parts list
3. Part detail page shows 'No Category' or blank category field
4. Part is fully functional (can be used in BOMs, orders, etc.)
5. Category can be assigned later via edit function
6. Part is searchable and accessible

---

### UI_PART_014: Create part as Reader role (unauthorized)
**Module:** part-creation
**Priority:** P1
**Test Type:** Permission

**Test Scenario:** Verify that users with read-only access cannot create parts

**Description:** Test permission enforcement to ensure read-only users cannot create parts.

**Preconditions:**
1. User is logged in as 'reader' account (read-only access)
2. Password: 'readonly'
3. Navigate to Parts section

**Test Steps:**
1. Observe parts list page - 'Create Part' button should be hidden or disabled
2. Attempt direct URL access to part creation form: https://demo.inventree.org/part/new/ (or equivalent)
3. If form is accessible, fill in Name 'Unauthorized Part' and attempt to save

**Expected Result:**
1. 'Create Part' button is not visible to reader role
2. Direct URL access either:
   a. Redirects to parts list with 'Permission Denied' message, OR
   b. Shows '403 Forbidden' error page
3. Error message: 'You do not have permission to create parts' or similar
4. No part is created in the system
5. User is informed to contact administrator for permissions
6. Appropriate HTTP status (403 Forbidden) is returned

---

### UI_PART_015: Create part as Engineer role
**Module:** part-creation
**Priority:** P2
**Test Type:** Permission

**Test Scenario:** Verify that Engineer role can create parts when permissions are granted

**Description:** Test that Engineer role with appropriate permissions can successfully create parts.

**Preconditions:**
1. User is logged in as 'engineer' account
2. Password: 'partsonly'
3. Engineer role has 'add_part' permission enabled
4. Navigate to Parts section

**Test Steps:**
1. Verify 'Create Part' button is visible and enabled
2. Click 'Create Part' button
3. Enter 'Engineer Created Part' in Name field
4. Enter 'ENG-TEST-001' in IPN field
5. Select category (if permissions allow)
6. Check relevant attributes (Purchaseable, Component)
7. Click 'Save'

**Expected Result:**
1. Part creation form is accessible
2. 'Create Part' button is visible
3. Form submission succeeds
4. Part is created successfully by Engineer user
5. Part appears in parts list
6. Created part shows 'engineer' as creator in audit log (if available)
7. Engineer can view and edit the created part (per role permissions)

---

### UI_PART_016: Create part with invalid URL in Link field
**Module:** part-creation
**Priority:** P3
**Test Type:** Negative

**Test Scenario:** Verify URL format validation for the Link field

**Description:** Test that the Link field properly validates URL format and rejects invalid URLs.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Test Part with Link' in Name field
3. Enter 'invalid-url-format' in Link field (not a valid URL)
4. Try to save
5. Correct to 'www.example.com' (missing protocol)
6. Try to save again
7. Correct to 'https://www.example.com/datasheet.pdf' (valid URL)
8. Save

**Expected Result:**
1. First attempt (invalid-url-format) shows validation error: 'Please enter a valid URL'
2. Second attempt (missing protocol) may show error or auto-correct to add https://
3. Link field is highlighted with error indicator for invalid formats
4. Third attempt with valid URL succeeds
5. Part is created with valid URL stored
6. URL is clickable and opens in new tab from part detail page

---

### UI_PART_017: Create part and verify default values
**Module:** part-creation
**Priority:** P3
**Test Type:** Positive

**Test Scenario:** Verify that default values are applied correctly to new parts

**Description:** Test that all fields have appropriate default values when creating a new part.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section

**Test Steps:**
1. Click 'Create Part' button
2. Observe default values in form fields before any input
3. Note that 'Active' checkbox is checked by default
4. Note that 'Virtual', 'Template', 'Assembly', etc. are unchecked by default
5. Note that Minimum Stock defaults to 0 or blank
6. Enter only Name: 'Default Values Test Part'
7. Click 'Save' without changing any other fields

**Expected Result:**
1. Part is created with defaults:
   - Active = True (checked)
   - Virtual = False (unchecked)
   - Template = False
   - Assembly = False
   - Component = False
   - Trackable = False
   - Purchaseable = False
   - Salable = False
   - Minimum Stock = 0 or blank
2. Part detail page reflects these default values
3. Part appears in active parts list (not hidden)
4. Part has sensible defaults for optional fields

---

### UI_PART_018: Cancel part creation and verify no data is saved
**Module:** part-creation
**Priority:** P2
**Test Type:** Negative

**Test Scenario:** Verify that cancelling part creation does not create any part or save data

**Description:** Test the Cancel functionality to ensure no data persistence occurs when user cancels part creation.

**Preconditions:**
1. User logged in with creation permissions
2. Navigate to Parts section
3. Note current part count

**Test Steps:**
1. Click 'Create Part' button
2. Enter 'Part to be Cancelled' in Name field
3. Enter 'CANCEL-001' in IPN field
4. Fill several optional fields with test data
5. Click 'Cancel' button (or equivalent close action)
6. Confirm cancellation if prompted
7. Return to parts list

**Expected Result:**
1. User is returned to parts list or previous page
2. No new part is created in the system
3. Part count remains unchanged
4. Search for IPN 'CANCEL-001' returns no results
5. No part named 'Part to be Cancelled' exists
6. Form data is discarded (not saved)
7. If user reopens creation form, it is blank/reset to defaults

---

## Test Execution Notes

### Credentials for Testing

| Account | Password | Access Level | Use Cases |
|---------|----------|-------------|-----------|
| admin | inventree | Superuser | Full CRUD operations, all test cases |
| allaccess | nolimits | Full CRUD | All creation tests |
| engineer | partsonly | Parts & stock only | Permission tests (UI_PART_015) |
| reader | readonly | View-only | Permission tests (UI_PART_014) |

### Prerequisites
- Demo instance accessible at https://demo.inventree.org
- Demo instance resets daily - tests should be repeatable
- Browser: Chrome/Firefox/Edge (latest versions)
- JavaScript enabled
- Cookies enabled for session management

### Test Data Cleanup
- Demo resets daily, so no manual cleanup required
- For local instances, clean up test parts after execution
- Test IPNs follow pattern: TEST-*, ENG-TEST-*, CANCEL-*, etc.

### Known Limitations
- Image upload may have file size restrictions (typically 5-10 MB)
- Category structure depends on demo instance configuration
- Some attribute combinations may have system-specific restrictions

---

**End of Test Cases**
