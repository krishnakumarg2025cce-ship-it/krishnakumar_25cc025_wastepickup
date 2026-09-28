package com.example.wastepickup.repository;

import com.example.wastepickup.entity.PickupLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PickupLogRepository extends JpaRepository<PickupLog, Long> {

    List<PickupLog> findByHouseholdIdOrderByPickupDateDescPickupTimeDesc(Long householdId);

    List<PickupLog> findByZoneId(Long zoneId);

    List<PickupLog> findByPickupDate(LocalDate pickupDate);

    List<PickupLog> findAllByOrderByPickupDateDescPickupTimeDesc();

    List<PickupLog> findTop10ByOrderByPickupDateDescPickupTimeDesc();

    @Query("SELECT AVG(p.segregationScore) FROM PickupLog p WHERE p.household.id = :householdId")
    Double getAverageScoreByHouseholdId(@Param("householdId") Long householdId);

    @Query("SELECT AVG(p.segregationScore) FROM PickupLog p WHERE p.zone.id = :zoneId")
    Double getAverageScoreByZoneId(@Param("zoneId") Long zoneId);

    @Query("SELECT AVG(p.segregationScore) FROM PickupLog p")
    Double getOverallAverageScore();

    long countByPickupDate(LocalDate pickupDate);

    long countByZoneId(Long zoneId);
}
