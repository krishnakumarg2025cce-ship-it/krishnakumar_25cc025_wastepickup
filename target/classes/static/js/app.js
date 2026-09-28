/**
 * WastePickup - Waste Collection Schedule and Segregation Score Tracker
 * Modern Front-End Controller
 */

// Application State
const state = {
  zones: [],
  households: [],
  schedules: [],
  pickups: [],
  reminders: [],
  dashboardStats: null,
  activePage: 'dashboard',
  zoneChart: null,
  scoresChart: null
};

// DOM Content Loaded Initializer
document.addEventListener('DOMContentLoaded', () => {
  initClock();
  setupNavigation();
  initFormDefaults();
  loadAllData();
});

// Live Clock Updater
function initClock() {
  const clockEl = document.getElementById('live-time-display');
  function update() {
    const now = new Date();
    const options = { weekday: 'short', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    if (clockEl) {
      clockEl.textContent = now.toLocaleDateString('en-US', options);
    }
  }
  update();
  setInterval(update, 1000);
}

// Navigation & Page Switching
function setupNavigation() {
  document.querySelectorAll('.sidebar-menu .nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      const page = link.getAttribute('data-page');
      if (page) {
        navigateToPage(page);
      }
    });
  });

  const toggleBtn = document.getElementById('toggle-sidebar-btn');
  if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
      document.getElementById('sidebar').classList.toggle('show');
    });
  }
}

function navigateToPage(pageId) {
  state.activePage = pageId;

  // Update active nav link
  document.querySelectorAll('.sidebar-menu .nav-link').forEach(link => {
    if (link.getAttribute('data-page') === pageId) {
      link.classList.add('active');
    } else {
      link.classList.remove('active');
    }
  });

  // Switch visible page view
  document.querySelectorAll('.page-view').forEach(view => {
    view.classList.remove('active');
  });

  const targetView = document.getElementById(`view-${pageId}`);
  if (targetView) {
    targetView.classList.add('active');
  }

  // Update header text
  const titleEl = document.getElementById('header-page-title');
  const subtitleEl = document.getElementById('header-page-subtitle');
  const meta = {
    'dashboard': { title: 'Dashboard', sub: 'Overview of waste collection schedules and segregation performance' },
    'zones': { title: 'Waste Collection Zones', sub: 'Manage municipal collection zones, household quotas, and average performance' },
    'schedules': { title: 'Collection Schedules', sub: 'Configure pickup day and operational time windows per zone' },
    'households': { title: 'Registered Households', sub: 'Monitor household participation, baseline targets, and segregation standing' },
    'record-pickup': { title: 'Record Waste Pickup', sub: 'Log collection scores and automatically validate against scheduled time windows' },
    'pickup-history': { title: 'Pickup History', sub: 'Search, sort, and inspect past waste collection and segregation logs' },
    'scores': { title: 'Segregation Scores', sub: 'Zone performance benchmarking and compliance analytics' },
    'reminders': { title: 'Segregation Improvement Reminders', sub: 'Active warnings and notices for households below required standards' }
  }[pageId] || { title: 'WastePickup', sub: '' };

  if (titleEl) titleEl.textContent = meta.title;
  if (subtitleEl) subtitleEl.textContent = meta.sub;

  // Refresh page-specific components
  if (pageId === 'dashboard') loadDashboard();
  if (pageId === 'zones') renderZonesTable();
  if (pageId === 'schedules') renderSchedulesTable();
  if (pageId === 'households') renderHouseholdsTable();
  if (pageId === 'record-pickup') refreshRecordPickupForm();
  if (pageId === 'pickup-history') renderPickupHistoryTable();
  if (pageId === 'scores') renderScoresView();
  if (pageId === 'reminders') renderRemindersTable();

  // On mobile close sidebar after selection
  const sidebar = document.getElementById('sidebar');
  if (sidebar && window.innerWidth < 992) {
    sidebar.classList.remove('show');
  }
}

// Initial Data Fetching
async function loadAllData() {
  await Promise.all([
    fetchZones(),
    fetchHouseholds(),
    fetchSchedules(),
    fetchPickups(),
    fetchReminders()
  ]);
  loadDashboard();
}

// API Calls
async function fetchZones() {
  try {
    const res = await fetch('/api/zones');
    const data = await res.json();
    if (data.success) {
      state.zones = data.data;
      populateZoneDropdowns();
      renderZonesTable();
    }
  } catch (err) {
    console.error('Error fetching zones:', err);
  }
}

async function fetchHouseholds() {
  try {
    const res = await fetch('/api/households');
    const data = await res.json();
    if (data.success) {
      state.households = data.data;
      renderHouseholdsTable();
    }
  } catch (err) {
    console.error('Error fetching households:', err);
  }
}

async function fetchSchedules() {
  try {
    const res = await fetch('/api/schedules');
    const data = await res.json();
    if (data.success) {
      state.schedules = data.data;
      renderSchedulesTable();
    }
  } catch (err) {
    console.error('Error fetching schedules:', err);
  }
}

async function fetchPickups() {
  try {
    const res = await fetch('/api/pickups');
    const data = await res.json();
    if (data.success) {
      state.pickups = data.data;
      renderPickupHistoryTable();
    }
  } catch (err) {
    console.error('Error fetching pickups:', err);
  }
}

async function fetchReminders() {
  try {
    const res = await fetch('/api/reminders');
    const data = await res.json();
    if (data.success) {
      state.reminders = data.data;
      updateReminderBadge(state.reminders.length);
      renderRemindersTable();
    }
  } catch (err) {
    console.error('Error fetching reminders:', err);
  }
}

function updateReminderBadge(count) {
  const badge = document.getElementById('sidebar-reminder-badge');
  const countBadge = document.getElementById('reminders-count-badge');
  if (badge) {
    badge.textContent = count;
    badge.style.display = count > 0 ? 'inline-block' : 'none';
  }
  if (countBadge) {
    countBadge.textContent = `${count} Flagged`;
  }
}

// ========================================================
// 1. DASHBOARD CONTROLLER
// ========================================================
async function loadDashboard() {
  try {
    const res = await fetch('/api/dashboard');
    const result = await res.json();
    if (!result.success) return;

    const data = result.data;
    state.dashboardStats = data;

    // Update Counter Cards
    document.getElementById('stat-total-zones').textContent = data.totalZones;
    document.getElementById('stat-total-households').textContent = data.totalHouseholds;
    document.getElementById('stat-today-pickups').textContent = data.todayPickups;
    document.getElementById('stat-overall-avg').textContent = `${data.overallAverageScore || 0}%`;
    document.getElementById('stat-needing-improvement').textContent = data.householdsNeedingImprovement;

    updateReminderBadge(data.householdsNeedingImprovement);

    // Render Dashboard Zone Chart
    renderDashboardChart(data.zoneScores || []);

    // Render Target Status Progress Bars
    renderDashboardZoneList(data.zoneScores || []);

    // Render Recent Pickup Activity
    renderDashboardRecentPickups(data.recentPickups || []);
  } catch (err) {
    console.error('Error loading dashboard stats:', err);
  }
}

function renderDashboardChart(zoneScores) {
  const ctx = document.getElementById('zoneScoreChart');
  if (!ctx) return;

  if (state.zoneChart) {
    state.zoneChart.destroy();
  }

  const labels = zoneScores.map(z => z.zoneName.replace(/ - .*/, ''));
  const values = zoneScores.map(z => z.averageScore || 0);

  state.zoneChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Average Segregation Score (%)',
        data: values,
        backgroundColor: values.map(v => v >= 80 ? 'rgba(46, 125, 50, 0.85)' : (v >= 60 ? 'rgba(2, 132, 199, 0.85)' : 'rgba(220, 38, 38, 0.85)')),
        borderColor: values.map(v => v >= 80 ? '#2e7d32' : (v >= 60 ? '#0284c7' : '#dc2626')),
        borderWidth: 1.5,
        borderRadius: 8
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => ` Average Score: ${ctx.parsed.y}%`
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          max: 100,
          ticks: { stepSize: 20, callback: v => v + '%' },
          grid: { color: '#f1f5f9' }
        },
        x: {
          grid: { display: false }
        }
      }
    }
  });
}

function renderDashboardZoneList(zoneScores) {
  const container = document.getElementById('dashboard-zone-list');
  if (!container) return;

  if (!zoneScores || zoneScores.length === 0) {
    container.innerHTML = '<div class="text-center text-muted py-4">No zone performance data available.</div>';
    return;
  }

  container.innerHTML = zoneScores.map(z => {
    const score = z.averageScore || 0;
    const colorClass = score >= 80 ? 'bg-success' : (score >= 60 ? 'bg-primary' : 'bg-danger');
    const statusText = score >= 80 ? 'High Compliance' : (score >= 60 ? 'Satisfactory' : 'Below Baseline');
    const badgeClass = score >= 80 ? 'text-success' : (score >= 60 ? 'text-primary' : 'text-danger');

    return `
      <div class="mb-3">
        <div class="d-flex justify-content-between align-items-center mb-1">
          <div>
            <strong class="text-dark">${escapeHtml(z.zoneName)}</strong>
            <span class="small ${badgeClass} ms-2 fw-semibold">• ${statusText}</span>
          </div>
          <span class="fw-bold">${score}%</span>
        </div>
        <div class="progress-score">
          <div class="progress-bar ${colorClass}" role="progressbar" style="width: ${score}%;"></div>
        </div>
        <div class="d-flex justify-content-between text-muted small mt-1">
          <span>${z.householdCount} households</span>
          <span>${z.pickupCount} total pickups recorded</span>
        </div>
      </div>
    `;
  }).join('');
}

function renderDashboardRecentPickups(pickups) {
  const tbody = document.getElementById('dashboard-recent-pickups-table');
  if (!tbody) return;

  if (!pickups || pickups.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">No pickups recorded yet.</td></tr>';
    return;
  }

  tbody.innerHTML = pickups.map(p => {
    const isGood = p.status === 'Good';
    const statusBadge = isGood
      ? '<span class="badge-status-good"><i class="bi bi-check-circle"></i> Good</span>'
      : '<span class="badge-status-warning"><i class="bi bi-exclamation-circle"></i> Needs Improvement</span>';

    const scoreColor = p.segregationScore >= 80 ? 'bg-success' : (p.segregationScore >= 60 ? 'bg-primary' : 'bg-danger');

    return `
      <tr>
        <td><strong>${escapeHtml(p.householdName)}</strong></td>
        <td><span class="badge bg-light text-dark border">${escapeHtml(p.zoneName)}</span></td>
        <td>${p.pickupDate} <span class="text-muted small">${formatTime(p.pickupTime)}</span></td>
        <td>
          <div class="d-flex align-items-center gap-2 score-progress-wrap">
            <span class="fw-bold" style="width: 38px;">${p.segregationScore}%</span>
            <div class="progress-score flex-grow-1">
              <div class="progress-bar ${scoreColor}" style="width: ${p.segregationScore}%;"></div>
            </div>
          </div>
        </td>
        <td>${statusBadge}</td>
        <td><small class="text-muted">${escapeHtml(p.remarks || 'None')}</small></td>
      </tr>
    `;
  }).join('');
}

// ========================================================
// 2. ZONES CONTROLLER
// ========================================================
function renderZonesTable() {
  const tbody = document.getElementById('zones-table-body');
  if (!tbody) return;

  if (state.zones.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7" class="text-center py-4 text-muted">No zones found. Add your first zone using the button above.</td></tr>';
    return;
  }

  tbody.innerHTML = state.zones.map(z => {
    const avgScore = z.averageScore !== null && z.averageScore !== undefined ? z.averageScore : null;
    let scoreDisplay = '<span class="text-muted">No Pickups</span>';

    if (avgScore !== null) {
      const color = avgScore >= 80 ? 'bg-success' : (avgScore >= 60 ? 'bg-primary' : 'bg-danger');
      scoreDisplay = `
        <div class="d-flex align-items-center gap-2 score-progress-wrap">
          <strong style="width: 42px;">${avgScore}%</strong>
          <div class="progress-score flex-grow-1">
            <div class="progress-bar ${color}" style="width: ${avgScore}%;"></div>
          </div>
        </div>
      `;
    }

    return `
      <tr>
        <td>#${z.id}</td>
        <td><strong>${escapeHtml(z.zoneName)}</strong></td>
        <td><small class="text-muted">${escapeHtml(z.description || 'No description')}</small></td>
        <td><span class="badge bg-light text-dark border">${z.householdCount} Households</span></td>
        <td><span class="badge bg-light text-dark border">${z.scheduleCount} Days</span></td>
        <td>${scoreDisplay}</td>
        <td class="text-end">
          <button class="btn-action-icon me-1" title="Edit Zone" onclick="openEditZoneModal(${z.id})">
            <i class="bi bi-pencil-fill"></i>
          </button>
          <button class="btn-action-icon delete" title="Delete Zone" onclick="deleteZone(${z.id})">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

function openAddZoneModal() {
  document.getElementById('zoneForm').reset();
  document.getElementById('zone-id-input').value = '';
  document.getElementById('zoneModalTitle').textContent = 'Add New Zone';
  new bootstrap.Modal(document.getElementById('zoneModal')).show();
}

function openEditZoneModal(id) {
  const zone = state.zones.find(z => z.id === id);
  if (!zone) return;

  document.getElementById('zone-id-input').value = zone.id;
  document.getElementById('zone-name-input').value = zone.zoneName;
  document.getElementById('zone-desc-input').value = zone.description || '';
  document.getElementById('zoneModalTitle').textContent = 'Edit Zone';
  new bootstrap.Modal(document.getElementById('zoneModal')).show();
}

async function handleSaveZone(e) {
  e.preventDefault();
  const id = document.getElementById('zone-id-input').value;
  const payload = {
    zoneName: document.getElementById('zone-name-input').value.trim(),
    description: document.getElementById('zone-desc-input').value.trim()
  };

  try {
    const url = id ? `/api/zones/${id}` : '/api/zones';
    const method = id ? 'PUT' : 'POST';
    const res = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (result.success) {
      bootstrap.Modal.getInstance(document.getElementById('zoneModal')).hide();
      showToast(result.message || 'Zone saved successfully!', 'success');
      await fetchZones();
      loadDashboard();
    } else {
      showToast(result.message || 'Failed to save zone.', 'danger');
    }
  } catch (err) {
    showToast('Network error while saving zone.', 'danger');
  }
}

async function deleteZone(id) {
  if (!confirm('Are you sure you want to delete this zone? Note: Associated schedules, households, and logs will also be removed.')) return;

  try {
    const res = await fetch(`/api/zones/${id}`, { method: 'DELETE' });
    const result = await res.json();
    if (result.success) {
      showToast('Zone deleted successfully.', 'success');
      await fetchZones();
      await fetchHouseholds();
      await fetchSchedules();
      loadDashboard();
    } else {
      showToast(result.message || 'Failed to delete zone.', 'danger');
    }
  } catch (err) {
    showToast('Network error while deleting zone.', 'danger');
  }
}

// ========================================================
// 3. SCHEDULES CONTROLLER
// ========================================================
function renderSchedulesTable() {
  const tbody = document.getElementById('schedules-table-body');
  if (!tbody) return;

  const filterZoneId = document.getElementById('schedules-zone-filter').value;
  let list = state.schedules;
  if (filterZoneId) {
    list = list.filter(s => s.zoneId === Number(filterZoneId));
  }

  if (list.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7" class="text-center py-4 text-muted">No schedules found for this selection.</td></tr>';
    return;
  }

  tbody.innerHTML = list.map(s => {
    return `
      <tr>
        <td>#${s.id}</td>
        <td><strong>${escapeHtml(s.zoneName)}</strong></td>
        <td><span class="badge bg-success-subtle text-success fw-bold">${s.pickupDay}</span></td>
        <td><i class="bi bi-clock me-1 text-muted"></i> ${formatTime(s.startTime)}</td>
        <td><i class="bi bi-clock me-1 text-muted"></i> ${formatTime(s.endTime)}</td>
        <td><span class="badge bg-light text-dark border">${calculateWindowDuration(s.startTime, s.endTime)}</span></td>
        <td class="text-end">
          <button class="btn-action-icon me-1" title="Edit Schedule" onclick="openEditScheduleModal(${s.id})">
            <i class="bi bi-pencil-fill"></i>
          </button>
          <button class="btn-action-icon delete" title="Delete Schedule" onclick="deleteSchedule(${s.id})">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

function filterSchedules() {
  renderSchedulesTable();
}

function openAddScheduleModal() {
  document.getElementById('scheduleForm').reset();
  document.getElementById('schedule-id-input').value = '';
  document.getElementById('scheduleModalTitle').textContent = 'Add Collection Schedule';
  populateScheduleZoneSelect();
  new bootstrap.Modal(document.getElementById('scheduleModal')).show();
}

function openEditScheduleModal(id) {
  const s = state.schedules.find(item => item.id === id);
  if (!s) return;

  populateScheduleZoneSelect();
  document.getElementById('schedule-id-input').value = s.id;
  document.getElementById('schedule-zone-input').value = s.zoneId;
  document.getElementById('schedule-day-input').value = s.pickupDay;
  document.getElementById('schedule-start-input').value = formatTimeForInput(s.startTime);
  document.getElementById('schedule-end-input').value = formatTimeForInput(s.endTime);
  document.getElementById('scheduleModalTitle').textContent = 'Edit Collection Schedule';
  new bootstrap.Modal(document.getElementById('scheduleModal')).show();
}

async function handleSaveSchedule(e) {
  e.preventDefault();
  const id = document.getElementById('schedule-id-input').value;
  const payload = {
    zoneId: Number(document.getElementById('schedule-zone-input').value),
    pickupDay: document.getElementById('schedule-day-input').value,
    startTime: document.getElementById('schedule-start-input').value,
    endTime: document.getElementById('schedule-end-input').value
  };

  try {
    const url = id ? `/api/schedules/${id}` : '/api/schedules';
    const method = id ? 'PUT' : 'POST';
    const res = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (result.success) {
      bootstrap.Modal.getInstance(document.getElementById('scheduleModal')).hide();
      showToast(result.message || 'Schedule saved successfully!', 'success');
      await fetchSchedules();
      await fetchZones();
    } else {
      showToast(result.message || 'Failed to save schedule.', 'danger');
    }
  } catch (err) {
    showToast('Network error while saving schedule.', 'danger');
  }
}

async function deleteSchedule(id) {
  if (!confirm('Are you sure you want to delete this schedule?')) return;
  try {
    const res = await fetch(`/api/schedules/${id}`, { method: 'DELETE' });
    const result = await res.json();
    if (result.success) {
      showToast('Schedule deleted successfully.', 'success');
      await fetchSchedules();
      await fetchZones();
    } else {
      showToast(result.message || 'Failed to delete schedule.', 'danger');
    }
  } catch (err) {
    showToast('Network error while deleting schedule.', 'danger');
  }
}

// ========================================================
// 4. HOUSEHOLDS CONTROLLER
// ========================================================
function renderHouseholdsTable() {
  const tbody = document.getElementById('households-table-body');
  if (!tbody) return;

  const searchQuery = document.getElementById('household-search-input').value.toLowerCase().trim();
  const filterZoneId = document.getElementById('household-zone-filter').value;

  let list = state.households;
  if (filterZoneId) {
    list = list.filter(h => h.zoneId === Number(filterZoneId));
  }
  if (searchQuery) {
    list = list.filter(h => h.householdName.toLowerCase().includes(searchQuery) || h.address.toLowerCase().includes(searchQuery));
  }

  if (list.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4 text-muted">No households matched your filter.</td></tr>';
    return;
  }

  tbody.innerHTML = list.map(h => {
    const hasLogs = h.totalPickups > 0;
    const isGood = h.currentStatus === 'Good';
    const statusBadge = hasLogs
      ? (isGood
          ? '<span class="badge-status-good"><i class="bi bi-check-circle"></i> Good</span>'
          : '<span class="badge-status-warning"><i class="bi bi-exclamation-circle"></i> Needs Improvement</span>')
      : '<span class="badge-status-neutral">No Pickups</span>';

    const avgScore = h.averageScore !== null && h.averageScore !== undefined ? h.averageScore : null;
    let scoreDisplay = '<span class="text-muted">--</span>';
    if (avgScore !== null) {
      const color = avgScore >= (h.minimumScore || 60) ? 'bg-success' : 'bg-danger';
      scoreDisplay = `
        <div class="d-flex align-items-center gap-2 score-progress-wrap">
          <strong style="width: 38px;">${avgScore}%</strong>
          <div class="progress-score flex-grow-1">
            <div class="progress-bar ${color}" style="width: ${avgScore}%;"></div>
          </div>
        </div>
      `;
    }

    const latestDisplay = h.latestScore !== null && h.latestScore !== undefined
      ? `<span class="fw-bold ${h.latestScore >= (h.minimumScore || 60) ? 'text-success' : 'text-danger'}">${h.latestScore}%</span>`
      : '<span class="text-muted">--</span>';

    return `
      <tr>
        <td><strong>${escapeHtml(h.householdName)}</strong></td>
        <td><small class="text-muted">${escapeHtml(h.address)}</small></td>
        <td><span class="badge bg-light text-dark border">${escapeHtml(h.zoneName)}</span></td>
        <td><span class="badge bg-secondary-subtle text-secondary">${h.minimumScore}%</span></td>
        <td>${scoreDisplay}</td>
        <td>${latestDisplay}</td>
        <td>${statusBadge}</td>
        <td class="text-end">
          <button class="btn-action-icon me-1 text-primary" title="View Profile & History" onclick="viewHouseholdDetails(${h.id})">
            <i class="bi bi-eye-fill"></i>
          </button>
          <button class="btn-action-icon me-1" title="Edit Household" onclick="openEditHouseholdModal(${h.id})">
            <i class="bi bi-pencil-fill"></i>
          </button>
          <button class="btn-action-icon delete" title="Delete Household" onclick="deleteHousehold(${h.id})">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

function searchHouseholds() {
  renderHouseholdsTable();
}

function filterHouseholdsByZone() {
  renderHouseholdsTable();
}

function openAddHouseholdModal() {
  document.getElementById('householdForm').reset();
  document.getElementById('household-id-input').value = '';
  document.getElementById('household-minscore-input').value = '60';
  document.getElementById('householdModalTitle').textContent = 'Add New Household';
  populateHouseholdZoneSelect();
  new bootstrap.Modal(document.getElementById('householdModal')).show();
}

function openEditHouseholdModal(id) {
  const h = state.households.find(item => item.id === id);
  if (!h) return;

  populateHouseholdZoneSelect();
  document.getElementById('household-id-input').value = h.id;
  document.getElementById('household-name-input').value = h.householdName;
  document.getElementById('household-address-input').value = h.address;
  document.getElementById('household-zone-input').value = h.zoneId;
  document.getElementById('household-minscore-input').value = h.minimumScore || 60;
  document.getElementById('householdModalTitle').textContent = 'Edit Household';
  new bootstrap.Modal(document.getElementById('householdModal')).show();
}

async function handleSaveHousehold(e) {
  e.preventDefault();
  const id = document.getElementById('household-id-input').value;
  const payload = {
    householdName: document.getElementById('household-name-input').value.trim(),
    address: document.getElementById('household-address-input').value.trim(),
    zoneId: Number(document.getElementById('household-zone-input').value),
    minimumScore: parseFloat(document.getElementById('household-minscore-input').value)
  };

  try {
    const url = id ? `/api/households/${id}` : '/api/households';
    const method = id ? 'PUT' : 'POST';
    const res = await fetch(url, {
      method: method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (result.success) {
      bootstrap.Modal.getInstance(document.getElementById('householdModal')).hide();
      showToast(result.message || 'Household saved successfully!', 'success');
      await fetchHouseholds();
      await fetchZones();
      loadDashboard();
    } else {
      showToast(result.message || 'Failed to save household.', 'danger');
    }
  } catch (err) {
    showToast('Network error while saving household.', 'danger');
  }
}

async function deleteHousehold(id) {
  if (!confirm('Are you sure you want to delete this household? All pickup records for this household will also be deleted.')) return;
  try {
    const res = await fetch(`/api/households/${id}`, { method: 'DELETE' });
    const result = await res.json();
    if (result.success) {
      showToast('Household deleted successfully.', 'success');
      await fetchHouseholds();
      await fetchZones();
      await fetchPickups();
      await fetchReminders();
      loadDashboard();
    } else {
      showToast(result.message || 'Failed to delete household.', 'danger');
    }
  } catch (err) {
    showToast('Network error while deleting household.', 'danger');
  }
}

// 11. Household Details View
async function viewHouseholdDetails(id) {
  try {
    const res = await fetch(`/api/households/${id}`);
    const result = await res.json();
    if (!result.success) {
      showToast('Could not load household details.', 'danger');
      return;
    }

    const h = result.data;
    const body = document.getElementById('household-details-body');

    const isGood = h.currentStatus === 'Good';
    const statusBadge = h.totalPickups > 0
      ? (isGood
          ? '<span class="badge bg-success fs-6"><i class="bi bi-check-circle"></i> Good Segregation</span>'
          : '<span class="badge bg-danger fs-6"><i class="bi bi-exclamation-triangle"></i> Needs Improvement</span>')
      : '<span class="badge bg-secondary fs-6">No History Yet</span>';

    const historyRows = (h.recentPickups || []).map(p => `
      <tr>
        <td>${p.pickupDate} <span class="text-muted small">${formatTime(p.pickupTime)}</span></td>
        <td>
          <span class="badge ${p.segregationScore >= h.minimumScore ? 'bg-success' : 'bg-danger'}">${p.segregationScore}%</span>
        </td>
        <td>${p.status === 'Good' ? '<span class="text-success fw-bold">Good</span>' : '<span class="text-danger fw-bold">Needs Improvement</span>'}</td>
        <td><small class="text-muted">${escapeHtml(p.remarks || 'None')}</small></td>
      </tr>
    `).join('');

    body.innerHTML = `
      <div class="row g-3 mb-4">
        <div class="col-md-7">
          <h4 class="fw-bold mb-1">${escapeHtml(h.householdName)}</h4>
          <p class="text-muted mb-2"><i class="bi bi-geo-alt"></i> ${escapeHtml(h.address)}</p>
          <div class="d-flex align-items-center gap-2">
            <span class="badge bg-light text-dark border"><i class="bi bi-pin-map"></i> ${escapeHtml(h.zoneName)}</span>
            <span class="badge bg-light text-dark border"><i class="bi bi-bullseye"></i> Target Min: ${h.minimumScore}%</span>
          </div>
        </div>
        <div class="col-md-5 text-md-end">
          <div class="mb-2">${statusBadge}</div>
          <button class="btn btn-sm btn-eco-outline" onclick="prefillRecordPickup(${h.zoneId}, ${h.id})">
            <i class="bi bi-plus-circle"></i> Record New Pickup
          </button>
        </div>
      </div>

      <!-- Performance Metrics Cards -->
      <div class="row g-2 mb-4 text-center">
        <div class="col-4">
          <div class="p-3 bg-light rounded-3">
            <span class="text-muted small d-block">Average Score</span>
            <h3 class="fw-bold mb-0 ${h.averageScore >= h.minimumScore ? 'text-success' : 'text-danger'}">${h.averageScore !== null ? h.averageScore + '%' : '--'}</h3>
          </div>
        </div>
        <div class="col-4">
          <div class="p-3 bg-light rounded-3">
            <span class="text-muted small d-block">Latest Score</span>
            <h3 class="fw-bold mb-0 ${h.latestScore >= h.minimumScore ? 'text-success' : 'text-danger'}">${h.latestScore !== null ? h.latestScore + '%' : '--'}</h3>
          </div>
        </div>
        <div class="col-4">
          <div class="p-3 bg-light rounded-3">
            <span class="text-muted small d-block">Total Pickups</span>
            <h3 class="fw-bold mb-0 text-dark">${h.totalPickups}</h3>
          </div>
        </div>
      </div>

      <!-- History Table -->
      <h6 class="fw-bold mb-2"><i class="bi bi-clock-history text-success"></i> Pickup & Scoring History</h6>
      <div class="table-responsive" style="max-height: 250px;">
        <table class="table table-sm table-custom">
          <thead>
            <tr>
              <th>Date & Time</th>
              <th>Score</th>
              <th>Status</th>
              <th>Remarks</th>
            </tr>
          </thead>
          <tbody>
            ${historyRows.length > 0 ? historyRows : '<tr><td colspan="4" class="text-center py-3 text-muted">No pickup records logged for this household yet.</td></tr>'}
          </tbody>
        </table>
      </div>
    `;

    new bootstrap.Modal(document.getElementById('householdDetailsModal')).show();
  } catch (err) {
    showToast('Failed to retrieve household details.', 'danger');
  }
}

// ========================================================
// 5. RECORD PICKUP CONTROLLER
// ========================================================
function initFormDefaults() {
  const dateInput = document.getElementById('pickup-date-input');
  const timeInput = document.getElementById('pickup-time-input');

  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');

  if (dateInput) dateInput.value = `${year}-${month}-${day}`;
  if (timeInput) timeInput.value = `${hours}:${minutes}`;
}

function refreshRecordPickupForm() {
  populateZoneDropdowns();
  checkScheduleWindow();
}

function onPickupZoneChange() {
  const zoneId = document.getElementById('pickup-zone-select').value;
  const householdSelect = document.getElementById('pickup-household-select');
  const hintEl = document.getElementById('pickup-zone-schedule-hint');

  householdSelect.innerHTML = '<option value="">-- Choose Household --</option>';

  if (!zoneId) {
    if (hintEl) hintEl.textContent = 'Select a zone to view its collection schedules.';
    checkScheduleWindow();
    return;
  }

  // Filter households belonging to this zone
  const zoneHouseholds = state.households.filter(h => h.zoneId === Number(zoneId));
  zoneHouseholds.forEach(h => {
    const opt = document.createElement('option');
    opt.value = h.id;
    opt.textContent = `${h.householdName} (${h.address})`;
    householdSelect.appendChild(opt);
  });

  // Display zone schedules hint
  const zoneSchedules = state.schedules.filter(s => s.zoneId === Number(zoneId));
  if (zoneSchedules.length > 0) {
    const summary = zoneSchedules.map(s => `${s.pickupDay}: ${formatTime(s.startTime)}-${formatTime(s.endTime)}`).join(', ');
    if (hintEl) hintEl.textContent = `Schedules: ${summary}`;
  } else {
    if (hintEl) hintEl.textContent = 'No schedules registered for this zone.';
  }

  checkScheduleWindow();
}

function checkScheduleWindow() {
  const zoneId = document.getElementById('pickup-zone-select').value;
  const dateVal = document.getElementById('pickup-date-input').value;
  const timeVal = document.getElementById('pickup-time-input').value;
  const box = document.getElementById('schedule-verification-box');
  const text = document.getElementById('schedule-verification-text');

  if (!box || !text) return;

  if (!zoneId || !dateVal || !timeVal) {
    box.className = 'alert alert-secondary py-2 px-3 small d-flex align-items-center gap-2 mb-0';
    text.textContent = 'Select zone, pickup date, and time to verify the schedule window.';
    return;
  }

  const dateObj = new Date(dateVal + 'T12:00:00');
  const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
  const dayName = days[dateObj.getDay()];

  // Find schedules for this zone on this day
  const zoneSchedules = state.schedules.filter(s => s.zoneId === Number(zoneId) && s.pickupDay.toUpperCase() === dayName);

  if (zoneSchedules.length === 0) {
    box.className = 'alert alert-danger py-2 px-3 small d-flex align-items-center gap-2 mb-0';
    text.innerHTML = `<strong>Notice:</strong> No collection scheduled for this zone on <strong>${dayName}</strong>. Submission will be rejected.`;
    return;
  }

  // Check if timeVal is within any schedule
  const pickupMins = parseTimeToMinutes(timeVal);
  let validWindow = null;

  for (const s of zoneSchedules) {
    const startMins = parseTimeToMinutes(s.startTime);
    const endMins = parseTimeToMinutes(s.endTime);
    if (pickupMins >= startMins && pickupMins <= endMins) {
      validWindow = s;
      break;
    }
  }

  if (validWindow) {
    box.className = 'alert alert-success py-2 px-3 small d-flex align-items-center gap-2 mb-0';
    text.innerHTML = `<strong>Valid Schedule Window:</strong> Inside scheduled pickup window for <strong>${dayName}</strong> (${formatTime(validWindow.startTime)} – ${formatTime(validWindow.endTime)}).`;
  } else {
    const windowsText = zoneSchedules.map(s => `${formatTime(s.startTime)} – ${formatTime(s.endTime)}`).join(' & ');
    box.className = 'alert alert-warning py-2 px-3 small d-flex align-items-center gap-2 mb-0';
    text.innerHTML = `<strong>Warning:</strong> Selected time (${timeVal}) is outside the scheduled window for <strong>${dayName}</strong> (${windowsText}).`;
  }
}

function onScoreSliderChange(val) {
  document.getElementById('pickup-score-value').textContent = val;
  const badgeContainer = document.getElementById('pickup-score-badge');
  const num = Number(val);

  if (num >= 80) {
    badgeContainer.innerHTML = '<span class="badge bg-success">Good (Optimal)</span>';
  } else if (num >= 60) {
    badgeContainer.innerHTML = '<span class="badge bg-primary">Good (Standard)</span>';
  } else {
    badgeContainer.innerHTML = '<span class="badge bg-danger">Needs Improvement</span>';
  }
}

function setRemark(text) {
  document.getElementById('pickup-remarks-input').value = text;
}

async function handleRecordPickup(e) {
  e.preventDefault();

  const zoneId = Number(document.getElementById('pickup-zone-select').value);
  const householdId = Number(document.getElementById('pickup-household-select').value);
  const pickupDate = document.getElementById('pickup-date-input').value;
  const pickupTime = document.getElementById('pickup-time-input').value;
  const segregationScore = parseFloat(document.getElementById('pickup-score-slider').value);
  const remarks = document.getElementById('pickup-remarks-input').value.trim();

  const submitBtn = document.getElementById('submit-pickup-btn');
  submitBtn.disabled = true;
  submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Validating Schedule & Saving...';

  const payload = {
    zoneId: zoneId,
    householdId: householdId,
    pickupDate: pickupDate,
    pickupTime: pickupTime,
    segregationScore: segregationScore,
    remarks: remarks
  };

  try {
    const res = await fetch('/api/pickups', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (result.success) {
      showToast(result.message || 'Waste pickup successfully recorded and scored!', 'success');
      // Reset form
      document.getElementById('record-pickup-form').reset();
      initFormDefaults();
      onScoreSliderChange(80);
      document.getElementById('pickup-score-slider').value = 80;

      // Refresh all state
      await Promise.all([
        fetchPickups(),
        fetchHouseholds(),
        fetchReminders()
      ]);
      loadDashboard();

      // Navigate to pickup history
      setTimeout(() => {
        navigateToPage('pickup-history');
      }, 700);
    } else {
      // Show exact rejection message
      alert(`Validation Error:\n${result.message}`);
      showToast(result.message, 'danger');
    }
  } catch (err) {
    showToast('Failed to record pickup due to network error.', 'danger');
  } finally {
    submitBtn.disabled = false;
    submitBtn.innerHTML = '<i class="bi bi-check-circle-fill"></i> Save and Score Waste Pickup';
  }
}

function prefillRecordPickup(zoneId, householdId) {
  const modal = bootstrap.Modal.getInstance(document.getElementById('householdDetailsModal'));
  if (modal) modal.hide();

  navigateToPage('record-pickup');
  setTimeout(() => {
    document.getElementById('pickup-zone-select').value = zoneId;
    onPickupZoneChange();
    document.getElementById('pickup-household-select').value = householdId;
    checkScheduleWindow();
  }, 100);
}

// ========================================================
// 6. PICKUP HISTORY CONTROLLER
// ========================================================
function renderPickupHistoryTable() {
  const tbody = document.getElementById('pickup-history-table-body');
  if (!tbody) return;

  const searchQuery = (document.getElementById('history-search-input')?.value || '').toLowerCase().trim();
  const filterZoneId = document.getElementById('history-zone-filter')?.value || '';
  const filterStatus = document.getElementById('history-status-filter')?.value || '';
  const sortOrder = document.getElementById('history-sort-select')?.value || 'date-desc';

  let list = [...state.pickups];

  if (filterZoneId) {
    list = list.filter(p => p.zoneId === Number(filterZoneId));
  }
  if (filterStatus) {
    list = list.filter(p => p.status === filterStatus);
  }
  if (searchQuery) {
    list = list.filter(p => p.householdName.toLowerCase().includes(searchQuery));
  }

  // Sorting
  list.sort((a, b) => {
    if (sortOrder === 'date-desc') {
      return (b.pickupDate + ' ' + b.pickupTime).localeCompare(a.pickupDate + ' ' + a.pickupTime);
    } else if (sortOrder === 'date-asc') {
      return (a.pickupDate + ' ' + a.pickupTime).localeCompare(b.pickupDate + ' ' + b.pickupTime);
    } else if (sortOrder === 'score-desc') {
      return b.segregationScore - a.segregationScore;
    } else if (sortOrder === 'score-asc') {
      return a.segregationScore - b.segregationScore;
    }
    return 0;
  });

  if (list.length === 0) {
    tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4 text-muted">No pickup records matched the current filter.</td></tr>';
    return;
  }

  tbody.innerHTML = list.map(p => {
    const isGood = p.status === 'Good';
    const statusBadge = isGood
      ? '<span class="badge-status-good"><i class="bi bi-check-circle"></i> Good</span>'
      : '<span class="badge-status-warning"><i class="bi bi-exclamation-circle"></i> Needs Improvement</span>';

    const color = p.segregationScore >= 80 ? 'bg-success' : (p.segregationScore >= 60 ? 'bg-primary' : 'bg-danger');

    return `
      <tr>
        <td><strong>${escapeHtml(p.householdName)}</strong></td>
        <td><span class="badge bg-light text-dark border">${escapeHtml(p.zoneName)}</span></td>
        <td>${p.pickupDate}</td>
        <td><small class="text-muted"><i class="bi bi-clock"></i> ${formatTime(p.pickupTime)}</small></td>
        <td>
          <div class="d-flex align-items-center gap-2 score-progress-wrap">
            <strong style="width: 38px;">${p.segregationScore}%</strong>
            <div class="progress-score flex-grow-1">
              <div class="progress-bar ${color}" style="width: ${p.segregationScore}%;"></div>
            </div>
          </div>
        </td>
        <td>${statusBadge}</td>
        <td><small class="text-muted">${escapeHtml(p.remarks || 'None')}</small></td>
        <td class="text-end">
          <button class="btn-action-icon delete" title="Delete Pickup Record" onclick="deletePickup(${p.id})">
            <i class="bi bi-trash-fill"></i>
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

function filterPickupHistory() {
  renderPickupHistoryTable();
}

async function deletePickup(id) {
  if (!confirm('Are you sure you want to delete this pickup record?')) return;
  try {
    const res = await fetch(`/api/pickups/${id}`, { method: 'DELETE' });
    const result = await res.json();
    if (result.success) {
      showToast('Pickup record deleted.', 'success');
      await Promise.all([
        fetchPickups(),
        fetchHouseholds(),
        fetchReminders()
      ]);
      loadDashboard();
    } else {
      showToast(result.message || 'Failed to delete pickup.', 'danger');
    }
  } catch (err) {
    showToast('Network error while deleting pickup.', 'danger');
  }
}

// ========================================================
// 7. SEGREGATION SCORES CONTROLLER
// ========================================================
async function renderScoresView() {
  try {
    const res = await fetch('/api/scores/zone-average');
    const result = await res.json();
    if (!result.success) return;

    const zoneScores = result.data;
    const cardsRow = document.getElementById('scores-zone-cards-row');

    if (cardsRow) {
      cardsRow.innerHTML = zoneScores.map(z => {
        const score = z.averageScore || 0;
        const colorClass = score >= 80 ? 'text-success' : (score >= 60 ? 'text-primary' : 'text-danger');
        const badgeBg = score >= 80 ? 'bg-success' : (score >= 60 ? 'bg-primary' : 'bg-danger');

        return `
          <div class="col-md-4">
            <div class="stats-card flex-column align-items-start">
              <div class="d-flex justify-content-between w-100 align-items-center mb-2">
                <span class="badge ${badgeBg}">${escapeHtml(z.zoneName)}</span>
                <span class="fw-bold ${colorClass} fs-4">${score}%</span>
              </div>
              <div class="progress-score w-100 mb-2">
                <div class="progress-bar ${badgeBg}" style="width: ${score}%;"></div>
              </div>
              <div class="d-flex justify-content-between w-100 text-muted small">
                <span>${z.householdCount} Registered Households</span>
                <span>${z.pickupCount} Pickups</span>
              </div>
            </div>
          </div>
        `;
      }).join('');
    }

    // Benchmark comparison chart
    const ctx = document.getElementById('scoresComparisonChart');
    if (ctx) {
      if (state.scoresChart) state.scoresChart.destroy();

      state.scoresChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: zoneScores.map(z => z.zoneName),
          datasets: [
            {
              label: 'Zone Average Score (%)',
              data: zoneScores.map(z => z.averageScore || 0),
              backgroundColor: 'rgba(46, 125, 50, 0.8)',
              borderColor: '#2e7d32',
              borderWidth: 1.5,
              borderRadius: 8
            },
            {
              type: 'line',
              label: 'Target Baseline Threshold (60%)',
              data: zoneScores.map(() => 60),
              borderColor: '#d97706',
              borderWidth: 2,
              borderDash: [5, 5],
              pointRadius: 0,
              fill: false
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: { beginAtZero: true, max: 100, ticks: { callback: v => v + '%' } }
          }
        }
      });
    }

  } catch (err) {
    console.error('Error rendering scores view:', err);
  }
}

// ========================================================
// 8. REMINDERS CONTROLLER
// ========================================================
function renderRemindersTable() {
  const tbody = document.getElementById('reminders-table-body');
  if (!tbody) return;

  if (state.reminders.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="8" class="text-center py-5">
          <i class="bi bi-shield-check text-success fs-1 d-block mb-2"></i>
          <h6 class="fw-bold text-success">All Households Meeting Municipal Standards!</h6>
          <p class="text-muted small mb-0">No households currently require segregation reminders.</p>
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = state.reminders.map(r => {
    return `
      <tr>
        <td>
          <strong>${escapeHtml(r.householdName)}</strong>
          <small class="text-muted d-block">${escapeHtml(r.address)}</small>
        </td>
        <td><span class="badge bg-light text-dark border">${escapeHtml(r.zoneName)}</span></td>
        <td><span class="badge bg-danger">${r.latestScore}%</span></td>
        <td><span class="badge bg-warning text-dark">${r.averageScore}%</span></td>
        <td><span class="badge-status-warning"><i class="bi bi-exclamation-octagon"></i> ${escapeHtml(r.status)}</span></td>
        <td><small class="text-muted"><i class="bi bi-calendar-event"></i> ${r.dateFlagged || 'Recent'}</small></td>
        <td>
          <div class="p-2 bg-light border rounded small text-danger fw-semibold" style="max-width: 320px;">
            <i class="bi bi-chat-left-dots-fill me-1"></i> "${escapeHtml(r.reminderMessage)}"
          </div>
        </td>
        <td class="text-end">
          <button class="btn btn-sm btn-eco-primary" title="Record Re-evaluation" onclick="prefillRecordPickup(${r.zoneId}, ${r.householdId})">
            <i class="bi bi-plus-lg"></i> Record
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

// ========================================================
// HELPER FUNCTIONS & DROPDOWNS
// ========================================================
function populateZoneDropdowns() {
  const zoneSelects = [
    document.getElementById('schedules-zone-filter'),
    document.getElementById('household-zone-filter'),
    document.getElementById('history-zone-filter'),
    document.getElementById('pickup-zone-select'),
    document.getElementById('schedule-zone-input'),
    document.getElementById('household-zone-input')
  ];

  zoneSelects.forEach(select => {
    if (!select) return;
    const currentVal = select.value;
    const isFilter = select.id.includes('filter');

    select.innerHTML = isFilter
      ? '<option value="">All Zones</option>'
      : '<option value="">-- Choose Zone --</option>';

    state.zones.forEach(z => {
      const opt = document.createElement('option');
      opt.value = z.id;
      opt.textContent = z.zoneName;
      select.appendChild(opt);
    });

    if (currentVal) select.value = currentVal;
  });
}

function populateScheduleZoneSelect() {
  const sel = document.getElementById('schedule-zone-input');
  if (!sel) return;
  sel.innerHTML = '<option value="">-- Choose Zone --</option>';
  state.zones.forEach(z => {
    const opt = document.createElement('option');
    opt.value = z.id;
    opt.textContent = z.zoneName;
    sel.appendChild(opt);
  });
}

function populateHouseholdZoneSelect() {
  const sel = document.getElementById('household-zone-input');
  if (!sel) return;
  sel.innerHTML = '<option value="">-- Choose Zone --</option>';
  state.zones.forEach(z => {
    const opt = document.createElement('option');
    opt.value = z.id;
    opt.textContent = z.zoneName;
    sel.appendChild(opt);
  });
}

function showToast(message, type = 'success') {
  const toastEl = document.getElementById('liveToast');
  const toastMsg = document.getElementById('toast-message');
  if (!toastEl || !toastMsg) return;

  toastMsg.textContent = message;
  toastEl.className = `toast align-items-center text-white border-0 bg-${type === 'success' ? 'success' : (type === 'danger' ? 'danger' : 'info')}`;

  const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
  toast.show();
}

function formatTime(timeStr) {
  if (!timeStr) return '';
  const parts = String(timeStr).split(':');
  if (parts.length < 2) return timeStr;
  let hour = parseInt(parts[0], 10);
  const min = parts[1];
  const ampm = hour >= 12 ? 'PM' : 'AM';
  hour = hour % 12 || 12;
  return `${hour}:${min} ${ampm}`;
}

function formatTimeForInput(timeStr) {
  if (!timeStr) return '';
  const parts = String(timeStr).split(':');
  return `${parts[0].padStart(2, '0')}:${parts[1].padStart(2, '0')}`;
}

function calculateWindowDuration(startStr, endStr) {
  const start = parseTimeToMinutes(startStr);
  const end = parseTimeToMinutes(endStr);
  let diff = end - start;
  if (diff < 0) diff += 24 * 60;
  const hours = Math.floor(diff / 60);
  const mins = diff % 60;
  return `${hours}h ${mins > 0 ? mins + 'm' : ''}`.trim();
}

function parseTimeToMinutes(timeStr) {
  if (!timeStr) return 0;
  const parts = String(timeStr).split(':');
  return parseInt(parts[0], 10) * 60 + parseInt(parts[1], 10);
}

function escapeHtml(text) {
  if (text === null || text === undefined) return '';
  return String(text)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
