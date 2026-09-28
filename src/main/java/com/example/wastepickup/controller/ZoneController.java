package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.ZoneDto;
import com.example.wastepickup.dto.ZoneRequest;
import com.example.wastepickup.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = "*")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ZoneDto>>> getAllZones() {
        List<ZoneDto> zones = zoneService.getAllZones();
        return ResponseEntity.ok(ApiResponse.ok("Zones retrieved successfully", zones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneDto>> getZoneById(@PathVariable Long id) {
        ZoneDto zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(ApiResponse.ok("Zone retrieved successfully", zone));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ZoneDto>> createZone(@Valid @RequestBody ZoneRequest request) {
        ZoneDto created = zoneService.createZone(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Zone created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneDto>> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneRequest request) {
        ZoneDto updated = zoneService.updateZone(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Zone updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok(ApiResponse.ok("Zone deleted successfully"));
    }
}
