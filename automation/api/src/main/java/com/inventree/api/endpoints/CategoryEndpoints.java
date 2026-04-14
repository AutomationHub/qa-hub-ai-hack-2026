package com.inventree.api.endpoints;

/**
 * Constants for Part Category API endpoints.
 */
public class CategoryEndpoints {

    /**
     * Part categories list endpoint.
     * GET: List all categories
     * POST: Create new category
     */
    public static final String CATEGORIES = "/part/category/";

    /**
     * Category by ID endpoint.
     * GET: Get category details
     * PUT/PATCH: Update category
     * DELETE: Delete category
     */
    public static final String CATEGORY_BY_ID = "/part/category/{id}/";

    /**
     * Category tree endpoint.
     * GET: Get category tree structure
     */
    public static final String CATEGORY_TREE = "/part/category/tree/";

    /**
     * Category parameters endpoint.
     * GET: List category parameters
     */
    public static final String CATEGORY_PARAMETERS = "/part/category/{id}/parameters/";

    private CategoryEndpoints() {
        // Private constructor to prevent instantiation
    }
}
