package com.example.wastepickup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "households")
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "household_name", nullable = false, length = 150)
    private String householdName;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(name = "minimum_score", nullable = false)
    private Double minimumScore = 60.0;

    @JsonIgnore
    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickupLog> pickupLogs = new ArrayList<>();

    public Household() {
    }

    public Household(String householdName, String address, Zone zone, Double minimumScore) {
        this.householdName = householdName;
        this.address = address;
        this.zone = zone;
        this.minimumScore = (minimumScore != null && minimumScore > 0) ? minimumScore : 60.0;
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Double getMinimumScore() {
        return minimumScore != null ? minimumScore : 60.0;
    }

    public void setMinimumScore(Double minimumScore) {
        this.minimumScore = (minimumScore != null && minimumScore > 0) ? minimumScore : 60.0;
    }

    public List<PickupLog> getPickupLogs() {
        return pickupLogs;
    }

    public void setPickupLogs(List<PickupLog> pickupLogs) {
        this.pickupLogs = pickupLogs;
    }
}
