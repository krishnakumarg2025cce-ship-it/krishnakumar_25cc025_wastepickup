package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.PickupLogDto;
import com.example.wastepickup.dto.PickupRequest;
import com.example.wastepickup.service.PickupLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pickups")
@CrossOrigin(origins = "*")
public class PickupController {

    private final PickupLogService pickupLogService;

    public PickupController(PickupLogService pickupLogService) {
        this.pickupLogService = pickupLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PickupLogDto>>> getAllPickups() {
        List<PickupLogDto> pickups = pickupLogService.getAllPickups();
        return ResponseEntity.ok(ApiResponse.ok("Pickups retrieved successfully", pickups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PickupLogDto>> getPickupById(@PathVariable Long id) {
        PickupLogDto pickup = pickupLogService.getPickupById(id);
        return ResponseEntity.ok(ApiResponse.ok("Pickup retrieved successfully", pickup));
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<ApiResponse<List<PickupLogDto>>> getPickupsByHousehold(@PathVariable Long householdId) {
        List<PickupLogDto> pickups = pickupLogService.getPickupsByHousehold(householdId);
        return ResponseEntity.ok(ApiResponse.ok("Household pickups retrieved successfully", pickups));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<ApiResponse<List<PickupLogDto>>> getPickupsByZone(@PathVariable Long zoneId) {
        List<PickupLogDto> pickups = pickupLogService.getPickupsByZone(zoneId);
        return ResponseEntity.ok(ApiResponse.ok("Zone pickups retrieved successfully", pickups));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PickupLogDto>> recordPickup(@Valid @RequestBody PickupRequest request) {
        PickupLogDto recorded = pickupLogService.recordPickup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Waste pickup successfully recorded and scored!", recorded));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePickup(@PathVariable Long id) {
        pickupLogService.deletePickup(id);
        return ResponseEntity.ok(ApiResponse.ok("Pickup record deleted successfully"));
    }
}
