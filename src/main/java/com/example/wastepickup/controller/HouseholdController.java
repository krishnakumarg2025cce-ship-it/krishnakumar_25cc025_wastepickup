package com.example.wastepickup.controller;

import com.example.wastepickup.dto.ApiResponse;
import com.example.wastepickup.dto.HouseholdDto;
import com.example.wastepickup.dto.HouseholdRequest;
import com.example.wastepickup.service.HouseholdService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/households")
@CrossOrigin(origins = "*")
public class HouseholdController {

    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseholdDto>>> getAllHouseholds() {
        List<HouseholdDto> households = householdService.getAllHouseholds();
        return ResponseEntity.ok(ApiResponse.ok("Households retrieved successfully", households));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseholdDto>> getHouseholdById(@PathVariable Long id) {
        HouseholdDto household = householdService.getHouseholdDetails(id);
        return ResponseEntity.ok(ApiResponse.ok("Household details retrieved successfully", household));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<ApiResponse<List<HouseholdDto>>> getHouseholdsByZone(@PathVariable Long zoneId) {
        List<HouseholdDto> households = householdService.getHouseholdsByZone(zoneId);
        return ResponseEntity.ok(ApiResponse.ok("Zone households retrieved successfully", households));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseholdDto>>> searchHouseholds(@RequestParam(required = false) String query) {
        List<HouseholdDto> results = householdService.searchHouseholds(query);
        return ResponseEntity.ok(ApiResponse.ok("Search completed successfully", results));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseholdDto>> createHousehold(@Valid @RequestBody HouseholdRequest request) {
        HouseholdDto created = householdService.createHousehold(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Household created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseholdDto>> updateHousehold(@PathVariable Long id, @Valid @RequestBody HouseholdRequest request) {
        HouseholdDto updated = householdService.updateHousehold(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Household updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHousehold(@PathVariable Long id) {
        householdService.deleteHousehold(id);
        return ResponseEntity.ok(ApiResponse.ok("Household deleted successfully"));
    }
}
