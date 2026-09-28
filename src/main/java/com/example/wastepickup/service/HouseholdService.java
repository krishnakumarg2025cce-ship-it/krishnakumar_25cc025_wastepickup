package com.example.wastepickup.service;

import com.example.wastepickup.dto.HouseholdDto;
import com.example.wastepickup.dto.HouseholdRequest;
import com.example.wastepickup.dto.PickupLogDto;
import com.example.wastepickup.entity.Household;
import com.example.wastepickup.entity.PickupLog;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.exception.ResourceNotFoundException;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final ZoneService zoneService;
    private final PickupLogRepository pickupLogRepository;

    public HouseholdService(HouseholdRepository householdRepository,
                            ZoneService zoneService,
                            PickupLogRepository pickupLogRepository) {
        this.householdRepository = householdRepository;
        this.zoneService = zoneService;
        this.pickupLogRepository = pickupLogRepository;
    }

    @Transactional(readOnly = true)
    public List<HouseholdDto> getAllHouseholds() {
        return householdRepository.findAll().stream()
                .map(h -> mapToDto(h, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HouseholdDto> getHouseholdsByZone(Long zoneId) {
        return householdRepository.findByZoneId(zoneId).stream()
                .map(h -> mapToDto(h, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HouseholdDto> searchHouseholds(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllHouseholds();
        }
        String cleanQuery = query.trim();
        return householdRepository.findByHouseholdNameContainingIgnoreCaseOrAddressContainingIgnoreCase(cleanQuery, cleanQuery)
                .stream()
                .map(h -> mapToDto(h, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HouseholdDto getHouseholdDetails(Long id) {
        Household household = getHouseholdEntity(id);
        return mapToDto(household, true);
    }

    @Transactional(readOnly = true)
    public Household getHouseholdEntity(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with ID: " + id));
    }

    @Transactional
    public HouseholdDto createHousehold(HouseholdRequest request) {
        Zone zone = zoneService.getZoneEntity(request.getZoneId());
        Double minScore = (request.getMinimumScore() != null && request.getMinimumScore() >= 0) ? request.getMinimumScore() : 60.0;

        Household household = new Household(
                request.getHouseholdName().trim(),
                request.getAddress().trim(),
                zone,
                minScore
        );
        Household saved = householdRepository.save(household);
        return mapToDto(saved, false);
    }

    @Transactional
    public HouseholdDto updateHousehold(Long id, HouseholdRequest request) {
        Household household = getHouseholdEntity(id);
        Zone zone = zoneService.getZoneEntity(request.getZoneId());
        Double minScore = (request.getMinimumScore() != null && request.getMinimumScore() >= 0) ? request.getMinimumScore() : 60.0;

        household.setHouseholdName(request.getHouseholdName().trim());
        household.setAddress(request.getAddress().trim());
        household.setZone(zone);
        household.setMinimumScore(minScore);

        Household updated = householdRepository.save(household);
        return mapToDto(updated, false);
    }

    @Transactional
    public void deleteHousehold(Long id) {
        Household household = getHouseholdEntity(id);
        householdRepository.delete(household);
    }

    public HouseholdDto mapToDto(Household household, boolean includeHistory) {
        List<PickupLog> logs = pickupLogRepository.findByHouseholdIdOrderByPickupDateDescPickupTimeDesc(household.getId());

        long totalPickups = logs.size();
        Double avg = null;
        Double latest = null;
        String status = "No Pickups";

        if (!logs.isEmpty()) {
            double sum = 0.0;
            for (PickupLog log : logs) {
                sum += log.getSegregationScore();
            }
            avg = Math.round((sum / totalPickups) * 10.0) / 10.0;
            latest = logs.get(0).getSegregationScore();
            double threshold = household.getMinimumScore() != null ? household.getMinimumScore() : 60.0;
            status = (avg >= threshold) ? "Good" : "Needs Improvement";
        }

        HouseholdDto dto = new HouseholdDto(
                household.getId(),
                household.getHouseholdName(),
                household.getAddress(),
                household.getZone().getId(),
                household.getZone().getZoneName(),
                household.getMinimumScore(),
                avg,
                latest,
                totalPickups,
                status
        );

        if (includeHistory) {
            List<PickupLogDto> historyDtos = logs.stream()
                    .map(p -> new PickupLogDto(
                            p.getId(),
                            p.getHousehold().getId(),
                            p.getHousehold().getHouseholdName(),
                            p.getZone().getId(),
                            p.getZone().getZoneName(),
                            p.getPickupDate(),
                            p.getPickupTime(),
                            p.getSegregationScore(),
                            p.getStatus(),
                            p.getRemarks()
                    ))
                    .collect(Collectors.toList());
            dto.setRecentPickups(historyDtos);
        }

        return dto;
    }
}
