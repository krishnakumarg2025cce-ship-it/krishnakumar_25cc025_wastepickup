package com.example.wastepickup.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PickupLogDto {
    private Long id;
    private Long householdId;
    private String householdName;
    private Long zoneId;
    private String zoneName;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private Double segregationScore;
    private String status;
    private String remarks;

    public PickupLogDto() {
    }

    public PickupLogDto(Long id, Long householdId, String householdName, Long zoneId, String zoneName,
                        LocalDate pickupDate, LocalTime pickupTime, Double segregationScore,
                        String status, String remarks) {
        this.id = id;
        this.householdId = householdId;
        this.householdName = householdName;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.segregationScore = segregationScore;
        this.status = status;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
