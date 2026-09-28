package com.example.wastepickup.repository;

import com.example.wastepickup.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByZoneId(Long zoneId);
    List<Schedule> findByZoneIdAndPickupDayIgnoreCase(Long zoneId, String pickupDay);
}
