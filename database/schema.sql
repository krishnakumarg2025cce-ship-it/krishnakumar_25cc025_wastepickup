-- ========================================================
-- Database Schema for WastePickup
-- Waste Collection Schedule and Segregation Score Tracker
-- ========================================================

CREATE DATABASE IF NOT EXISTS wastepickup_db;
USE wastepickup_db;

-- 1. Zones Table
CREATE TABLE IF NOT EXISTS zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zone_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Schedules Table
CREATE TABLE IF NOT EXISTS schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zone_id BIGINT NOT NULL,
    pickup_day VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_schedules_zone FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Households Table
CREATE TABLE IF NOT EXISTS households (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    household_name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    zone_id BIGINT NOT NULL,
    minimum_score DOUBLE NOT NULL DEFAULT 60.0,
    CONSTRAINT fk_households_zone FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Pickup Logs Table
CREATE TABLE IF NOT EXISTS pickup_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    household_id BIGINT NOT NULL,
    zone_id BIGINT NOT NULL,
    pickup_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    segregation_score DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,
    remarks VARCHAR(500),
    CONSTRAINT fk_pickups_household FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE,
    CONSTRAINT fk_pickups_zone FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
