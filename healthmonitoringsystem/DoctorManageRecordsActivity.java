package com.harshdi.healthmonitoringsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorManageRecordsActivity extends AppCompatActivity implements HealthReadingsAdapter.OnReadingActionListener {

    private static final String TAG = "DoctorManageRecords";
    private TextView tvPatientNameHeader;
    private EditText etHeartRate, etSystolic, etDiastolic, etTemperature, etOxygen;
    private Spinner spinnerHealthStatus;
    private Button btnAddRecord;
    private LineChart chartHeartRate, chartBloodPressure, chartTemperature;
    private RecyclerView recyclerViewReadings;

    private FirebaseFirestore db;
    private HealthReadingsAdapter readingsAdapter;
    private List<HealthReading> readingsList;
    private String patientId, patientName, editingRecordId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_manage_records);

        db = FirebaseFirestore.getInstance();
        readingsList = new ArrayList<>();
        patientId = getIntent().getStringExtra("patientId");
        patientName = getIntent().getStringExtra("patientName");

        initViews();
        setupStatusSpinner();
        setupRecyclerView();
        setupChartSettings(chartHeartRate);
        setupChartSettings(chartBloodPressure);
        setupChartSettings(chartTemperature);
        setupClickListeners();
        setupBottomNavigation();

        if (patientId != null) {
            loadHealthData();
        }
    }

    private void initViews() {
        tvPatientNameHeader = findViewById(R.id.tvPatientNameHeader);
        if (patientName != null) tvPatientNameHeader.setText("Records for " + patientName);
        
        etHeartRate = findViewById(R.id.etHeartRate);
        etSystolic = findViewById(R.id.etSystolic);
        etDiastolic = findViewById(R.id.etDiastolic);
        etTemperature = findViewById(R.id.etTemperature);
        etOxygen = findViewById(R.id.etOxygen);
        spinnerHealthStatus = findViewById(R.id.spinnerHealthStatus);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        recyclerViewReadings = findViewById(R.id.recyclerViewReadings);
        
        chartHeartRate = findViewById(R.id.chartHeartRate);
        chartBloodPressure = findViewById(R.id.chartBloodPressure);
        chartTemperature = findViewById(R.id.chartTemperature);
    }

    private void setupStatusSpinner() {
        String[] statuses = {"Normal", "Elevated", "High BP", "Critical", "Stable"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHealthStatus.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        readingsAdapter = new HealthReadingsAdapter(readingsList);
        readingsAdapter.setOnReadingActionListener(this);
        recyclerViewReadings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReadings.setAdapter(readingsAdapter);
    }

    private void setupChartSettings(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setNoDataText("Loading trends...");
        chart.setBackgroundColor(Color.WHITE);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnAddRecord.setOnClickListener(v -> handleSaveOrUpdate());
    }

    private void handleSaveOrUpdate() {
        String hrStr = etHeartRate.getText().toString().trim();
        String sysStr = etSystolic.getText().toString().trim();
        String diaStr = etDiastolic.getText().toString().trim();
        String tempStr = etTemperature.getText().toString().trim();
        String oxyStr = etOxygen.getText().toString().trim();

        if (hrStr.isEmpty() && sysStr.isEmpty() && tempStr.isEmpty()) {
            Toast.makeText(this, "Please enter at least one metric", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", patientId);
        data.put("timestamp", new java.util.Date());
        data.put("healthStatus", spinnerHealthStatus.getSelectedItem().toString());
        
        try {
            if (!hrStr.isEmpty()) {
                long hr = Long.parseLong(hrStr);
                // Validation logic: Heart Rate around 110 check
                data.put("heartRate", hr);
            }
            if (!sysStr.isEmpty()) {
                long sys = Long.parseLong(sysStr);
                // Validation logic: BP not below 80 and around 200
                if (sys < 80 || sys > 250) {
                    etSystolic.setError("Range: 80 - 250");
                    return;
                }
                data.put("bloodPressureSystolic", sys);
            }
            if (!diaStr.isEmpty()) data.put("bloodPressureDiastolic", Long.parseLong(diaStr));
            if (!tempStr.isEmpty()) data.put("temperature", Double.parseDouble(tempStr));
            if (!oxyStr.isEmpty()) data.put("oxygenLevel", Long.parseLong(oxyStr));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingRecordId == null) {
            db.collection("health_readings").add(data).addOnSuccessListener(ref -> {
                Toast.makeText(this, "Record added!", Toast.LENGTH_SHORT).show();
                clearAndRefresh();
            });
        } else {
            db.collection("health_readings").document(editingRecordId).update(data).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Record updated!", Toast.LENGTH_SHORT).show();
                clearAndRefresh();
            });
        }
    }

    private void loadHealthData() {
        // Use DESCENDING to reuse existing index from patient side
        db.collection("health_readings")
                .whereEqualTo("userId", patientId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    List<HealthReading> data = snapshots.toObjects(HealthReading.class);
                    for (int i = 0; i < snapshots.size(); i++) {
                        data.get(i).setId(snapshots.getDocuments().get(i).getId());
                    }
                    
                    readingsList.clear();
                    readingsList.addAll(data);
                    readingsAdapter.notifyDataSetChanged();
                    
                    // Prepare data for charts (Chronological order: Oldest to Newest)
                    List<HealthReading> chartData = new ArrayList<>(data);
                    Collections.sort(chartData, (a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
                    
                    List<Entry> hrEntries = new ArrayList<>();
                    List<Entry> sysEntries = new ArrayList<>();
                    List<Entry> diaEntries = new ArrayList<>();
                    List<Entry> tempEntries = new ArrayList<>();

                    for (int i = 0; i < chartData.size(); i++) {
                        HealthReading r = chartData.get(i);
                        if (r.getHeartRate() != null) hrEntries.add(new Entry(i, r.getHeartRate().floatValue()));
                        if (r.getBloodPressureSystolic() != null) sysEntries.add(new Entry(i, r.getBloodPressureSystolic().floatValue()));
                        if (r.getBloodPressureDiastolic() != null) diaEntries.add(new Entry(i, r.getBloodPressureDiastolic().floatValue()));
                        if (r.getTemperature() != null) tempEntries.add(new Entry(i, r.getTemperature().floatValue()));
                    }
                    
                    updateCharts(hrEntries, sysEntries, diaEntries, tempEntries);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading data", e);
                    Toast.makeText(this, "Database error. Check index.", Toast.LENGTH_LONG).show();
                });
    }

    private void updateCharts(List<Entry> hr, List<Entry> sys, List<Entry> dia, List<Entry> temp) {
        setupLineChart(chartHeartRate, hr, "Heart Rate", Color.BLUE);
        setupLineChart(chartBloodPressure, sys, "Systolic", Color.RED, dia, "Diastolic", Color.MAGENTA);
        setupLineChart(chartTemperature, temp, "Temperature", Color.rgb(255, 165, 0));
    }

    private void setupLineChart(LineChart chart, List<Entry> entries, String label, int color) {
        if (entries.isEmpty()) { chart.clear(); chart.setNoDataText("No data available"); return; }
        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        chart.setData(new LineData(set));
        chart.invalidate();
    }

    private void setupLineChart(LineChart chart, List<Entry> entries1, String label1, int color1, List<Entry> entries2, String label2, int color2) {
        if (entries1.isEmpty() && entries2.isEmpty()) { chart.clear(); chart.setNoDataText("No data available"); return; }
        LineDataSet set1 = new LineDataSet(entries1, label1);
        set1.setColor(color1);
        set1.setCircleColor(color1);
        
        LineDataSet set2 = new LineDataSet(entries2, label2);
        set2.setColor(color2);
        set2.setCircleColor(color2);
        
        chart.setData(new LineData(set1, set2));
        chart.invalidate();
    }

    @Override
    public void onEditClick(HealthReading reading) {
        editingRecordId = reading.getId();
        etHeartRate.setText(reading.getHeartRate() != null ? String.valueOf(reading.getHeartRate()) : "");
        etSystolic.setText(reading.getBloodPressureSystolic() != null ? String.valueOf(reading.getBloodPressureSystolic()) : "");
        etDiastolic.setText(reading.getBloodPressureDiastolic() != null ? String.valueOf(reading.getBloodPressureDiastolic()) : "");
        etTemperature.setText(reading.getTemperature() != null ? String.valueOf(reading.getTemperature()) : "");
        etOxygen.setText(reading.getOxygenLevel() != null ? String.valueOf(reading.getOxygenLevel()) : "");
        
        btnAddRecord.setText("Update Record");
        btnAddRecord.setBackgroundResource(R.drawable.button_green);
        Toast.makeText(this, "Editing selected record", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(HealthReading reading) {
        new AlertDialog.Builder(this).setTitle("Delete Record").setMessage("Delete this reading permanently?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("health_readings").document(reading.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                                loadHealthData();
                            });
                }).setNegativeButton("Cancel", null).show();
    }

    private void clearAndRefresh() {
        editingRecordId = null;
        btnAddRecord.setText("Add Record");
        btnAddRecord.setBackgroundResource(R.drawable.button_blue);
        etHeartRate.setText(""); etSystolic.setText(""); etDiastolic.setText("");
        etTemperature.setText(""); etOxygen.setText("");
        loadHealthData();
    }

    private void setupBottomNavigation() {
        findViewById(R.id.btnMyPatients).setOnClickListener(v -> { startActivity(new Intent(this, DoctorDashboardActivity.class)); finish(); });
        findViewById(R.id.btnAppointments).setOnClickListener(v -> { startActivity(new Intent(this, DoctorAppointmentsActivity.class)); finish(); });
        findViewById(R.id.btnProfile).setOnClickListener(v -> { startActivity(new Intent(this, DoctorProfileActivity.class)); finish(); });
    }
}