package com.example.wastepickup.dto;

import java.time.LocalDate;

public class ReminderDto {
    private Long householdId;
    private String householdName;
    private String address;
    private Long zoneId;
    private String zoneName;
    private Double latestScore;
    private Double averageScore;
    private Double minimumScore;
    private String status;
    private LocalDate dateFlagged;
    private String reminderMessage;

    public ReminderDto() {
    }

    public ReminderDto(Long householdId, String householdName, String address, Long zoneId, String zoneName,
                       Double latestScore, Double averageScore, Double minimumScore, String status,
                       LocalDate dateFlagged, String reminderMessage) {
        this.householdId = householdId;
        this.householdName = householdName;
        this.address = address;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.latestScore = latestScore;
        this.averageScore = averageScore;
        this.minimumScore = minimumScore;
        this.status = status;
        this.dateFlagged = dateFlagged;
        this.reminderMessage = reminderMessage;
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

    public Double getLatestScore() {
        return latestScore;
    }

    public void setLatestScore(Double latestScore) {
        this.latestScore = latestScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(Double minimumScore) {
        this.minimumScore = minimumScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateFlagged() {
        return dateFlagged;
    }

    public void setDateFlagged(LocalDate dateFlagged) {
        this.dateFlagged = dateFlagged;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }
}
