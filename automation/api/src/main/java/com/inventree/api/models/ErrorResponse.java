package com.inventree.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * POJO for error responses.
 * Handles InvenTree API error response format.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    // Field-specific errors (e.g., {"name": ["This field is required"]})
    @JsonProperty("errors")
    private Map<String, List<String>> errors;

    // Non-field errors
    @JsonProperty("non_field_errors")
    private List<String> nonFieldErrors;

    // Default constructor
    public ErrorResponse() {
    }

    // Getters and Setters
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public List<String> getNonFieldErrors() {
        return nonFieldErrors;
    }

    public void setNonFieldErrors(List<String> nonFieldErrors) {
        this.nonFieldErrors = nonFieldErrors;
    }

    /**
     * Gets the first error message from field errors.
     *
     * @param fieldName Field name
     * @return First error message for the field, or null
     */
    public String getFieldError(String fieldName) {
        if (errors != null && errors.containsKey(fieldName)) {
            List<String> fieldErrors = errors.get(fieldName);
            if (fieldErrors != null && !fieldErrors.isEmpty()) {
                return fieldErrors.get(0);
            }
        }
        return null;
    }

    /**
     * Checks if a specific field has an error.
     *
     * @param fieldName Field name
     * @return True if field has errors
     */
    public boolean hasFieldError(String fieldName) {
        return errors != null && errors.containsKey(fieldName);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "detail='" + detail + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", nonFieldErrors=" + nonFieldErrors +
                '}';
    }
}
