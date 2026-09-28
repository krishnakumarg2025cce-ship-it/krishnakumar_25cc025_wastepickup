package com.example.wastepickup.dto;

import java.util.List;

public class HouseholdDto {
    private Long id;
    private String householdName;
    private String address;
    private Long zoneId;
    private String zoneName;
    private Double minimumScore;
    private Double averageScore;
    private Double latestScore;
    private long totalPickups;
    private String currentStatus; // "Good" or "Needs Improvement"
    private List<PickupLogDto> recentPickups;

    public HouseholdDto() {
    }

    public HouseholdDto(Long id, String householdName, String address, Long zoneId, String zoneName,
                        Double minimumScore, Double averageScore, Double latestScore,
                        long totalPickups, String currentStatus) {
        this.id = id;
        this.householdName = householdName;
        this.address = address;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.minimumScore = minimumScore;
        this.averageScore = averageScore;
        this.latestScore = latestScore;
        this.totalPickups = totalPickups;
        this.currentStatus = currentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(Double minimumScore) {
        this.minimumScore = minimumScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getLatestScore() {
        return latestScore;
    }

    public void setLatestScore(Double latestScore) {
        this.latestScore = latestScore;
    }

    public long getTotalPickups() {
        return totalPickups;
    }

    public void setTotalPickups(long totalPickups) {
        this.totalPickups = totalPickups;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<PickupLogDto> getRecentPickups() {
        return recentPickups;
    }

    public void setRecentPickups(List<PickupLogDto> recentPickups) {
        this.recentPickups = recentPickups;
    }
}
