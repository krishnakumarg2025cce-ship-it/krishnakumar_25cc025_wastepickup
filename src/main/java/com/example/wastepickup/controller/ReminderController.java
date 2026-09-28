package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.ReminderDto;
import com.example.wastepickup.service.PickupLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    private final PickupLogService pickupLogService;

    public ReminderController(PickupLogService pickupLogService) {
        this.pickupLogService = pickupLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReminderDto>>> getReminders() {
        List<ReminderDto> reminders = pickupLogService.getReminders();
        return ResponseEntity.ok(ApiResponse.ok("Reminders retrieved successfully", reminders));
    }
}
