package com.example.wastepickup.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HouseholdRequest {

    @NotBlank(message = "Household name is required")
    @Size(max = 150, message = "Household name cannot exceed 150 characters")
    private String householdName;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotNull(message = "Zone ID is required")
    private Long zoneId;

    @DecimalMin(value = "0.0", message = "Minimum score must be at least 0")
    @DecimalMax(value = "100.0", message = "Minimum score cannot exceed 100")
    private Double minimumScore = 60.0;

    public HouseholdRequest() {
    }

    public HouseholdRequest(String householdName, String address, Long zoneId, Double minimumScore) {
        this.householdName = householdName;
        this.address = address;
        this.zoneId = zoneId;
        this.minimumScore = minimumScore != null ? minimumScore : 60.0;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Double getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(Double minimumScore) {
        this.minimumScore = minimumScore != null ? minimumScore : 60.0;
    }
}
