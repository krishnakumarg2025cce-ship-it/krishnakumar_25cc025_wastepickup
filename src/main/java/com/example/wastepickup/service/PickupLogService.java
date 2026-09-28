package com.example.wastepickup.service;

import com.example.wastepickup.dto.PickupLogDto;
import com.example.wastepickup.dto.PickupRequest;
import com.example.wastepickup.dto.ReminderDto;
import com.example.wastepickup.entity.Household;
import com.example.wastepickup.entity.PickupLog;
import com.example.wastepickup.entity.Schedule;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.exception.BadRequestException;
import com.example.wastepickup.exception.InvalidPickupException;
import com.example.wastepickup.exception.ResourceNotFoundException;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import com.example.wastepickup.repository.ScheduleRepository;
import com.example.wastepickup.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PickupLogService {

    private final PickupLogRepository pickupLogRepository;
    private final HouseholdRepository householdRepository;
    private final ZoneRepository zoneRepository;
    private final ScheduleRepository scheduleRepository;

    public PickupLogService(PickupLogRepository pickupLogRepository,
                            HouseholdRepository householdRepository,
                            ZoneRepository zoneRepository,
                            ScheduleRepository scheduleRepository) {
        this.pickupLogRepository = pickupLogRepository;
        this.householdRepository = householdRepository;
        this.zoneRepository = zoneRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<PickupLogDto> getAllPickups() {
        return pickupLogRepository.findAllByOrderByPickupDateDescPickupTimeDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PickupLogDto getPickupById(Long id) {
        PickupLog log = pickupLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup record not found with ID: " + id));
        return mapToDto(log);
    }

    @Transactional(readOnly = true)
    public List<PickupLogDto> getPickupsByHousehold(Long householdId) {
        return pickupLogRepository.findByHouseholdIdOrderByPickupDateDescPickupTimeDesc(householdId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PickupLogDto> getPickupsByZone(Long zoneId) {
        return pickupLogRepository.findByZoneId(zoneId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PickupLogDto recordPickup(PickupRequest request) {
        // 1. Validate score is between 0 and 100
        if (request.getSegregationScore() == null || request.getSegregationScore() < 0.0 || request.getSegregationScore() > 100.0) {
            throw new BadRequestException("Segregation score must be between 0 and 100.");
        }

        // Fetch Zone and Household
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + request.getZoneId()));

        Household household = householdRepository.findById(request.getHouseholdId())
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with ID: " + request.getHouseholdId()));

        // 2. Verify that the household belongs to the selected zone
        if (!household.getZone().getId().equals(zone.getId())) {
            throw new InvalidPickupException("Household '" + household.getHouseholdName() +
                    "' belongs to Zone '" + household.getZone().getZoneName() +
                    "', not to selected Zone '" + zone.getZoneName() + "'.");
        }

        // 3. Find schedule for that zone and pickup day
        String pickupDay = request.getPickupDate().getDayOfWeek().name();
        List<Schedule> schedules = scheduleRepository.findByZoneIdAndPickupDayIgnoreCase(zone.getId(), pickupDay);

        if (schedules.isEmpty()) {
            throw new InvalidPickupException("No pickup schedule is configured for Zone '" + zone.getZoneName() + "' on " + pickupDay + ".");
        }

        // 4. Check whether pickup time is inside the scheduled start and end time
        LocalTime pickupTime = request.getPickupTime();
        boolean insideWindow = false;
        for (Schedule schedule : schedules) {
            // inside window if pickupTime >= startTime && pickupTime <= endTime
            if (!pickupTime.isBefore(schedule.getStartTime()) && !pickupTime.isAfter(schedule.getEndTime())) {
                insideWindow = true;
                break;
            }
        }

        // 5. If outside scheduled window, reject pickup with exact required message
        if (!insideWindow) {
            throw new InvalidPickupException("Pickup cannot be recorded outside the scheduled time window.");
        }

        // 8. Compare the score with the minimum threshold & 9. Set status
        double threshold = household.getMinimumScore() != null ? household.getMinimumScore() : 60.0;
        String status = (request.getSegregationScore() >= threshold) ? "Good" : "Needs Improvement";

        // 6. If valid, save the pickup
        PickupLog pickupLog = new PickupLog(
                household,
                zone,
                request.getPickupDate(),
                request.getPickupTime(),
                Math.round(request.getSegregationScore() * 10.0) / 10.0,
                status,
                request.getRemarks()
        );

        PickupLog saved = pickupLogRepository.save(pickupLog);
        return mapToDto(saved);
    }

    @Transactional
    public void deletePickup(Long id) {
        PickupLog log = pickupLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup record not found with ID: " + id));
        pickupLogRepository.delete(log);
    }

    @Transactional(readOnly = true)
    public List<ReminderDto> getReminders() {
        List<Household> households = householdRepository.findAll();
        List<ReminderDto> reminders = new ArrayList<>();

        for (Household h : households) {
            List<PickupLog> logs = pickupLogRepository.findByHouseholdIdOrderByPickupDateDescPickupTimeDesc(h.getId());
            if (logs.isEmpty()) {
                continue;
            }

            double sum = 0.0;
            for (PickupLog l : logs) {
                sum += l.getSegregationScore();
            }
            double avgScore = Math.round((sum / logs.size()) * 10.0) / 10.0;
            PickupLog latestLog = logs.get(0);
            Double latestScore = latestLog.getSegregationScore();
            double minThreshold = h.getMinimumScore() != null ? h.getMinimumScore() : 60.0;

            // Flag if latest score is below threshold OR average score is below threshold
            if (avgScore < minThreshold || latestScore < minThreshold) {
                // Find date of most recent below-threshold pickup
                LocalDate flaggedDate = latestLog.getPickupDate();
                for (PickupLog l : logs) {
                    if (l.getSegregationScore() < minThreshold) {
                        flaggedDate = l.getPickupDate();
                        break;
                    }
                }

                String status = "Needs Improvement";
                String msg = "Your waste segregation score is below the required level. Please separate wet and dry waste properly.";

                reminders.add(new ReminderDto(
                        h.getId(),
                        h.getHouseholdName(),
                        h.getAddress(),
                        h.getZone().getId(),
                        h.getZone().getZoneName(),
                        latestScore,
                        avgScore,
                        minThreshold,
                        status,
                        flaggedDate,
                        msg
                ));
            }
        }

        return reminders;
    }

    private PickupLogDto mapToDto(PickupLog log) {
        return new PickupLogDto(
                log.getId(),
                log.getHousehold().getId(),
                log.getHousehold().getHouseholdName(),
                log.getZone().getId(),
                log.getZone().getZoneName(),
                log.getPickupDate(),
                log.getPickupTime(),
                log.getSegregationScore(),
                log.getStatus(),
                log.getRemarks()
        );
    }
}
