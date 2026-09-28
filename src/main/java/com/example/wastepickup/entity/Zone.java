package com.example.wastepickup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_name", nullable = false, unique = true, length = 100)
    private String zoneName;

    @Column(name = "description", length = 500)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Household> households = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickupLog> pickupLogs = new ArrayList<>();

    public Zone() {
    }

    public Zone(String zoneName, String description) {
        this.zoneName = zoneName;
        this.description = description;
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

    public List<Household> getHouseholds() {
        return households;
    }

    public void setHouseholds(List<Household> households) {
        this.households = households;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<PickupLog> getPickupLogs() {
        return pickupLogs;
    }

    public void setPickupLogs(List<PickupLog> pickupLogs) {
        this.pickupLogs = pickupLogs;
    }
}
