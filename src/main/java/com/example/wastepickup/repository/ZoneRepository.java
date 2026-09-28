package com.example.wastepickup.repository;

import com.example.wastepickup.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByZoneNameIgnoreCase(String zoneName);
    Optional<Zone> findByZoneNameIgnoreCase(String zoneName);
}
