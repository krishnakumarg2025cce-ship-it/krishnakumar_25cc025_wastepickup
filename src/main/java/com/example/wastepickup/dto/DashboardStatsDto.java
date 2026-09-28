package com.example.wastepickup.dto;

import java.util.List;

public class DashboardStatsDto {
    private long totalZones;
    private long totalHouseholds;
    private long todayPickups;
    private Double overallAverageScore;
    private long householdsNeedingImprovement;
    private List<ZoneScoreDto> zoneScores;
    private List<PickupLogDto> recentPickups;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(long totalZones, long totalHouseholds, long todayPickups,
                             Double overallAverageScore, long householdsNeedingImprovement,
                             List<ZoneScoreDto> zoneScores, List<PickupLogDto> recentPickups) {
        this.totalZones = totalZones;
        this.totalHouseholds = totalHouseholds;
        this.todayPickups = todayPickups;
        this.overallAverageScore = overallAverageScore;
        this.householdsNeedingImprovement = householdsNeedingImprovement;
        this.zoneScores = zoneScores;
        this.recentPickups = recentPickups;
    }

    public long getTotalZones() {
        return totalZones;
    }

    public void setTotalZones(long totalZones) {
        this.totalZones = totalZones;
    }

    public long getTotalHouseholds() {
        return totalHouseholds;
    }

    public void setTotalHouseholds(long totalHouseholds) {
        this.totalHouseholds = totalHouseholds;
    }

    public long getTodayPickups() {
        return todayPickups;
    }

    public void setTodayPickups(long todayPickups) {
        this.todayPickups = todayPickups;
    }

    public Double getOverallAverageScore() {
        return overallAverageScore;
    }

    public void setOverallAverageScore(Double overallAverageScore) {
        this.overallAverageScore = overallAverageScore;
    }

    public long getHouseholdsNeedingImprovement() {
        return householdsNeedingImprovement;
    }

    public void setHouseholdsNeedingImprovement(long householdsNeedingImprovement) {
        this.householdsNeedingImprovement = householdsNeedingImprovement;
    }

    public List<ZoneScoreDto> getZoneScores() {
        return zoneScores;
    }

    public void setZoneScores(List<ZoneScoreDto> zoneScores) {
        this.zoneScores = zoneScores;
    }

    public List<PickupLogDto> getRecentPickups() {
        return recentPickups;
    }

    public void setRecentPickups(List<PickupLogDto> recentPickups) {
        this.recentPickups = recentPickups;
    }
}
