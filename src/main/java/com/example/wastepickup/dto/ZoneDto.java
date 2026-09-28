package com.example.wastepickup.dto;

public class ZoneDto {
    private Long id;
    private String zoneName;
    private String description;
    private long householdCount;
    private Double averageScore;
    private long scheduleCount;

    public ZoneDto() {
    }

    public ZoneDto(Long id, String zoneName, String description, long householdCount, Double averageScore, long scheduleCount) {
        this.id = id;
        this.zoneName = zoneName;
        this.description = description;
        this.householdCount = householdCount;
        this.averageScore = averageScore;
        this.scheduleCount = scheduleCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getHouseholdCount() {
        return householdCount;
    }

    public void setHouseholdCount(long householdCount) {
        this.householdCount = householdCount;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public long getScheduleCount() {
        return scheduleCount;
    }

    public void setScheduleCount(long scheduleCount) {
        this.scheduleCount = scheduleCount;
    }
}
