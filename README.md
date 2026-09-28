# WastePickup – Waste Collection Schedule and Segregation Score Tracker

A complete, modern Spring Boot web application designed for municipal waste management, scheduled collection tracking, waste segregation evaluation, and zone-based compliance monitoring.

---

## 📋 Project Overview

In municipal waste collection systems, waste collectors follow fixed pickup schedules across designated zones. **WastePickup** provides a centralized system to:
1. **Manage Collection Zones & Schedules**: Define zones and active operational time windows for every day of the week.
2. **Track Household Segregation**: Record waste pickups with an objective segregation score ($0 - 100$).
3. **Automate Schedule Window Enforcement**: Reject pickups submitted outside the scheduled start and end time window or for mismatched zones.
4. **Calculate Real-Time Performance**: Compute household and zone average segregation scores dynamically from database records.
5. **Flag Defaulters & Generate Reminders**: Automatically flag households scoring below the minimum threshold ($60\%$) and issue targeted counseling alerts.
6. **Visual Analytics**: Interactive dashboard with real-time statistics, Chart.js zone benchmarks, and recent pickup logs.

---

## 🛠️ Technologies Used

- **Backend**: Java 17+, Spring Boot 3.3.4
- **ORM & Persistence**: Spring Data JPA, Hibernate
- **Database**: MySQL Server (`wastepickup_db`) with automatic in-memory H2 fallback for instant out-of-the-box local testing
- **Validation**: Jakarta Validation (`@Valid`, `@NotNull`, `@Min`, `@Max`, `@NotBlank`)
- **Exception Handling**: Global `@ControllerAdvice`
- **Frontend**: Responsive Single-Page UI with HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3, Bootstrap Icons, and Chart.js
- **Build Tool**: Apache Maven

---

## 📁 Project Structure

```text
WastePickup/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/example/wastepickup/
│   │   │   ├── WastePickupApplication.java
│   │   │   ├── config/
│   │   │   │   ├── DatabaseConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── HouseholdController.java
│   │   │   │   ├── PickupController.java
│   │   │   │   ├── ReminderController.java
│   │   │   │   ├── ScheduleController.java
│   │   │   │   ├── ScoreController.java
│   │   │   │   └── ZoneController.java
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── DashboardStatsDto.java
│   │   │   │   ├── HouseholdDto.java
│   │   │   │   ├── HouseholdRequest.java
│   │   │   │   ├── PickupLogDto.java
│   │   │   │   ├── PickupRequest.java
│   │   │   │   ├── ReminderDto.java
│   │   │   │   ├── ScheduleDto.java
│   │   │   │   ├── ScheduleRequest.java
│   │   │   │   ├── ZoneDto.java
│   │   │   │   ├── ZoneRequest.java
│   │   │   │   └── ZoneScoreDto.java
│   │   │   ├── entity/
│   │   │   │   ├── Household.java
│   │   │   │   ├── PickupLog.java
│   │   │   │   ├── Schedule.java
│   │   │   │   └── Zone.java
│   │   │   ├── exception/
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidPickupException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   ├── HouseholdRepository.java
│   │   │   │   ├── PickupLogRepository.java
│   │   │   │   ├── ScheduleRepository.java
│   │   │   │   └── ZoneRepository.java
│   │   │   └── service/
│   │   │       ├── DashboardService.java
│   │   │       ├── HouseholdService.java
│   │   │       ├── PickupLogService.java
│   │   │       ├── ScheduleService.java
│   │   │       └── ZoneService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       ├── schema.sql
│   │       └── static/
│   │           ├── index.html
│   │           ├── css/
│   │           │   └── style.css
│   │           └── js/
│   │               └── app.js
│   └── test/
│       └── java/com/example/wastepickup/
│           └── WastePickupApplicationTests.java
```

---

## 🗄️ Database Setup & Configuration

### 1. Create MySQL Database
Ensure your MySQL server is running, then create the database:
```sql
CREATE DATABASE IF NOT EXISTS wastepickup_db;
```

### 2. Configure `application.properties`
Open `src/main/resources/application.properties`:
```properties
spring.application.name=wastepickup
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/wastepickup_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

> **Note on Zero-Configuration Testing:**
> If `spring.datasource.password` remains `YOUR_MYSQL_PASSWORD_HERE` or MySQL is momentarily unreachable, `DatabaseConfig.java` gracefully activates an in-memory MySQL-mode H2 database with automatic schema creation and sample data seeding so the application starts and can be tested without friction. Once you put your MySQL password, it immediately connects to your local MySQL database.

---

## 🚀 How to Run the Application

### Option A: Using Maven (Terminal / Command Prompt)
1. Navigate to the project root directory:
   ```bash
   cd WastePickup
   ```
2. Build the project:
   ```bash
   mvn clean package -DskipTests
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the web application in any browser:
   ```text
   http://localhost:8080
   ```

### Option B: Using IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Select **File** > **Open** and choose the `WastePickup` directory (or extract `WastePickup.zip` first).
3. IntelliJ will detect `pom.xml` as a Maven project and automatically import all dependencies.
4. Locate `src/main/java/com/example/wastepickup/WastePickupApplication.java`.
5. Right-click and select **Run 'WastePickupApplication'**.
6. Open your web browser and navigate to `http://localhost:8080`.

---

## ⚡ Main Business Logic & Rules

When a collector records a pickup via the form (`/api/pickups`):
1. **Score Validation**: Ensures the score is between $0$ and $100$.
2. **Zone Membership Verification**: Checks that the selected household belongs to the chosen zone. Rejects with an error if there is a mismatch.
3. **Schedule Lookup**: Identifies the day of the week (e.g., `MONDAY`, `TUESDAY`) and retrieves the active schedules for that zone.
4. **Time Window Verification**: Compares the pickup time against the scheduled `startTime` and `endTime`.
5. **Window Rejection**: If the time is outside the valid pickup window, the pickup is rejected with:
   > *"Pickup cannot be recorded outside the scheduled time window."*
6. **Scoring & Status Calculation**:
   - If the score meets or exceeds the household's minimum threshold ($60.0$), status is set to `"Good"`.
   - If below threshold, status is marked `"Needs Improvement"`.
7. **Automated Reminders**: Households with low scores are instantly added to the **Reminders** page with the message:
   > *"Your waste segregation score is below the required level. Please separate wet and dry waste properly."*

---

## 🌐 REST API Endpoints

### 1. Zones (`/api/zones`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/zones` | List all zones with household count & average score |
| `GET` | `/api/zones/{id}` | Get zone details by ID |
| `POST` | `/api/zones` | Create a new zone |
| `PUT` | `/api/zones/{id}` | Update an existing zone |
| `DELETE`| `/api/zones/{id}` | Delete a zone |

### 2. Schedules (`/api/schedules`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/schedules` | List all pickup schedules |
| `GET` | `/api/schedules/{id}` | Get schedule by ID |
| `GET` | `/api/schedules/zone/{zoneId}` | List schedules for a specific zone |
| `POST` | `/api/schedules` | Create a new schedule |
| `PUT` | `/api/schedules/{id}` | Update a schedule |
| `DELETE`| `/api/schedules/{id}` | Delete a schedule |

### 3. Households (`/api/households`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/households` | List all households with current status & scores |
| `GET` | `/api/households/{id}` | Get household profile and recent pickup logs |
| `GET` | `/api/households/search?query=...` | Search households by name or address |
| `GET` | `/api/households/zone/{zoneId}` | Filter households by zone |
| `POST` | `/api/households` | Register a new household |
| `PUT` | `/api/households/{id}` | Update household details |
| `DELETE`| `/api/households/{id}` | Delete a household |

### 4. Waste Pickups (`/api/pickups`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/pickups` | List all recorded pickups |
| `GET` | `/api/pickups/{id}` | Get pickup record by ID |
| `POST` | `/api/pickups` | Record a new pickup (validates schedule & zone) |
| `DELETE`| `/api/pickups/{id}` | Delete a pickup record |

### 5. Segregation Scores & Reminders
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/scores/summary` | Overall average score and total pickups |
| `GET` | `/api/scores/zone-average` | Zone-wise average segregation scores |
| `GET` | `/api/reminders` | Households flagged for improvement with notice |
| `GET` | `/api/dashboard` | Aggregated dashboard metrics & recent activities |

---

## 🧪 Sample API Requests

### 1. Record a Valid Pickup
**Request**:
```http
POST /api/pickups
Content-Type: application/json

{
  "zoneId": 1,
  "householdId": 1,
  "pickupDate": "2026-09-28",
  "pickupTime": "09:30",
  "segregationScore": 88.0,
  "remarks": "Pristine separation of wet organics and dry cardboard"
}
```
**Response (201 Created)**:
```json
{
  "success": true,
  "message": "Waste pickup successfully recorded and scored!",
  "data": {
    "id": 22,
    "householdId": 1,
    "householdName": "The Sharma Residence",
    "zoneId": 1,
    "zoneName": "Zone A - North Hills",
    "pickupDate": "2026-09-28",
    "pickupTime": "09:30:00",
    "segregationScore": 88.0,
    "status": "Good",
    "remarks": "Pristine separation of wet organics and dry cardboard"
  }
}
```

### 2. Record an Invalid Pickup (Outside Schedule Window)
**Request**:
```http
POST /api/pickups
Content-Type: application/json

{
  "zoneId": 1,
  "householdId": 1,
  "pickupDate": "2026-09-28",
  "pickupTime": "23:45",
  "segregationScore": 85.0,
  "remarks": "Late night attempt"
}
```
**Response (400 Bad Request)**:
```json
{
  "success": false,
  "message": "Pickup cannot be recorded outside the scheduled time window.",
  "data": null
}
```

---

## 🖥️ Website Interface Pages

1. **Dashboard**: High-level KPI cards, Chart.js zone bar chart, target status indicators, and recent pickup activity.
2. **Zones**: Table of zones with household quotas, schedule days, and average scores; Add/Edit modals.
3. **Schedules**: Day-wise start and end times for all zones with window duration calculation.
4. **Households**: Search, filter by zone, real-time score progress bars, status badges, and detail profile modal.
5. **Record Pickup**: Collector form with live schedule verification, interactive score slider, and preset remarks.
6. **Pickup History**: Complete audit trail with sorting (by date/score) and status filtering.
7. **Segregation Scores**: Benchmarking cards and comparative chart against municipal target.
8. **Reminders**: Defaulter dashboard displaying flagged households and compliance messages.
