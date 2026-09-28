package com.example.wastepickup.config;

import com.example.wastepickup.entity.Household;
import com.example.wastepickup.entity.PickupLog;
import com.example.wastepickup.entity.Schedule;
import com.example.wastepickup.entity.Zone;
import com.example.wastepickup.repository.HouseholdRepository;
import com.example.wastepickup.repository.PickupLogRepository;
import com.example.wastepickup.repository.ScheduleRepository;
import com.example.wastepickup.repository.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ZoneRepository zoneRepository;
    private final ScheduleRepository scheduleRepository;
    private final HouseholdRepository householdRepository;
    private final PickupLogRepository pickupLogRepository;

    public DataInitializer(ZoneRepository zoneRepository,
                           ScheduleRepository scheduleRepository,
                           HouseholdRepository householdRepository,
                           PickupLogRepository pickupLogRepository) {
        this.zoneRepository = zoneRepository;
        this.scheduleRepository = scheduleRepository;
        this.householdRepository = householdRepository;
        this.pickupLogRepository = pickupLogRepository;
    }

    @Override
    public void run(String... args) {
        if (zoneRepository.count() > 0) {
            log.info("Database already contains data. Skipping sample data initialization.");
            return;
        }

        log.info("Initializing comprehensive sample data for WastePickup application...");

        // 1. Create 3 Zones
        Zone zoneA = new Zone("Zone A - North Hills", "Suburban residential district with residential villas and gardens.");
        Zone zoneB = new Zone("Zone B - Downtown Central", "Urban high-density commercial, shopping, and apartment complexes.");
        Zone zoneC = new Zone("Zone C - Green Valley", "Eco-conscious residential green valley development with community composting.");

        zoneRepository.saveAll(Arrays.asList(zoneA, zoneB, zoneC));

        // 2. Create Schedules for each zone (covering all days of the week so testing works any day)
        // Zone A: MONDAY, WEDNESDAY, FRIDAY, SATURDAY (morning 07:00 - 12:00)
        Schedule sA1 = new Schedule(zoneA, DayOfWeek.MONDAY.name(), LocalTime.of(7, 0), LocalTime.of(12, 0));
        Schedule sA2 = new Schedule(zoneA, DayOfWeek.WEDNESDAY.name(), LocalTime.of(7, 0), LocalTime.of(12, 0));
        Schedule sA3 = new Schedule(zoneA, DayOfWeek.FRIDAY.name(), LocalTime.of(7, 0), LocalTime.of(12, 0));
        Schedule sA4 = new Schedule(zoneA, DayOfWeek.SATURDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sA5 = new Schedule(zoneA, DayOfWeek.SUNDAY.name(), LocalTime.of(8, 0), LocalTime.of(12, 0));
        Schedule sA6 = new Schedule(zoneA, DayOfWeek.TUESDAY.name(), LocalTime.of(7, 0), LocalTime.of(12, 0));
        Schedule sA7 = new Schedule(zoneA, DayOfWeek.THURSDAY.name(), LocalTime.of(7, 0), LocalTime.of(12, 0));

        // Zone B: MONDAY, TUESDAY, THURSDAY, SATURDAY, SUNDAY (morning 08:00 - 13:00)
        Schedule sB1 = new Schedule(zoneB, DayOfWeek.MONDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sB2 = new Schedule(zoneB, DayOfWeek.TUESDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sB3 = new Schedule(zoneB, DayOfWeek.WEDNESDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sB4 = new Schedule(zoneB, DayOfWeek.THURSDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sB5 = new Schedule(zoneB, DayOfWeek.FRIDAY.name(), LocalTime.of(8, 0), LocalTime.of(13, 0));
        Schedule sB6 = new Schedule(zoneB, DayOfWeek.SATURDAY.name(), LocalTime.of(9, 0), LocalTime.of(14, 0));
        Schedule sB7 = new Schedule(zoneB, DayOfWeek.SUNDAY.name(), LocalTime.of(9, 0), LocalTime.of(14, 0));

        // Zone C: TUESDAY, THURSDAY, SATURDAY, SUNDAY, MONDAY, WEDNESDAY, FRIDAY (morning 07:30 - 12:30)
        Schedule sC1 = new Schedule(zoneC, DayOfWeek.MONDAY.name(), LocalTime.of(7, 30), LocalTime.of(12, 30));
        Schedule sC2 = new Schedule(zoneC, DayOfWeek.TUESDAY.name(), LocalTime.of(7, 30), LocalTime.of(12, 30));
        Schedule sC3 = new Schedule(zoneC, DayOfWeek.WEDNESDAY.name(), LocalTime.of(7, 30), LocalTime.of(12, 30));
        Schedule sC4 = new Schedule(zoneC, DayOfWeek.THURSDAY.name(), LocalTime.of(7, 30), LocalTime.of(12, 30));
        Schedule sC5 = new Schedule(zoneC, DayOfWeek.FRIDAY.name(), LocalTime.of(7, 30), LocalTime.of(12, 30));
        Schedule sC6 = new Schedule(zoneC, DayOfWeek.SATURDAY.name(), LocalTime.of(8, 30), LocalTime.of(13, 30));
        Schedule sC7 = new Schedule(zoneC, DayOfWeek.SUNDAY.name(), LocalTime.of(8, 30), LocalTime.of(13, 30));

        scheduleRepository.saveAll(Arrays.asList(
                sA1, sA2, sA3, sA4, sA5, sA6, sA7,
                sB1, sB2, sB3, sB4, sB5, sB6, sB7,
                sC1, sC2, sC3, sC4, sC5, sC6, sC7
        ));

        // 3. Create 9 Households across the 3 zones
        Household h1 = new Household("The Sharma Residence", "12 Maple Avenue, North Hills", zoneA, 60.0);
        Household h2 = new Household("Smith Family Villa", "45 Pine Crest Rd, North Hills", zoneA, 65.0);
        Household h3 = new Household("Patel Eco-Home", "88 Highland Way, North Hills", zoneA, 60.0);

        Household h4 = new Household("Metro Apartments #4B (John Davis)", "101 Downtown Blvd, Apt 4B", zoneB, 60.0);
        Household h5 = new Household("Urban Loft (Maria Garcia)", "210 Central Square, Suite 12", zoneB, 60.0);
        Household h6 = new Household("City Towers Apt 801 (Robert Chen)", "305 Market Street, Apt 801", zoneB, 70.0);

        Household h7 = new Household("Greenfield Cottage (The Greens)", "14 Valley View Lane, Green Valley", zoneC, 60.0);
        Household h8 = new Household("Riverbend Villa (Priya Nair)", "56 Riverbend Terrace, Green Valley", zoneC, 65.0);
        Household h9 = new Household("Meadowbrook Residence (David Wilson)", "77 Meadow Road, Green Valley", zoneC, 60.0);

        List<Household> households = householdRepository.saveAll(Arrays.asList(h1, h2, h3, h4, h5, h6, h7, h8, h9));

        // 4. Create Pickup Records with realistic segregation scores and statuses
        LocalDate today = LocalDate.now();

        // Household 1 (Sharma) - Consistently Excellent (Good)
        PickupLog p1 = new PickupLog(h1, zoneA, today.minusDays(6), LocalTime.of(8, 15), 88.0, "Good", "Excellent dry/wet separation, compost bag clean");
        PickupLog p2 = new PickupLog(h1, zoneA, today.minusDays(4), LocalTime.of(8, 30), 92.0, "Good", "Perfect segregation, zero plastic contamination");
        PickupLog p3 = new PickupLog(h1, zoneA, today.minusDays(2), LocalTime.of(8, 20), 85.0, "Good", "Properly tied color-coded biodegradable bins");

        // Household 2 (Smith) - Good
        PickupLog p4 = new PickupLog(h2, zoneA, today.minusDays(5), LocalTime.of(9, 10), 78.0, "Good", "Acceptable separation, minor paper in wet bin");
        PickupLog p5 = new PickupLog(h2, zoneA, today.minusDays(3), LocalTime.of(9, 0), 82.0, "Good", "Well segregated cardboard and food waste");

        // Household 3 (Patel) - Outstanding (Good)
        PickupLog p6 = new PickupLog(h3, zoneA, today.minusDays(4), LocalTime.of(9, 45), 95.0, "Good", "Master-level organic composting and metal sorting");
        PickupLog p7 = new PickupLog(h3, zoneA, today.minusDays(1), LocalTime.of(9, 30), 90.0, "Good", "Clean packaging materials separated");

        // Household 4 (John Davis) - POOR SEGREGATION (Needs Improvement -> Flaggable)
        PickupLog p8 = new PickupLog(h4, zoneB, today.minusDays(5), LocalTime.of(10, 15), 45.0, "Needs Improvement", "Plastic bottles mixed with wet food waste");
        PickupLog p9 = new PickupLog(h4, zoneB, today.minusDays(3), LocalTime.of(10, 30), 40.0, "Needs Improvement", "Batteries and e-waste dumped in general bin");
        PickupLog p10 = new PickupLog(h4, zoneB, today.minusDays(1), LocalTime.of(10, 0), 50.0, "Needs Improvement", "Unsegregated garbage bags left out");

        // Household 5 (Maria Garcia) - LOW SEGREGATION (Needs Improvement -> Flaggable)
        PickupLog p11 = new PickupLog(h5, zoneB, today.minusDays(6), LocalTime.of(11, 0), 55.0, "Needs Improvement", "Wet waste leaking into recyclable carton bin");
        PickupLog p12 = new PickupLog(h5, zoneB, today.minusDays(2), LocalTime.of(11, 20), 52.0, "Needs Improvement", "Food containers not rinsed before disposal");

        // Household 6 (Robert Chen) - BELOW MINIMUM THRESHOLD (Threshold is 70, score is 64 -> Needs Improvement)
        PickupLog p13 = new PickupLog(h6, zoneB, today.minusDays(4), LocalTime.of(10, 45), 65.0, "Needs Improvement", "Threshold is 70%; unwashed foil wrappers present");
        PickupLog p14 = new PickupLog(h6, zoneB, today.minusDays(1), LocalTime.of(10, 30), 63.0, "Needs Improvement", "Plastic wrap in paper container");

        // Household 7 (The Greens) - Outstanding (Good)
        PickupLog p15 = new PickupLog(h7, zoneC, today.minusDays(5), LocalTime.of(8, 0), 96.0, "Good", "Model household! Clean glass, paper, wet segregated");
        PickupLog p16 = new PickupLog(h7, zoneC, today.minusDays(3), LocalTime.of(8, 15), 94.0, "Good", "Immaculate segregation and compost bin usage");

        // Household 8 (Priya Nair) - Good
        PickupLog p17 = new PickupLog(h8, zoneC, today.minusDays(4), LocalTime.of(8, 45), 84.0, "Good", "Good dry waste sorting, paper flattened");
        PickupLog p18 = new PickupLog(h8, zoneC, today.minusDays(2), LocalTime.of(8, 50), 86.0, "Good", "All items properly categorized");

        // Household 9 (David Wilson) - Recent drop (Needs Improvement -> Flaggable)
        PickupLog p19 = new PickupLog(h9, zoneC, today.minusDays(5), LocalTime.of(9, 15), 62.0, "Good", "Borderline score, some mixed plastics");
        PickupLog p20 = new PickupLog(h9, zoneC, today.minusDays(2), LocalTime.of(9, 30), 48.0, "Needs Improvement", "Wet vegetables and Styrofoam mixed in bin");

        // Today's Pickup record so Dashboard shows today's activity
        PickupLog pToday = new PickupLog(h1, zoneA, today, LocalTime.of(8, 45), 90.0, "Good", "Today's scheduled collection: Pristine segregation");

        pickupLogRepository.saveAll(Arrays.asList(
                p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
                p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, pToday
        ));

        log.info("Sample data initialization completed successfully! Created 3 Zones, 21 Schedules, 9 Households, and 21 Pickup Logs.");
    }
}
