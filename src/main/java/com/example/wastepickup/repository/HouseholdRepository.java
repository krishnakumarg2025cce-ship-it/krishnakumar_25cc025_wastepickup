package com.example.wastepickup.repository;

import com.example.wastepickup.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    List<Household> findByZoneId(Long zoneId);
    List<Household> findByHouseholdNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address);
    long countByZoneId(Long zoneId);
}
