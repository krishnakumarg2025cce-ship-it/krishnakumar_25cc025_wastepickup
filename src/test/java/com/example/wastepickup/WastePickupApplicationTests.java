package com.example.wastepickup;

import com.example.wastepickup.dto.HouseholdDto;
import com.example.wastepickup.dto.PickupLogDto;
import com.example.wastepickup.dto.PickupRequest;
import com.example.wastepickup.dto.ReminderDto;
import com.example.wastepickup.exception.InvalidPickupException;
import com.example.wastepickup.service.HouseholdService;
import com.example.wastepickup.service.PickupLogService;
import com.example.wastepickup.service.ZoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WastePickupApplicationTests {

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private HouseholdService householdService;

    @Autowired
    private PickupLogService pickupLogService;

    @Test
    void contextLoads() {
        assertNotNull(zoneService);
        assertNotNull(householdService);
        assertNotNull(pickupLogService);
    }

    @Test
    @DisplayName("Should verify initial sample data is seeded properly")
    void testSampleDataLoaded() {
        assertTrue(zoneService.getAllZones().size() >= 3);
        assertTrue(householdService.getAllHouseholds().size() >= 9);
        assertTrue(pickupLogService.getAllPickups().size() >= 20);
    }

    @Test
    @DisplayName("Should reject pickup recorded outside scheduled time window")
    void testPickupOutsideScheduledWindow() {
        // Find household in Zone A
        List<HouseholdDto> households = householdService.getAllHouseholds();
        HouseholdDto zoneAHousehold = households.stream()
                .filter(h -> h.getZoneName().contains("Zone A"))
                .findFirst()
                .orElseThrow();

        // Zone A morning schedule ends at 12:00. Attempt pickup at 23:45 at night.
        PickupRequest invalidTimeRequest = new PickupRequest(
                zoneAHousehold.getZoneId(),
                zoneAHousehold.getId(),
                LocalDate.now(),
                LocalTime.of(23, 45),
                85.0,
                "Testing late night pickup outside window"
        );

        InvalidPickupException exception = assertThrows(InvalidPickupException.class, () -> {
            pickupLogService.recordPickup(invalidTimeRequest);
        });

        assertEquals("Pickup cannot be recorded outside the scheduled time window.", exception.getMessage());
    }

    @Test
    @DisplayName("Should reject pickup if household does not belong to selected zone")
    void testHouseholdZoneMismatch() {
        List<HouseholdDto> households = householdService.getAllHouseholds();
        HouseholdDto zoneAHousehold = households.stream()
                .filter(h -> h.getZoneName().contains("Zone A"))
                .findFirst()
                .orElseThrow();

        // Attempt pickup claiming Zone B for a Zone A household
        PickupRequest mismatchRequest = new PickupRequest(
                2L, // Zone B
                zoneAHousehold.getId(),
                LocalDate.now(),
                LocalTime.of(9, 0),
                80.0,
                "Zone mismatch test"
        );

        assertThrows(InvalidPickupException.class, () -> {
            pickupLogService.recordPickup(mismatchRequest);
        });
    }

    @Test
    @DisplayName("Should identify households needing improvement and flag reminders")
    void testRemindersFlagging() {
        List<ReminderDto> reminders = pickupLogService.getReminders();
        assertFalse(reminders.isEmpty(), "Should have flagged households needing improvement");

        // Verify reminder content
        for (ReminderDto reminder : reminders) {
            assertEquals("Needs Improvement", reminder.getStatus());
            assertNotNull(reminder.getReminderMessage());
            assertTrue(reminder.getReminderMessage().contains("separate wet and dry waste properly"));
        }
    }
}
