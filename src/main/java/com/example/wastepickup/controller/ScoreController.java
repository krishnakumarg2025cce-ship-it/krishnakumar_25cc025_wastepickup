package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.HouseholdDto;
import com.example.wastepickup.dto.ZoneScoreDto;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import com.example.wastepickup.repository.ZoneRepository;
import com.example.wastepickup.service.HouseholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    private final PickupLogRepository pickupLogRepository;
    private final ZoneRepository zoneRepository;
    private final HouseholdRepository householdRepository;
    private final HouseholdService householdService;

    public ScoreController(PickupLogRepository pickupLogRepository,
                           ZoneRepository zoneRepository,
                           HouseholdRepository householdRepository,
                           HouseholdService householdService) {
        this.pickupLogRepository = pickupLogRepository;
        this.zoneRepository = zoneRepository;
        this.householdRepository = householdRepository;
        this.householdService = householdService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getScoreSummary() {
        Double overallAvg = pickupLogRepository.getOverallAverageScore();
        long totalPickups = pickupLogRepository.count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("overallAverageScore", overallAvg != null ? Math.round(overallAvg * 10.0) / 10.0 : 0.0);
        summary.put("totalPickups", totalPickups);

        return ResponseEntity.ok(ApiResponse.ok("Score summary retrieved", summary));
    }

    @GetMapping("/zone-average")
    public ResponseEntity<ApiResponse<List<ZoneScoreDto>>> getZoneAverages() {
        List<Zone> zones = zoneRepository.findAll();
        List<ZoneScoreDto> zoneScores = new ArrayList<>();

        for (Zone z : zones) {
            Double avg = pickupLogRepository.getAverageScoreByZoneId(z.getId());
            Double roundedAvg = (avg != null) ? Math.round(avg * 10.0) / 10.0 : 0.0;
            long hCount = householdRepository.countByZoneId(z.getId());
            long pCount = pickupLogRepository.countByZoneId(z.getId());
            zoneScores.add(new ZoneScoreDto(z.getId(), z.getZoneName(), roundedAvg, hCount, pCount));
        }

        return ResponseEntity.ok(ApiResponse.ok("Zone averages retrieved successfully", zoneScores));
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<ApiResponse<HouseholdDto>> getHouseholdScore(@PathVariable Long householdId) {
        HouseholdDto dto = householdService.getHouseholdDetails(householdId);
        return ResponseEntity.ok(ApiResponse.ok("Household score details retrieved", dto));
    }
}
