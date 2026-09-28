package com.example.wastepickup.service;

import com.example.wastepickup.dto.ScheduleDto;
import com.example.wastepickup.dto.ScheduleRequest;
import com.example.wastepickup.entity.Schedule;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.exception.BadRequestException;
import com.example.wastepickup.exception.ResourceNotFoundException;
import com.example.wastepickup.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ZoneService zoneService;

    public ScheduleService(ScheduleRepository scheduleRepository, ZoneService zoneService) {
        this.scheduleRepository = scheduleRepository;
        this.zoneService = zoneService;
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesByZone(Long zoneId) {
        return scheduleRepository.findByZoneId(zoneId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDto getScheduleById(Long id) {
        Schedule schedule = getScheduleEntity(id);
        return mapToDto(schedule);
    }

    @Transactional(readOnly = true)
    public Schedule getScheduleEntity(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
    }

    @Transactional
    public ScheduleDto createSchedule(ScheduleRequest request) {
        validateScheduleTimes(request);
        String day = validateAndFormatDay(request.getPickupDay());
        Zone zone = zoneService.getZoneEntity(request.getZoneId());

        Schedule schedule = new Schedule(zone, day, request.getStartTime(), request.getEndTime());
        Schedule saved = scheduleRepository.save(schedule);
        return mapToDto(saved);
    }

    @Transactional
    public ScheduleDto updateSchedule(Long id, ScheduleRequest request) {
        validateScheduleTimes(request);
        String day = validateAndFormatDay(request.getPickupDay());
        Schedule schedule = getScheduleEntity(id);
        Zone zone = zoneService.getZoneEntity(request.getZoneId());

        schedule.setZone(zone);
        schedule.setPickupDay(day);
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        Schedule updated = scheduleRepository.save(schedule);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleEntity(id);
        scheduleRepository.delete(schedule);
    }

    private void validateScheduleTimes(ScheduleRequest request) {
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time.");
        }
    }

    private String validateAndFormatDay(String dayStr) {
        try {
            return DayOfWeek.valueOf(dayStr.trim().toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid day of week: '" + dayStr + "'. Expected MONDAY, TUESDAY, etc.");
        }
    }

    private ScheduleDto mapToDto(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId(),
                schedule.getZone().getId(),
                schedule.getZone().getZoneName(),
                schedule.getPickupDay(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
