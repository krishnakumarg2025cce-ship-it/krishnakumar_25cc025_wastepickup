-- ========================================================
-- Sample Initial Data for WastePickup
-- ========================================================

USE wastepickup_db;

-- Insert Zones
INSERT IGNORE INTO zones (id, zone_name, description) VALUES
(1, 'Zone A - North Hills', 'Suburban residential district with residential villas and gardens.'),
(2, 'Zone B - Downtown Central', 'Urban high-density commercial, shopping, and apartment complexes.'),
(3, 'Zone C - Green Valley', 'Eco-conscious residential green valley development with community composting.');

-- Insert Schedules
INSERT IGNORE INTO schedules (id, zone_id, pickup_day, start_time, end_time) VALUES
(1, 1, 'MONDAY', '07:00:00', '12:00:00'),
(2, 1, 'WEDNESDAY', '07:00:00', '12:00:00'),
(3, 1, 'FRIDAY', '07:00:00', '12:00:00'),
(4, 1, 'SATURDAY', '08:00:00', '13:00:00'),
(5, 2, 'MONDAY', '08:00:00', '13:00:00'),
(6, 2, 'TUESDAY', '08:00:00', '13:00:00'),
(7, 2, 'THURSDAY', '08:00:00', '13:00:00'),
(8, 2, 'SATURDAY', '09:00:00', '14:00:00'),
(9, 3, 'TUESDAY', '07:30:00', '12:30:00'),
(10, 3, 'THURSDAY', '07:30:00', '12:30:00'),
(11, 3, 'SATURDAY', '08:30:00', '13:30:00');

-- Insert Households
INSERT IGNORE INTO households (id, household_name, address, zone_id, minimum_score) VALUES
(1, 'The Sharma Residence', '12 Maple Avenue, North Hills', 1, 60.0),
(2, 'Smith Family Villa', '45 Pine Crest Rd, North Hills', 1, 65.0),
(3, 'Patel Eco-Home', '88 Highland Way, North Hills', 1, 60.0),
(4, 'Metro Apartments #4B (John Davis)', '101 Downtown Blvd, Apt 4B', 2, 60.0),
(5, 'Urban Loft (Maria Garcia)', '210 Central Square, Suite 12', 2, 60.0),
(6, 'City Towers Apt 801 (Robert Chen)', '305 Market Street, Apt 801', 2, 70.0),
(7, 'Greenfield Cottage (The Greens)', '14 Valley View Lane, Green Valley', 3, 60.0),
(8, 'Riverbend Villa (Priya Nair)', '56 Riverbend Terrace, Green Valley', 3, 65.0),
(9, 'Meadowbrook Residence (David Wilson)', '77 Meadow Road, Green Valley', 3, 60.0);
