package com.example.wastepickup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public class PickupRequest {

    @NotNull(message = "Zone is required")
    private Long zoneId;

    @NotNull(message = "Household is required")
    private Long householdId;

    @NotNull(message = "Pickup date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @NotNull(message = "Pickup time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime pickupTime;

    @NotNull(message = "Segregation score is required")
    @DecimalMin(value = "0.0", message = "Segregation score must be at least 0")
    @DecimalMax(value = "100.0", message = "Segregation score cannot exceed 100")
    private Double segregationScore;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    public PickupRequest() {
    }

    public PickupRequest(Long zoneId, Long householdId, LocalDate pickupDate, LocalTime pickupTime, Double segregationScore, String remarks) {
        this.zoneId = zoneId;
        this.householdId = householdId;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.segregationScore = segregationScore;
        this.remarks = remarks;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Double getSegregationScore() {
        return segregationScore;
    }

    public void setSegregationScore(Double segregationScore) {
        this.segregationScore = segregationScore;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
