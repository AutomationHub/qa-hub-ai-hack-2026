package com.inventree.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for PartCategory entity.
 * Used for request serialization and response deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartCategory {

    @JsonProperty("pk")
    private Integer pk;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("parent")
    private Integer parent;

    @JsonProperty("pathstring")
    private String pathstring;

    @JsonProperty("level")
    private Integer level;

    // Default constructor
    public PartCategory() {
    }

    // Builder pattern
    public static class Builder {
        private PartCategory category = new PartCategory();

        public Builder pk(Integer pk) {
            category.pk = pk;
            return this;
        }

        public Builder name(String name) {
            category.name = name;
            return this;
        }

        public Builder description(String description) {
            category.description = description;
            return this;
        }

        public Builder parent(Integer parent) {
            category.parent = parent;
            return this;
        }

        public Builder pathstring(String pathstring) {
            category.pathstring = pathstring;
            return this;
        }

        public Builder level(Integer level) {
            category.level = level;
            return this;
        }

        public PartCategory build() {
            return category;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getPathstring() {
        return pathstring;
    }

    public void setPathstring(String pathstring) {
        this.pathstring = pathstring;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "PartCategory{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parent=" + parent +
                ", pathstring='" + pathstring + '\'' +
                ", level=" + level +
                '}';
    }
}
