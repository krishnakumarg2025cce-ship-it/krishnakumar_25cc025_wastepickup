package com.example.wastepickup.service;

import com.example.wastepickup.dto.ZoneDto;
import com.example.wastepickup.dto.ZoneRequest;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.exception.BadRequestException;
import com.example.wastepickup.exception.ResourceNotFoundException;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import com.example.wastepickup.repository.ScheduleRepository;
import com.example.wastepickup.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final HouseholdRepository householdRepository;
    private final ScheduleRepository scheduleRepository;
    private final PickupLogRepository pickupLogRepository;

    public ZoneService(ZoneRepository zoneRepository,
                       HouseholdRepository householdRepository,
                       ScheduleRepository scheduleRepository,
                       PickupLogRepository pickupLogRepository) {
        this.zoneRepository = zoneRepository;
        this.householdRepository = householdRepository;
        this.scheduleRepository = scheduleRepository;
        this.pickupLogRepository = pickupLogRepository;
    }

    @Transactional(readOnly = true)
    public List<ZoneDto> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ZoneDto getZoneById(Long id) {
        Zone zone = getZoneEntity(id);
        return mapToDto(zone);
    }

    @Transactional(readOnly = true)
    public Zone getZoneEntity(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + id));
    }

    @Transactional
    public ZoneDto createZone(ZoneRequest request) {
        if (zoneRepository.existsByZoneNameIgnoreCase(request.getZoneName().trim())) {
            throw new BadRequestException("Zone with name '" + request.getZoneName() + "' already exists.");
        }
        Zone zone = new Zone(request.getZoneName().trim(), request.getDescription());
        Zone saved = zoneRepository.save(zone);
        return mapToDto(saved);
    }

    @Transactional
    public ZoneDto updateZone(Long id, ZoneRequest request) {
        Zone zone = getZoneEntity(id);
        String newName = request.getZoneName().trim();
        if (!zone.getZoneName().equalsIgnoreCase(newName) && zoneRepository.existsByZoneNameIgnoreCase(newName)) {
            throw new BadRequestException("Zone with name '" + newName + "' already exists.");
        }
        zone.setZoneName(newName);
        zone.setDescription(request.getDescription());
        Zone updated = zoneRepository.save(zone);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteZone(Long id) {
        Zone zone = getZoneEntity(id);
        zoneRepository.delete(zone);
    }

    private ZoneDto mapToDto(Zone zone) {
        long householdCount = householdRepository.countByZoneId(zone.getId());
        long scheduleCount = scheduleRepository.findByZoneId(zone.getId()).size();
        Double avg = pickupLogRepository.getAverageScoreByZoneId(zone.getId());
        Double roundedAvg = (avg != null) ? Math.round(avg * 10.0) / 10.0 : null;

        return new ZoneDto(
                zone.getId(),
                zone.getZoneName(),
                zone.getDescription(),
                householdCount,
                roundedAvg,
                scheduleCount
        );
    }
}
