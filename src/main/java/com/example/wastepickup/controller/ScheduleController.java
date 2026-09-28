package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.ScheduleDto;
import com.example.wastepickup.dto.ScheduleRequest;
import com.example.wastepickup.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleDto>>> getAllSchedules() {
        List<ScheduleDto> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.ok("Schedules retrieved successfully", schedules));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleDto>> getScheduleById(@PathVariable Long id) {
        ScheduleDto schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.ok("Schedule retrieved successfully", schedule));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<ApiResponse<List<ScheduleDto>>> getSchedulesByZone(@PathVariable Long zoneId) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByZone(zoneId);
        return ResponseEntity.ok(ApiResponse.ok("Zone schedules retrieved successfully", schedules));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleDto>> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        ScheduleDto created = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Schedule created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleDto>> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        ScheduleDto updated = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Schedule updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.ok("Schedule deleted successfully"));
    }
}
