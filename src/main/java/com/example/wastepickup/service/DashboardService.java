package com.example.wastepickup.service;

import com.example.wastepickup.dto.DashboardStatsDto;
import com.example.wastepickup.dto.PickupLogDto;
import com.example.wastepickup.dto.ZoneScoreDto;
import com.example.wastepickup.entity.Household;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import com.example.wastepickup.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ZoneRepository zoneRepository;
    private final HouseholdRepository householdRepository;
    private final PickupLogRepository pickupLogRepository;
    private final PickupLogService pickupLogService;

    public DashboardService(ZoneRepository zoneRepository,
                            HouseholdRepository householdRepository,
                            PickupLogRepository pickupLogRepository,
                            PickupLogService pickupLogService) {
        this.zoneRepository = zoneRepository;
        this.householdRepository = householdRepository;
        this.pickupLogRepository = pickupLogRepository;
        this.pickupLogService = pickupLogService;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        long totalZones = zoneRepository.count();
        long totalHouseholds = householdRepository.count();
        long todayPickups = pickupLogRepository.countByPickupDate(LocalDate.now());

        Double overallAvg = pickupLogRepository.getOverallAverageScore();
        Double roundedOverallAvg = (overallAvg != null) ? Math.round(overallAvg * 10.0) / 10.0 : 0.0;

        // Calculate count of households needing improvement
        long needingImprovementCount = pickupLogService.getReminders().size();

        // Zone-wise performance scores
        List<Zone> zones = zoneRepository.findAll();
        List<ZoneScoreDto> zoneScores = new ArrayList<>();
        for (Zone z : zones) {
            Double avg = pickupLogRepository.getAverageScoreByZoneId(z.getId());
            Double roundedAvg = (avg != null) ? Math.round(avg * 10.0) / 10.0 : 0.0;
            long hCount = householdRepository.countByZoneId(z.getId());
            long pCount = pickupLogRepository.countByZoneId(z.getId());
            zoneScores.add(new ZoneScoreDto(z.getId(), z.getZoneName(), roundedAvg, hCount, pCount));
        }

        // Recent 10 pickups
        List<PickupLogDto> recentPickups = pickupLogRepository.findTop10ByOrderByPickupDateDescPickupTimeDesc().stream()
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

        return new DashboardStatsDto(
                totalZones,
                totalHouseholds,
                todayPickups,
                roundedOverallAvg,
                needingImprovementCount,
                zoneScores,
                recentPickups
        );
    }
}
