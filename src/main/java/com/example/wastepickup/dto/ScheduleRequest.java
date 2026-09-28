package com.example.wastepickup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class ScheduleRequest {

    @NotNull(message = "Zone ID is required")
    private Long zoneId;

    @NotBlank(message = "Pickup day is required (e.g. MONDAY, TUESDAY)")
    private String pickupDay;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public ScheduleRequest() {
    }

    public ScheduleRequest(Long zoneId, String pickupDay, LocalTime startTime, LocalTime endTime) {
        this.zoneId = zoneId;
        this.pickupDay = pickupDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
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
