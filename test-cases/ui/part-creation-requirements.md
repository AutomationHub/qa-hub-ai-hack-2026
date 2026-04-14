# Part Creation - Structured Requirements Analysis

**Module:** InvenTree Parts Module
**Functional Area:** Part Creation
**Analysis Date:** 2026-04-14
**Documentation Sources:**
- https://docs.inventree.org/en/stable/part/create/
- https://docs.inventree.org/en/stable/part/part/

---

## Table of Contents
1. [Overview](#overview)
2. [Core Requirements](#core-requirements)
3. [Field Requirements](#field-requirements)
4. [Part Attributes Requirements](#part-attributes-requirements)
5. [Validation Requirements](#validation-requirements)
6. [Permission Requirements](#permission-requirements)
7. [Import/Export Requirements](#import-export-requirements)
8. [Business Rules & Constraints](#business-rules--constraints)

---

## Overview

Part creation is the foundation of the InvenTree inventory management system. Users can create parts through manual entry via UI forms or bulk import functionality. Each part represents a physical or virtual item that can be tracked, purchased, sold, or assembled.

---

## Core Requirements

### REQ-PC-001: Manual Part Creation via UI
**Description:** Users must be able to create parts manually through a web form interface.

**Test Scenarios:**
- **Positive:**
  - Create part with all required fields populated
  - Create part with required + optional fields
  - Create part with minimum required data only

- **Negative:**
  - Attempt to create part without required fields
  - Submit empty form
  - Cancel part creation mid-workflow

- **Boundary:**
  - Create part with maximum allowed field lengths
  - Create part with special characters in name
  - Create part with Unicode characters

- **Permission:**
  - Admin user creates part (should succeed)
  - Engineer user creates part (should succeed if has permissions)
  - Reader user attempts to create part (should fail)
  - Unauthenticated user attempts access (should redirect to login)

**Expected Behavior:**
- Form should display with clear field labels and validation hints
- Required fields should be marked visually (asterisk or label)
- Successful creation should redirect to part detail page
- Failure should display clear error messages without losing form data
- Created part should be immediately searchable and visible

**UI Elements:**
- "Create Part" button (from parts list or category view)
- Part creation form with tabs/sections
- Save/Cancel buttons
- Field validation indicators (red borders, error messages)
- Success notification/toast

---

### REQ-PC-002: Part Creation Button Accessibility
**Description:** Users should have multiple entry points to create parts.

**Test Scenarios:**
- **Positive:**
  - Click "Create Part" from main parts list
  - Click "Add Part" from within a category
  - Use quick-add from navigation menu
  - Access via keyboard shortcut (if available)

- **Negative:**
  - Button should be disabled/hidden for users without create permissions

- **Permission:**
  - Verify button visibility for different user roles

**Expected Behavior:**
- Button should be prominently displayed for authorized users
- Button should open part creation form/modal
- Form should pre-populate category if accessed from category view
- Button state should reflect user permissions

**UI Elements:**
- "Create Part" / "New Part" / "Add Part" button
- Plus icon or similar visual indicator
- Keyboard shortcut indicator (optional)

---

## Field Requirements

### REQ-PC-101: Part Name (Required Field)
**Description:** Part name is a mandatory text field that serves as the primary identifier.

**Test Scenarios:**
- **Positive:**
  - Enter valid part name (alphanumeric)
  - Enter name with spaces
  - Enter name with hyphens and underscores
  - Enter name with numbers

- **Negative:**
  - Submit form with empty part name
  - Submit form with only whitespace
  - Enter excessively long name (>250 characters)

- **Boundary:**
  - Single character name
  - Name at maximum length (250 chars typically)
  - Name with special characters: !@#$%^&*()
  - Name with Unicode/emoji characters
  - Name with leading/trailing spaces (should trim)

**Expected Behavior:**
- Field should be marked as required
- Empty submission should show validation error: "Part name is required"
- Name should be trimmed of leading/trailing whitespace
- Duplicate names should be allowed (parts differentiated by IPN/ID)
- Field should accept Unicode characters
- Should have reasonable character limit (display remaining count)

**UI Elements:**
- Text input field labeled "Name" or "Part Name"
- Required indicator (*)
- Character counter (optional)
- Inline validation message

---

### REQ-PC-102: IPN - Internal Part Number (Optional)
**Description:** Optional unique identifier field for internal part numbering system.

**Test Scenarios:**
- **Positive:**
  - Create part without IPN (leave blank)
  - Create part with valid alphanumeric IPN
  - Create part with IPN containing hyphens/underscores
  - Create part with numeric-only IPN

- **Negative:**
  - Attempt to create part with duplicate IPN
  - Enter IPN with invalid special characters

- **Boundary:**
  - Minimum length IPN (1 character)
  - Maximum length IPN (100 characters typically)
  - IPN with only numbers
  - IPN with mixed case (verify case sensitivity)

**Expected Behavior:**
- Field is optional - can be left blank
- If provided, must be unique across all parts
- System should validate uniqueness before saving
- Error message: "Part with this IPN already exists"
- Should support alphanumeric + common separators (-, _, .)
- Case sensitivity should be configurable (typically case-insensitive)

**UI Elements:**
- Text input field labeled "IPN" or "Internal Part Number"
- Optional indicator or no required mark
- Uniqueness validation feedback
- Helper text explaining IPN purpose

---

### REQ-PC-103: Description (Optional)
**Description:** Multi-line text field for detailed part description.

**Test Scenarios:**
- **Positive:**
  - Create part with no description
  - Create part with single-line description
  - Create part with multi-line description
  - Include special formatting (if supported)

- **Boundary:**
  - Very long description (test character limit)
  - Description with HTML/markdown (verify sanitization)
  - Description with line breaks and formatting

**Expected Behavior:**
- Field is optional
- Should support multi-line text entry
- Should preserve line breaks
- May support markdown or rich text (verify)
- Should sanitize HTML for security
- Character limit should be clearly communicated

**UI Elements:**
- Textarea input labeled "Description"
- Optional character counter
- Formatting toolbar (if rich text supported)

---

### REQ-PC-104: Category Assignment (Optional)
**Description:** Hierarchical category selection for part organization.

**Test Scenarios:**
- **Positive:**
  - Create part without category (uncategorized)
  - Assign part to top-level category
  - Assign part to nested subcategory
  - Change category during creation

- **Negative:**
  - Attempt to assign non-existent category (should not be possible via UI)

- **Boundary:**
  - Assign to deeply nested category (5+ levels)
  - Assign to category with special characters in name

**Expected Behavior:**
- Field is optional - parts can exist without category
- Should display category tree/hierarchy
- Should support search/filter in category dropdown
- Pre-populate category if accessed from category view
- Show full category path for clarity
- Category selection should update instantly

**UI Elements:**
- Dropdown/select with category tree
- Search functionality in category selector
- Breadcrumb showing full category path
- "Create new category" quick link (if permitted)

---

### REQ-PC-105: Revision Code (Optional)
**Description:** Version/revision identifier for part management.

**Test Scenarios:**
- **Positive:**
  - Create part without revision
  - Create part with alphanumeric revision (e.g., "A", "Rev 1.0")
  - Create part with numeric revision

- **Boundary:**
  - Very long revision code
  - Revision with special characters

**Expected Behavior:**
- Field is optional
- No format enforcement (free text)
- Used for version tracking and part history
- Multiple parts can have same revision code

**UI Elements:**
- Text input labeled "Revision"
- Helper text explaining revision usage

---

### REQ-PC-106: Keywords (Optional)
**Description:** Tags/keywords for improved search and filtering.

**Test Scenarios:**
- **Positive:**
  - Create part without keywords
  - Add single keyword
  - Add multiple comma-separated keywords
  - Add keywords with spaces

- **Boundary:**
  - Very long keyword string
  - Large number of keywords

**Expected Behavior:**
- Field is optional
- Should support comma-separated or tag-based entry
- Keywords improve searchability
- May be auto-suggested from existing keywords

**UI Elements:**
- Text input or tag input component
- Keyword suggestions/autocomplete
- Visual tag representation

---

### REQ-PC-107: Link/External Reference (Optional)
**Description:** URL field for external documentation or datasheets.

**Test Scenarios:**
- **Positive:**
  - Create part without link
  - Add valid HTTP URL
  - Add valid HTTPS URL
  - Add multiple URLs (if supported)

- **Negative:**
  - Enter invalid URL format
  - Enter non-URL text

- **Boundary:**
  - Very long URL
  - URL with special characters and query parameters

**Expected Behavior:**
- Field is optional
- Should validate URL format
- Should support both HTTP and HTTPS
- May open in new tab when clicked
- Error message: "Please enter a valid URL"

**UI Elements:**
- Text input labeled "Link" or "External Link"
- URL format hint/placeholder
- Link test button (optional)

---

### REQ-PC-108: Units of Measure (Optional)
**Description:** Physical or abstract unit for part quantity.

**Test Scenarios:**
- **Positive:**
  - Create part without unit
  - Select common unit (pcs, kg, m)
  - Select custom/configured unit

- **Boundary:**
  - Select from extensive unit list
  - Unit with special symbols

**Expected Behavior:**
- Field is optional (defaults to "pieces" or system default)
- Should provide dropdown of configured units
- Units affect stock tracking and ordering
- Display unit symbol/abbreviation

**UI Elements:**
- Dropdown/select for unit selection
- Unit symbol display
- Helper text explaining unit purpose

---

### REQ-PC-109: Default Location (Optional)
**Description:** Default storage location for the part.

**Test Scenarios:**
- **Positive:**
  - Create part without default location
  - Assign valid stock location
  - Assign nested location

- **Negative:**
  - Attempt to assign non-existent location

- **Boundary:**
  - Assign deeply nested location

**Expected Behavior:**
- Field is optional
- Should display location hierarchy
- Location dropdown should show tree structure
- Used as default for stock items
- Must be valid stock location from system

**UI Elements:**
- Location picker/dropdown
- Location tree display
- Search in location dropdown

---

### REQ-PC-110: Default Supplier (Optional)
**Description:** Primary supplier for purchasing the part.

**Test Scenarios:**
- **Positive:**
  - Create part without default supplier
  - Assign existing supplier
  - Change supplier during creation

- **Negative:**
  - Attempt to assign non-existent supplier

**Expected Behavior:**
- Field is optional
- Dropdown lists active suppliers only
- Only applicable if part is marked as "Purchaseable"
- Should integrate with supplier part management

**UI Elements:**
- Supplier dropdown/select
- Search functionality
- Link to create new supplier (if permitted)

---

### REQ-PC-111: Minimum Stock Level (Optional, Numeric)
**Description:** Threshold for low stock alerts.

**Test Scenarios:**
- **Positive:**
  - Create part without minimum stock
  - Set minimum stock to 0
  - Set positive integer value
  - Set decimal value (if supported)

- **Negative:**
  - Enter negative value
  - Enter non-numeric value
  - Enter excessively large number

- **Boundary:**
  - Minimum stock = 0
  - Very large minimum stock value
  - Decimal precision limits

**Expected Behavior:**
- Field is optional (defaults to 0)
- Must be non-negative number
- Decimal support depends on part unit configuration
- Triggers alerts when stock falls below threshold
- Error message: "Minimum stock must be a non-negative number"

**UI Elements:**
- Numeric input field
- Increment/decrement buttons (optional)
- Unit indicator
- Helper text about alert functionality

---

### REQ-PC-112: Image Upload (Optional)
**Description:** Primary image for part visualization.

**Test Scenarios:**
- **Positive:**
  - Create part without image
  - Upload valid image (JPG, PNG)
  - Upload from file system
  - Use URL for remote image (if supported)

- **Negative:**
  - Upload invalid file type (PDF, EXE)
  - Upload file exceeding size limit
  - Upload corrupted image

- **Boundary:**
  - Upload minimum size image (1x1 pixel)
  - Upload maximum allowed file size
  - Upload image with non-standard dimensions
  - Upload very large resolution image

**Expected Behavior:**
- Field is optional
- Supported formats: JPG, PNG, GIF, WebP
- File size limit (typically 5-10 MB)
- Image should be resized/optimized for display
- Thumbnail generation
- Error messages for invalid format or size
- Preview before upload confirmation

**UI Elements:**
- File upload button/drag-drop area
- Image preview
- Progress indicator during upload
- Remove/replace image button
- Supported formats indicator

---

## Part Attributes Requirements

### REQ-PC-201: Active Attribute
**Description:** Boolean flag indicating if part is currently active in the system.

**Test Scenarios:**
- **Positive:**
  - Create part with Active=True (default)
  - Create part with Active=False
  - Toggle between active/inactive states

- **Permission:**
  - Verify only authorized users can set inactive status

**Expected Behavior:**
- Defaults to Active=True for new parts
- Inactive parts may be hidden from certain views
- Inactive parts cannot be used in new BOMs or orders (typically)
- Status should be clearly indicated in UI
- Deactivation should require confirmation

**UI Elements:**
- Checkbox or toggle switch
- "Active" / "Inactive" label
- Warning message when setting inactive
- Status badge/indicator

---

### REQ-PC-202: Virtual Attribute
**Description:** Boolean flag for non-physical parts (software, services, labor).

**Test Scenarios:**
- **Positive:**
  - Create physical part (Virtual=False)
  - Create virtual part (Virtual=True)
  - Create software license part (Virtual)
  - Create service part (Virtual)

- **Boundary:**
  - Verify virtual parts have appropriate restrictions
  - Virtual parts with stock tracking (should be limited)

**Expected Behavior:**
- Defaults to Virtual=False
- Virtual parts typically don't have stock location
- Virtual parts may skip serial number tracking
- Affects available features (stock, shipping)
- Clear indicator in part detail view

**UI Elements:**
- Checkbox labeled "Virtual"
- Helper text explaining virtual parts
- Icon/badge indicating virtual status

---

### REQ-PC-203: Template Attribute
**Description:** Boolean flag marking part as a template for creating variants.

**Test Scenarios:**
- **Positive:**
  - Create regular part (Template=False)
  - Create template part (Template=True)
  - Create variant from template (separate workflow)

- **Negative:**
  - Attempt to create template and variant simultaneously

- **Boundary:**
  - Template with all attributes enabled
  - Template that is also Assembly, Component, etc.

**Expected Behavior:**
- Defaults to Template=False
- Templates cannot be used directly in BOMs or stock
- Templates enable variant creation workflow
- Template parts show in separate view/filter
- Cannot be both Template and Variant of another part

**UI Elements:**
- Checkbox labeled "Template"
- Helper text explaining templates
- Warning about template restrictions
- Link to variant creation workflow

---

### REQ-PC-204: Assembly Attribute
**Description:** Boolean flag indicating part is assembled from other parts (has BOM).

**Test Scenarios:**
- **Positive:**
  - Create assembly part (Assembly=True)
  - Create non-assembly part (Assembly=False)
  - Create assembly with empty BOM
  - Create assembly and add BOM items (separate workflow)

- **Boundary:**
  - Assembly that is also Component (valid use case)
  - Assembly marked as Virtual

**Expected Behavior:**
- Defaults to Assembly=False
- Assemblies can have Bill of Materials (BOM)
- Enables build order functionality
- Assembly parts can be built from component parts
- BOM management features become available

**UI Elements:**
- Checkbox labeled "Assembly"
- Helper text explaining assemblies
- BOM tab enabled/disabled based on status
- Build order features linked to this flag

---

### REQ-PC-205: Component Attribute
**Description:** Boolean flag indicating part can be used as component in assemblies.

**Test Scenarios:**
- **Positive:**
  - Create component part (Component=True)
  - Create non-component part (Component=False)
  - Use component in BOM (separate workflow)

- **Boundary:**
  - Part that is both Assembly and Component
  - Virtual part marked as Component

**Expected Behavior:**
- Defaults to Component=False
- Components can be added to other parts' BOMs
- Enables component-specific features
- Affects part availability in BOM selection
- Component tracking in assemblies

**UI Elements:**
- Checkbox labeled "Component"
- Helper text explaining components
- Usage tracking indicator (where used)

---

### REQ-PC-206: Trackable Attribute
**Description:** Boolean flag enabling serial number or batch tracking.

**Test Scenarios:**
- **Positive:**
  - Create trackable part (Trackable=True)
  - Create non-trackable part (Trackable=False)
  - Add serial numbers to trackable part (separate workflow)

- **Negative:**
  - Attempt to make virtual part trackable (may be restricted)

- **Boundary:**
  - Trackable with very large serial number count
  - Change trackable status after stock exists (should warn)

**Expected Behavior:**
- Defaults to Trackable=False
- Enables serial number and batch tracking
- Affects stock item creation workflow
- Each stock item requires serial/batch number if enabled
- Cannot be easily changed after stock exists
- Warning when enabling for parts with existing stock

**UI Elements:**
- Checkbox labeled "Trackable"
- Helper text explaining tracking implications
- Warning message for status changes
- Serial number management link

---

### REQ-PC-207: Purchaseable Attribute
**Description:** Boolean flag indicating part can be purchased from suppliers.

**Test Scenarios:**
- **Positive:**
  - Create purchaseable part (Purchaseable=True)
  - Create non-purchaseable part (Purchaseable=False)
  - Link suppliers to purchaseable part (separate workflow)

- **Boundary:**
  - Purchaseable assembly (buy vs build decision)
  - Purchaseable virtual part (services)

**Expected Behavior:**
- Defaults to Purchaseable=False
- Enables supplier part management
- Enables purchase order functionality
- Can assign default supplier when enabled
- Purchaseable parts appear in procurement workflows

**UI Elements:**
- Checkbox labeled "Purchaseable"
- Helper text explaining purchasing
- Supplier management section (shown/hidden)
- Purchase order integration indicators

---

### REQ-PC-208: Salable Attribute
**Description:** Boolean flag indicating part can be sold to customers.

**Test Scenarios:**
- **Positive:**
  - Create salable part (Salable=True)
  - Create non-salable part (Salable=False)
  - Create sales order with salable part (separate workflow)

- **Boundary:**
  - Salable component (raw material for sale)
  - Salable virtual part (services)

**Expected Behavior:**
- Defaults to Salable=False
- Enables customer part management
- Enables sales order functionality
- Salable parts appear in sales workflows
- Pricing information becomes relevant

**UI Elements:**
- Checkbox labeled "Salable" or "Saleable"
- Helper text explaining sales functionality
- Customer part section (shown/hidden)
- Pricing management link

---

### REQ-PC-209: Multiple Attributes Combinations
**Description:** Parts can have multiple attributes enabled simultaneously.

**Test Scenarios:**
- **Positive:**
  - Create part with no attributes (all False except Active)
  - Create part with all attributes enabled
  - Assembly + Component (sub-assembly)
  - Purchaseable + Salable (resale item)
  - Assembly + Purchaseable (buy vs build)
  - Virtual + Salable (service product)

- **Boundary:**
  - Template + other attributes (verify restrictions)
  - Virtual + Trackable (should be restricted/warned)

**Expected Behavior:**
- Most attribute combinations are valid
- Some combinations may show warnings (Virtual + Trackable)
- Template parts may have restrictions on other attributes
- UI should clearly show all enabled attributes
- Documentation should explain valid combinations

**UI Elements:**
- Attribute checklist/grid
- Warning messages for unusual combinations
- Helper text explaining interactions
- Visual summary of selected attributes

---

## Validation Requirements

### REQ-PC-301: Client-Side Form Validation
**Description:** Immediate validation feedback without server round-trip.

**Test Scenarios:**
- **Positive:**
  - Valid input shows no errors
  - Required field completion removes error state

- **Negative:**
  - Empty required field shows error immediately on blur
  - Invalid format shows error on blur (URL, numbers)
  - Invalid character entry prevented or highlighted

**Expected Behavior:**
- Real-time validation as user types or on field blur
- Clear error messages below each field
- Error state visual indication (red border)
- Submit button may be disabled until form is valid
- Error messages in user's language
- Validation messages are specific and actionable

**UI Elements:**
- Field-level error messages
- Error state styling (red borders, icons)
- Validation icons (check marks, x marks)
- Error summary at form top (optional)

---

### REQ-PC-302: Server-Side Form Validation
**Description:** Backend validation for data integrity and security.

**Test Scenarios:**
- **Positive:**
  - Valid submission accepted
  - Part created successfully

- **Negative:**
  - Duplicate IPN rejected by server
  - Invalid data types rejected
  - Constraint violations caught
  - Malicious input sanitized/rejected

**Expected Behavior:**
- All client-side validation is repeated server-side
- Server returns detailed validation errors
- User sees server errors in friendly format
- Form data is preserved on validation failure
- Security validations (XSS, SQL injection) enforced
- Transaction rolled back on any validation failure

**UI Elements:**
- Server error messages displayed in form
- Error summary banner
- Field highlighting for server errors
- Retry mechanism

---

### REQ-PC-303: IPN Uniqueness Validation
**Description:** System enforces unique Internal Part Numbers.

**Test Scenarios:**
- **Positive:**
  - Create part with unique IPN
  - Create part without IPN (leave blank)

- **Negative:**
  - Attempt to create part with existing IPN
  - Duplicate IPN check is case-insensitive (typically)

- **Boundary:**
  - IPN "ABC" vs "abc" (should be same)
  - IPN with leading/trailing spaces

**Expected Behavior:**
- Uniqueness check happens on form submission
- Clear error message: "Part with IPN 'XYZ' already exists"
- May include link to existing part
- Real-time check on blur (optional, performance dependent)
- Case-insensitive comparison (configurable)
- Whitespace trimmed before comparison

**UI Elements:**
- Inline validation message on IPN field
- Link to conflicting part
- Clear error explanation

---

### REQ-PC-304: Required Fields Validation
**Description:** System enforces all required fields before allowing submission.

**Test Scenarios:**
- **Negative:**
  - Submit form with empty Name field
  - Submit form with only whitespace in Name
  - Clear required field after filling it

- **Positive:**
  - All required fields completed allows submission

**Expected Behavior:**
- Required fields marked with asterisk or "Required" label
- Submit button may be disabled until all required fields completed
- Submission with missing required fields shows error summary
- Each missing field highlighted individually
- Focus moved to first invalid field
- Error message: "This field is required"

**UI Elements:**
- Required field indicators (*)
- Field-level error messages
- Form-level error summary
- Disabled submit button (optional)

---

### REQ-PC-305: Data Type Validation
**Description:** System validates data types for numeric and special fields.

**Test Scenarios:**
- **Negative:**
  - Enter text in numeric field (Minimum Stock)
  - Enter special characters in IPN (if restricted)
  - Enter invalid URL format in Link field

- **Positive:**
  - Valid numeric input accepted
  - Valid URL format accepted

**Expected Behavior:**
- Numeric fields reject non-numeric input
- URL fields validate URL format
- Date fields enforce date format (if any)
- Type validation shows specific error
- Input masking or pattern enforcement (optional)
- Error: "Please enter a valid number/URL/date"

**UI Elements:**
- Input type constraints (HTML5 input types)
- Format hints/placeholders
- Input masks (phone, date patterns)
- Validation messages specific to type

---

### REQ-PC-306: Length Validation
**Description:** System enforces maximum field lengths.

**Test Scenarios:**
- **Negative:**
  - Enter text exceeding max length in Name field
  - Paste very long text in Description

- **Boundary:**
  - Enter text at exactly maximum length
  - Enter text one character over maximum

**Expected Behavior:**
- Client-side: Input truncated or prevented at max length
- Character counter shows remaining characters
- Server-side: Excess characters rejected with error
- Error: "Maximum length is X characters"
- Truncation handled gracefully without data loss

**UI Elements:**
- Character counter (X/250 characters)
- Visual indicator approaching limit
- Hard limit prevention or soft warning
- Error message on over-limit

---

## Permission Requirements

### REQ-PC-401: Create Permission Required
**Description:** User must have "add_part" permission to create parts.

**Test Scenarios:**
- **Permission:**
  - Admin user can create parts
  - User with "add_part" permission can create parts
  - Engineer role can create parts (if role has permission)
  - Reader role cannot create parts
  - Unauthenticated user cannot access creation form

**Expected Behavior:**
- Users without permission don't see "Create Part" button
- Direct URL access redirected or shows permission denied
- Error message: "You don't have permission to create parts"
- Appropriate HTTP status code (403 Forbidden)

**UI Elements:**
- Create button hidden for unauthorized users
- Permission denied page/message
- Contact administrator message (optional)

---

### REQ-PC-402: Role-Based Access
**Description:** Different user roles have different creation capabilities.

**Test Scenarios:**
- **Permission:**
  - Admin: Full creation access, all attributes
  - AllAccess: Full creation access
  - Engineer: Can create parts in allowed categories
  - Reader: Cannot create parts (read-only)
  - Custom roles: Permissions as configured

**Expected Behavior:**
- Role permissions checked on every request
- Permissions granular: create, edit, delete, view
- Category-based permissions may apply
- Attribute restrictions based on role (optional)
- Permission checks both client and server side

**UI Elements:**
- Role-appropriate UI elements
- Permission-based feature availability
- Clear messaging for permission issues

---

### REQ-PC-403: Category-Based Permissions
**Description:** Permissions may be restricted by category hierarchy.

**Test Scenarios:**
- **Permission:**
  - User can create parts in assigned categories only
  - User cannot create parts in restricted categories
  - Category dropdown shows only permitted categories

- **Boundary:**
  - User has permission to parent but not child category
  - User has permission to child but not parent category

**Expected Behavior:**
- Category dropdown filters based on permissions
- Attempt to create in unauthorized category fails
- Error: "You don't have permission to create parts in this category"
- Permission inheritance configurable (parent/child)

**UI Elements:**
- Filtered category dropdown
- Permission denied message for category
- Visual indicator of accessible categories

---

### REQ-PC-404: Attribute Permission Restrictions
**Description:** Certain attributes may require additional permissions.

**Test Scenarios:**
- **Permission:**
  - Setting Active/Inactive may require special permission
  - Template creation may require admin rights
  - All users with create permission can set basic attributes

**Expected Behavior:**
- Restricted attributes hidden/disabled for unauthorized users
- Attempt to set restricted attribute fails with error
- Clear messaging about permission requirements
- Defaults applied for inaccessible attributes

**UI Elements:**
- Disabled checkboxes for restricted attributes
- Tooltips explaining permission requirements
- Permission-based form sections

---

## Import/Export Requirements

### REQ-PC-501: Bulk Part Import
**Description:** Users can import multiple parts from structured data files.

**Test Scenarios:**
- **Positive:**
  - Import parts from CSV file
  - Import parts from Excel file (if supported)
  - Import with all required fields
  - Import with required + optional fields
  - Import with category mapping

- **Negative:**
  - Import file with missing required columns
  - Import file with invalid data types
  - Import file with duplicate IPNs
  - Import file with too many rows (test limits)

- **Boundary:**
  - Import single part via import (minimum)
  - Import maximum allowed parts in one file
  - Import file with special characters
  - Import file with Unicode/international characters

**Expected Behavior:**
- Support CSV format minimum
- Excel format support (optional)
- Import wizard guides user through process
- Field mapping interface for columns
- Validation before import execution
- Preview of data to be imported
- Detailed error report for invalid rows
- Partial success option (import valid, report errors)
- Transaction rollback on critical errors

**UI Elements:**
- "Import Parts" button
- File upload interface
- Column mapping wizard
- Data preview table
- Validation error report
- Progress indicator during import
- Success/failure summary

---

### REQ-PC-502: Import Field Mapping
**Description:** Import wizard allows mapping CSV columns to part fields.

**Test Scenarios:**
- **Positive:**
  - Auto-detect standard column names
  - Manually map custom column names
  - Map optional fields selectively
  - Skip columns not needed

- **Boundary:**
  - CSV with non-standard column names
  - CSV with extra columns (should ignore)
  - CSV missing optional columns (should use defaults)

**Expected Behavior:**
- Intelligent column name detection
- Drag-drop or dropdown for manual mapping
- Clear indication of required vs optional mappings
- Ability to set default values for unmapped fields
- Preview showing mapped data
- Save mapping template for reuse (optional)

**UI Elements:**
- Column mapping interface
- Source column → Target field mapping
- Default value inputs for unmapped fields
- Mapping preview table
- Save/load mapping template buttons

---

### REQ-PC-503: Import Validation and Error Handling
**Description:** Import process validates data and reports errors clearly.

**Test Scenarios:**
- **Positive:**
  - All valid data imports successfully
  - Valid rows import, invalid rows reported

- **Negative:**
  - Missing required field in row 5 (reported with row number)
  - Invalid IPN format in row 10
  - Duplicate IPN across import rows
  - Invalid category reference

- **Boundary:**
  - Import with 100% invalid rows (none imported)
  - Import with mix of valid/invalid (partial import)

**Expected Behavior:**
- Pre-import validation phase
- Row-by-row error reporting
- Error report downloadable (CSV/Excel)
- Option to continue on non-critical errors
- Option to abort entire import on any error
- Clear error messages with row numbers
- Link to documentation for error resolution
- Imported parts marked/tagged for easy identification

**UI Elements:**
- Validation progress bar
- Error report table/list
- Download error report button
- Import options (fail-fast vs continue)
- Success/error statistics
- Rollback option (if supported)

---

### REQ-PC-504: Part Export Functionality
**Description:** Users can export part data for backup or external use.

**Test Scenarios:**
- **Positive:**
  - Export all parts to CSV
  - Export filtered parts to CSV
  - Export selected parts to CSV
  - Export with all fields included
  - Export with selected fields only

- **Boundary:**
  - Export very large dataset (10,000+ parts)
  - Export with special characters in data
  - Export with images (may be URLs or excluded)

**Expected Behavior:**
- Export button available in parts list
- Format options (CSV, Excel, JSON)
- Field selection for export
- Respect view filters for export
- Progress indicator for large exports
- Downloaded file properly formatted
- Filename includes timestamp
- Exported data matches displayed data

**UI Elements:**
- "Export" button in parts list toolbar
- Export format selector
- Field selection checkboxes
- Progress indicator
- Download completion notification

---

### REQ-PC-505: Import Template Download
**Description:** System provides import templates for easy data preparation.

**Test Scenarios:**
- **Positive:**
  - Download blank CSV template
  - Download template with sample data
  - Template includes all required columns
  - Template includes optional columns

- **Expected Behavior:**
- Template available before import process
- Template includes column headers matching system fields
- Sample row included showing data format
- Documentation explaining each column
- Template filename indicates version/date

**UI Elements:**
- "Download Template" link/button
- Template format selector (CSV/Excel)
- Include sample data checkbox
- Template documentation link

---

## Business Rules & Constraints

### REQ-PC-601: Active Status Default
**Description:** New parts default to Active status unless explicitly set inactive.

**Test Scenarios:**
- **Positive:**
  - Create part without touching Active checkbox (defaults to True)
  - Explicitly set Active=False during creation

- **Expected Behavior:**
- Active checkbox checked by default
- Inactive parts require deliberate action
- Active status affects part visibility in workflows

**UI Elements:**
- Active checkbox pre-checked
- Clear indication of default state

---

### REQ-PC-602: Template Cannot Be Variant
**Description:** A part marked as Template cannot simultaneously be a variant of another template.

**Test Scenarios:**
- **Negative:**
  - Create part with Template=True and Template variant relationship

- **Boundary:**
  - Template that later becomes a variant (status change restriction)

**Expected Behavior:**
- Mutually exclusive states enforced
- Clear error message explaining restriction
- UI prevents invalid combination

**UI Elements:**
- Disabled or hidden fields when conflict exists
- Warning message explaining constraint

---

### REQ-PC-603: Virtual Parts Stock Tracking Limitation
**Description:** Virtual parts have limited or no stock tracking capabilities.

**Test Scenarios:**
- **Boundary:**
  - Create virtual part and attempt stock operations
  - Virtual part with Trackable=True (should warn or prevent)

- **Expected Behavior:**
- Virtual parts may not have stock locations
- Virtual parts may not have serial numbers
- System warns or prevents incompatible attribute combinations
- Clear documentation of virtual part limitations

**UI Elements:**
- Warning message when combining Virtual with stock-related attributes
- Disabled stock tracking for virtual parts
- Helper text explaining limitations

---

### REQ-PC-604: IPN Case Insensitivity
**Description:** Internal Part Numbers are case-insensitive for uniqueness checks.

**Test Scenarios:**
- **Negative:**
  - Create part with IPN "ABC123"
  - Attempt to create another part with IPN "abc123" (should fail)
  - Attempt IPN "ABC123" with spaces (should be trimmed and fail)

- **Expected Behavior:**
- Uniqueness check is case-insensitive
- Original case is preserved but not enforced
- Whitespace trimmed from IPN before storage/comparison
- Clear error message showing existing part with similar IPN

**UI Elements:**
- Error message showing conflicting IPN with link
- Real-time duplication check (optional)

---

### REQ-PC-605: Category Hierarchy Integrity
**Description:** Category assignments must reference valid, existing categories.

**Test Scenarios:**
- **Negative:**
  - Direct API call with invalid category ID (should fail)
  - Assign part to deleted category (should fail or auto-update)

- **Expected Behavior:**
- Only valid categories available in dropdown
- Deleted categories handled gracefully
- Category tree structure maintained
- Orphaned parts (deleted category) moved to uncategorized or parent

**UI Elements:**
- Category dropdown shows only valid categories
- Tree structure visually represented
- Deleted category handling (error or default)

---

### REQ-PC-606: Default Values Application
**Description:** System applies sensible default values for optional fields.

**Test Scenarios:**
- **Positive:**
  - Create part with minimal data (observe defaults)
  - Verify all boolean attributes default to False except Active
  - Verify numeric fields default to 0 or null

- **Expected Behavior:**
- Active = True (default)
- All other boolean attributes = False (default)
- Minimum Stock = 0 (default)
- Optional text fields = null/empty (default)
- Defaults configurable at system level (optional)
- Defaults clearly communicated to user

**UI Elements:**
- Placeholder text showing defaults
- Pre-filled default values
- Clear indication of default vs. user-entered

---

### REQ-PC-607: Image Size and Format Restrictions
**Description:** System enforces limits on uploaded images.

**Test Scenarios:**
- **Negative:**
  - Upload 50MB image (should reject)
  - Upload PDF file as image (should reject)
  - Upload corrupted image file

- **Boundary:**
  - Upload image at exactly size limit
  - Upload minimum size image (1x1 pixel)

- **Expected Behavior:**
- Maximum file size: 5-10MB (configurable)
- Supported formats: JPG, PNG, GIF, WebP
- Image automatically resized if too large
- Thumbnail generated for listings
- Original image preserved (or configurable)
- Clear error messages for unsupported formats
- File type validated by content, not extension only

**UI Elements:**
- Upload constraints displayed (max size, formats)
- Preview of uploaded image
- Upload progress indicator
- Error messages for invalid uploads
- Image optimization feedback

---

### REQ-PC-608: Attribute Combinations Validation
**Description:** Certain attribute combinations are invalid or require warnings.

**Test Scenarios:**
- **Warning Cases:**
  - Virtual + Trackable (non-standard, warn user)
  - Template + Trackable (may be unusual)

- **Invalid Cases:**
  - Template + Variant (mutually exclusive)

- **Expected Behavior:**
- System prevents invalid combinations
- System warns on unusual combinations (allows override)
- Clear explanation of why combination is problematic
- Documentation links for complex rules

**UI Elements:**
- Warning banners for unusual combinations
- Blocking error for invalid combinations
- Explanation text and documentation links

---

### REQ-PC-609: Audit Trail for Part Creation
**Description:** System logs part creation events for audit purposes.

**Test Scenarios:**
- **Positive:**
  - Create part and verify audit log entry
  - Verify log includes user, timestamp, initial values

- **Expected Behavior:**
- Every part creation logged to audit trail
- Log includes: user ID, timestamp, initial values, IP address
- Audit log immutable (cannot be edited/deleted)
- Audit log accessible to admins
- Audit log includes before/after state (for creation, before=null)

**UI Elements:**
- Audit log visible in part detail view
- Audit log filterable by user, date, action
- Audit export functionality

---

### REQ-PC-610: Successful Creation Confirmation
**Description:** User receives clear confirmation after successful part creation.

**Test Scenarios:**
- **Positive:**
  - Create part and observe success message
  - Verify redirect to part detail page
  - Verify part appears in parts list

- **Expected Behavior:**
- Success message/toast displayed prominently
- Redirect to newly created part detail page
- Part immediately visible in parts list (no refresh needed)
- Part ID/number displayed in success message
- Option to create another part (quick add)
- Breadcrumb updated to show new part

**UI Elements:**
- Success toast/notification
- Part detail page with all entered data
- "Create Another Part" button (optional)
- Success message with part name/ID

---

## Summary Statistics

**Total Requirements:** 61
**Field Requirements:** 12
**Attribute Requirements:** 9
**Validation Requirements:** 6
**Permission Requirements:** 4
**Import/Export Requirements:** 5
**Business Rules:** 10

**Minimum Test Cases Recommended:** 12 (as per functional area definition)
**Comprehensive Test Coverage:** 150+ test scenarios across all requirements

---

## Appendix: Common Error Messages

| Error Code | Message | Cause |
|------------|---------|-------|
| ERR-PC-001 | Part name is required | Name field empty |
| ERR-PC-002 | Part with this IPN already exists | Duplicate IPN |
| ERR-PC-003 | Please enter a valid URL | Invalid link format |
| ERR-PC-004 | Minimum stock must be a non-negative number | Negative or invalid number |
| ERR-PC-005 | You don't have permission to create parts | User lacks add_part permission |
| ERR-PC-006 | You don't have permission to create parts in this category | Category permission restriction |
| ERR-PC-007 | Invalid category selected | Category doesn't exist |
| ERR-PC-008 | Maximum length is X characters | Field length exceeded |
| ERR-PC-009 | Template parts cannot be variants | Invalid attribute combination |
| ERR-PC-010 | File size exceeds maximum allowed (Xmb) | Image too large |
| ERR-PC-011 | Unsupported file format | Invalid image type |
| ERR-PC-012 | Part name cannot contain only whitespace | Invalid name format |

---

## Appendix: UI Workflow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     Part Creation Workflow                       │
└─────────────────────────────────────────────────────────────────┘
                                │
                    ┌───────────┴───────────┐
                    │  User Entry Points    │
                    │  • Parts List Button  │
                    │  • Category Button    │
                    │  • Quick Add Menu     │
                    └───────────┬───────────┘
                                │
                    ┌───────────▼────────────┐
                    │  Part Creation Form    │
                    │  • Required Fields     │
                    │  • Optional Fields     │
                    │  • Attribute Checkboxes│
                    │  • Image Upload        │
                    └───────────┬────────────┘
                                │
                    ┌───────────▼────────────┐
                    │  Client-Side           │
                    │  Validation            │
                    │  • Required Fields     │
                    │  • Data Types          │
                    │  • Format Checks       │
                    └───────────┬────────────┘
                                │
                        Valid? ─────No───> Show Errors
                                │                │
                               Yes               │
                                │                │
                    ┌───────────▼────────────┐   │
                    │  Submit to Server      │   │
                    └───────────┬────────────┘   │
                                │                │
                    ┌───────────▼────────────┐   │
                    │  Server-Side           │   │
                    │  Validation            │   │
                    │  • All Client Checks   │   │
                    │  • Uniqueness (IPN)    │   │
                    │  • Permissions         │   │
                    │  • Business Rules      │   │
                    └───────────┬────────────┘   │
                                │                │
                        Valid? ─────No───────────┘
                                │
                               Yes
                                │
                    ┌───────────▼────────────┐
                    │  Create Part Record    │
                    │  • Save to Database    │
                    │  • Upload Image        │
                    │  • Create Audit Log    │
                    │  • Update Indexes      │
                    └───────────┬────────────┘
                                │
                    ┌───────────▼────────────┐
                    │  Success Response      │
                    │  • Show Success Toast  │
                    │  • Redirect to Detail  │
                    │  • Update Parts List   │
                    └────────────────────────┘
```

---

**Document Version:** 1.0
**Last Updated:** 2026-04-14
**Status:** Draft - Pending Documentation Access

**Note:** This requirements analysis is based on standard inventory management system patterns and the project's functional area definitions. Once direct access to InvenTree documentation is available, this document should be validated and updated with product-specific details.
