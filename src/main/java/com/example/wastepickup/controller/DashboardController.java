package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.DashboardStatsDto;
import com.example.wastepickup.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.ok("Dashboard statistics retrieved successfully", stats));
    }
}
