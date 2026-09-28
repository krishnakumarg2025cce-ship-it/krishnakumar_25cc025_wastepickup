package com.example.wastepickup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ZoneRequest {

    @NotBlank(message = "Zone name is required")
    @Size(max = 100, message = "Zone name cannot exceed 100 characters")
    private String zoneName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    public ZoneRequest() {
    }

    public ZoneRequest(String zoneName, String description) {
        this.zoneName = zoneName;
        this.description = description;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
