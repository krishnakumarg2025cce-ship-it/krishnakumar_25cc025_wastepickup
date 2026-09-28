package com.example.wastepickup.dto;

import java.time.LocalTime;

public class ScheduleDto {
    private Long id;
    private Long zoneId;
    private String zoneName;
    private String pickupDay;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleDto() {
    }

    public ScheduleDto(Long id, Long zoneId, String zoneName, String pickupDay, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.pickupDay = pickupDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPickupDay() {
        return pickupDay;
    }

    public void setPickupDay(String pickupDay) {
        this.pickupDay = pickupDay;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
