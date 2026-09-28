package com.example.wastepickup.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "pickup_logs")
public class PickupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "pickup_time", nullable = false)
    private LocalTime pickupTime;

    @Column(name = "segregation_score", nullable = false)
    private Double segregationScore;

    @Column(name = "status", nullable = false, length = 30)
    private String status; // "Good" or "Needs Improvement"

    @Column(name = "remarks", length = 500)
    private String remarks;

    public PickupLog() {
    }

    public PickupLog(Household household, Zone zone, LocalDate pickupDate, LocalTime pickupTime, Double segregationScore, String status, String remarks) {
        this.household = household;
        this.zone = zone;
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

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
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
