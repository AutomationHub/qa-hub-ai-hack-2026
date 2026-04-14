package com.inventree.api.endpoints;

/**
 * Constants for Part API endpoints.
 */
public class PartEndpoints {

    /**
     * Parts list endpoint.
     * GET: List all parts
     * POST: Create new part
     */
    public static final String PARTS = "/part/";

    /**
     * Part by ID endpoint.
     * GET: Get part details
     * PUT/PATCH: Update part
     * DELETE: Delete part
     */
    public static final String PART_BY_ID = "/part/{id}/";

    /**
     * Part metadata endpoint.
     * GET: Get part metadata
     * PUT/PATCH: Update part metadata
     */
    public static final String PART_METADATA = "/part/{id}/metadata/";

    /**
     * Part parameters endpoint.
     * GET: List part parameters
     * POST: Create part parameter
     */
    public static final String PART_PARAMETERS = "/part/{id}/parameters/";

    /**
     * Part stock items endpoint.
     * GET: List stock items for a part
     */
    public static final String PART_STOCK = "/part/{id}/stock/";

    /**
     * Part BOM items endpoint.
     * GET: List BOM items for a part
     */
    public static final String PART_BOM = "/part/{id}/bom/";

    /**
     * Part test templates endpoint.
     * GET: List test templates for a part
     */
    public static final String PART_TEST_TEMPLATES = "/part/{id}/test-templates/";

    /**
     * Part attachments endpoint.
     * GET: List attachments for a part
     * POST: Upload attachment
     */
    public static final String PART_ATTACHMENTS = "/part/{id}/attachments/";

    private PartEndpoints() {
        // Private constructor to prevent instantiation
    }
}
