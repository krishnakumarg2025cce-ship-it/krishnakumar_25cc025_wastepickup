package com.example.wastepickup.dto;

public class ZoneScoreDto {
    private Long zoneId;
    private String zoneName;
    private Double averageScore;
    private long householdCount;
    private long pickupCount;

    public ZoneScoreDto() {
    }

    public ZoneScoreDto(Long zoneId, String zoneName, Double averageScore, long householdCount, long pickupCount) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.averageScore = averageScore;
        this.householdCount = householdCount;
        this.pickupCount = pickupCount;
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

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public long getHouseholdCount() {
        return householdCount;
    }

    public void setHouseholdCount(long householdCount) {
        this.householdCount = householdCount;
    }

    public long getPickupCount() {
        return pickupCount;
    }

    public void setPickupCount(long pickupCount) {
        this.pickupCount = pickupCount;
    }
}
