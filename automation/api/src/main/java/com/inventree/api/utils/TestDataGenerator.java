package com.inventree.api.utils;

import com.inventree.api.models.Part;
import com.inventree.api.models.PartCategory;

import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating test data.
 * Provides methods for creating valid and invalid test payloads.
 */
public class TestDataGenerator {

    private static final Random random = new Random();

    /**
     * Generates a unique suffix to avoid name collisions on demo server.
     *
     * @return Unique suffix
     */
    private static String getUniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Creates a valid Part with only required fields.
     *
     * @return Part with required fields
     */
    public static Part validPart() {
        return Part.builder()
                .name("Test Part " + getUniqueSuffix())
                .description("Test part description")
                .category(1) // Assuming category 1 exists
                .active(true)
                .build();
    }

    /**
     * Creates a valid Part with all fields populated.
     *
     * @return Part with all fields
     */
    public static Part validPartWithAllFields() {
        return Part.builder()
                .name("Test Part Complete " + getUniqueSuffix())
                .description("Complete test part with all fields")
                .category(1)
                .ipn("IPN-" + getUniqueSuffix())
                .revision("A")
                .keywords("test, automation, api")
                .units("pcs")
                .link("https://example.com/part")
                .active(true)
                .virtual_(false)
                .isTemplate(false)
                .assembly(false)
                .component(true)
                .trackable(false)
                .purchaseable(true)
                .saleable(false)
                .minimumStock(10)
                .notes("Test notes")
                .build();
    }

    /**
     * Creates a Part with missing name (invalid).
     *
     * @return Part without name
     */
    public static Part invalidPartMissingName() {
        return Part.builder()
                .description("Part without name")
                .category(1)
                .active(true)
                .build();
    }

    /**
     * Creates a Part with missing category (invalid).
     *
     * @return Part without category
     */
    public static Part invalidPartMissingCategory() {
        return Part.builder()
                .name("Test Part No Category " + getUniqueSuffix())
                .description("Part without category")
                .active(true)
                .build();
    }

    /**
     * Creates a Part with excessively long name (boundary test).
     *
     * @param length Name length
     * @return Part with long name
     */
    public static Part partWithLongName(int length) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < length; i++) {
            name.append("A");
        }
        return Part.builder()
                .name(name.toString())
                .description("Part with long name")
                .category(1)
                .active(true)
                .build();
    }

    /**
     * Creates a Part with empty name (invalid).
     *
     * @return Part with empty name
     */
    public static Part partWithEmptyName() {
        return Part.builder()
                .name("")
                .description("Part with empty name")
                .category(1)
                .active(true)
                .build();
    }

    /**
     * Creates a Part with null description.
     *
     * @return Part with null description
     */
    public static Part partWithNullDescription() {
        return Part.builder()
                .name("Test Part No Desc " + getUniqueSuffix())
                .description(null)
                .category(1)
                .active(true)
                .build();
    }

    /**
     * Creates a Part configured as a template.
     *
     * @return Template part
     */
    public static Part templatePart() {
        return Part.builder()
                .name("Template Part " + getUniqueSuffix())
                .description("Template part for testing")
                .category(1)
                .active(true)
                .isTemplate(true)
                .build();
    }

    /**
     * Creates a Part configured as an assembly.
     *
     * @return Assembly part
     */
    public static Part assemblyPart() {
        return Part.builder()
                .name("Assembly Part " + getUniqueSuffix())
                .description("Assembly part for testing")
                .category(1)
                .active(true)
                .assembly(true)
                .build();
    }

    /**
     * Creates a Part configured as a component.
     *
     * @return Component part
     */
    public static Part componentPart() {
        return Part.builder()
                .name("Component Part " + getUniqueSuffix())
                .description("Component part for testing")
                .category(1)
                .active(true)
                .component(true)
                .purchaseable(true)
                .build();
    }

    /**
     * Creates a valid PartCategory.
     *
     * @return Valid category
     */
    public static PartCategory validCategory() {
        return PartCategory.builder()
                .name("Test Category " + getUniqueSuffix())
                .description("Test category description")
                .build();
    }

    /**
     * Creates a PartCategory with parent.
     *
     * @param parentId Parent category ID
     * @return Category with parent
     */
    public static PartCategory categoryWithParent(Integer parentId) {
        return PartCategory.builder()
                .name("Sub Category " + getUniqueSuffix())
                .description("Sub category description")
                .parent(parentId)
                .build();
    }

    /**
     * Generates a random integer within range.
     *
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Random integer
     */
    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
