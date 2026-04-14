package com.inventree.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Part entity.
 * Used for request serialization and response deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {

    @JsonProperty("pk")
    private Integer pk;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private Integer category;

    @JsonProperty("IPN")
    private String ipn;

    @JsonProperty("revision")
    private String revision;

    @JsonProperty("keywords")
    private String keywords;

    @JsonProperty("units")
    private String units;

    @JsonProperty("link")
    private String link;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("virtual")
    private Boolean virtual_;

    @JsonProperty("is_template")
    private Boolean isTemplate;

    @JsonProperty("assembly")
    private Boolean assembly;

    @JsonProperty("component")
    private Boolean component;

    @JsonProperty("trackable")
    private Boolean trackable;

    @JsonProperty("purchaseable")
    private Boolean purchaseable;

    @JsonProperty("saleable")
    private Boolean saleable;

    @JsonProperty("minimum_stock")
    private Integer minimumStock;

    @JsonProperty("notes")
    private String notes;

    // Default constructor
    public Part() {
    }

    // Builder pattern
    public static class Builder {
        private Part part = new Part();

        public Builder pk(Integer pk) {
            part.pk = pk;
            return this;
        }

        public Builder name(String name) {
            part.name = name;
            return this;
        }

        public Builder description(String description) {
            part.description = description;
            return this;
        }

        public Builder category(Integer category) {
            part.category = category;
            return this;
        }

        public Builder ipn(String ipn) {
            part.ipn = ipn;
            return this;
        }

        public Builder revision(String revision) {
            part.revision = revision;
            return this;
        }

        public Builder keywords(String keywords) {
            part.keywords = keywords;
            return this;
        }

        public Builder units(String units) {
            part.units = units;
            return this;
        }

        public Builder link(String link) {
            part.link = link;
            return this;
        }

        public Builder active(Boolean active) {
            part.active = active;
            return this;
        }

        public Builder virtual_(Boolean virtual_) {
            part.virtual_ = virtual_;
            return this;
        }

        public Builder isTemplate(Boolean isTemplate) {
            part.isTemplate = isTemplate;
            return this;
        }

        public Builder assembly(Boolean assembly) {
            part.assembly = assembly;
            return this;
        }

        public Builder component(Boolean component) {
            part.component = component;
            return this;
        }

        public Builder trackable(Boolean trackable) {
            part.trackable = trackable;
            return this;
        }

        public Builder purchaseable(Boolean purchaseable) {
            part.purchaseable = purchaseable;
            return this;
        }

        public Builder saleable(Boolean saleable) {
            part.saleable = saleable;
            return this;
        }

        public Builder minimumStock(Integer minimumStock) {
            part.minimumStock = minimumStock;
            return this;
        }

        public Builder notes(String notes) {
            part.notes = notes;
            return this;
        }

        public Part build() {
            return part;
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

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getIpn() {
        return ipn;
    }

    public void setIpn(String ipn) {
        this.ipn = ipn;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getVirtual() {
        return virtual_;
    }

    public void setVirtual(Boolean virtual_) {
        this.virtual_ = virtual_;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Boolean getAssembly() {
        return assembly;
    }

    public void setAssembly(Boolean assembly) {
        this.assembly = assembly;
    }

    public Boolean getComponent() {
        return component;
    }

    public void setComponent(Boolean component) {
        this.component = component;
    }

    public Boolean getTrackable() {
        return trackable;
    }

    public void setTrackable(Boolean trackable) {
        this.trackable = trackable;
    }

    public Boolean getPurchaseable() {
        return purchaseable;
    }

    public void setPurchaseable(Boolean purchaseable) {
        this.purchaseable = purchaseable;
    }

    public Boolean getSaleable() {
        return saleable;
    }

    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Part{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", ipn='" + ipn + '\'' +
                ", active=" + active +
                ", component=" + component +
                ", assembly=" + assembly +
                '}';
    }
}
