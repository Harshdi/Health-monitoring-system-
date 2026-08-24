package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientDetailsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvPatientName, tvPatientAge, tvPatientGender, tvPatientPhone, tvPatientEmail;
    private TextView tvLastReading, tvHeartRate, tvBloodPressure, tvTemperature, tvOxygenLevel;
    private RecyclerView recyclerViewReadings;
    private LinearLayout noReadingsLayout;

    private FirebaseFirestore db;
    private String patientId, patientName;
    private HealthReadingsAdapter readingsAdapter;
    private List<HealthReading> readingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        db = FirebaseFirestore.getInstance();
        readingsList = new ArrayList<>();

        // Get patient data from intent
        patientId = getIntent().getStringExtra("patientId");
        patientName = getIntent().getStringExtra("patientName");

        if (patientId == null) {
            Toast.makeText(this, "Patient data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        setupRecyclerView();
        loadPatientDetails();
        loadHealthReadings();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientAge = findViewById(R.id.tvPatientAge);
        tvPatientGender = findViewById(R.id.tvPatientGender);
        tvPatientPhone = findViewById(R.id.tvPatientPhone);
        tvPatientEmail = findViewById(R.id.tvPatientEmail);
        tvLastReading = findViewById(R.id.tvLastReading);
        tvHeartRate = findViewById(R.id.tvHeartRate);
        tvBloodPressure = findViewById(R.id.tvBloodPressure);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvOxygenLevel = findViewById(R.id.tvOxygenLevel);
        recyclerViewReadings = findViewById(R.id.recyclerViewReadings);
        noReadingsLayout = findViewById(R.id.noReadingsLayout);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        readingsAdapter = new HealthReadingsAdapter(readingsList);
        recyclerViewReadings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReadings.setAdapter(readingsAdapter);
    }

    private void loadPatientDetails() {
        db.collection("users").document(patientId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        tvPatientName.setText(documentSnapshot.getString("name"));
                        tvPatientEmail.setText(documentSnapshot.getString("email"));
                        tvPatientPhone.setText(documentSnapshot.getString("phone"));

                        Long age = documentSnapshot.getLong("age");
                        if (age != null) {
                            tvPatientAge.setText(age + " years");
                        }

                        String gender = documentSnapshot.getString("gender");
                        if (gender != null) {
                            tvPatientGender.setText(gender);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load patient details", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadHealthReadings() {
        db.collection("health_readings")
                .whereEqualTo("userId", patientId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    readingsList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        noReadingsLayout.setVisibility(View.VISIBLE);
                        recyclerViewReadings.setVisibility(View.GONE);
                        tvLastReading.setText("No readings available");
                        return;
                    }

                    boolean isFirstReading = true;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        HealthReading reading = new HealthReading();
                        reading.setId(document.getId());
                        reading.setHeartRate(document.getLong("heartRate"));
                        reading.setBloodPressureSystolic(document.getLong("bloodPressureSystolic"));
                        reading.setBloodPressureDiastolic(document.getLong("bloodPressureDiastolic"));
                        reading.setTemperature(document.getDouble("temperature"));
                        reading.setOxygenLevel(document.getLong("oxygenLevel"));
                        reading.setTimestamp(document.getDate("timestamp"));

                        readingsList.add(reading);

                        // Show latest reading in summary
                        if (isFirstReading) {
                            updateLatestReadingSummary(reading);
                            isFirstReading = false;
                        }
                    }

                    readingsAdapter.notifyDataSetChanged();
                    noReadingsLayout.setVisibility(View.GONE);
                    recyclerViewReadings.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load health readings", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateLatestReadingSummary(HealthReading reading) {
        if (reading.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
            tvLastReading.setText("Last updated: " + sdf.format(reading.getTimestamp()));
        }

        if (reading.getHeartRate() != null) {
            tvHeartRate.setText(reading.getHeartRate() + " BPM");
        }

        if (reading.getBloodPressureSystolic() != null && reading.getBloodPressureDiastolic() != null) {
            tvBloodPressure.setText(reading.getBloodPressureSystolic() + "/" + reading.getBloodPressureDiastolic() + " mmHg");
        }

        if (reading.getTemperature() != null) {
            tvTemperature.setText(String.format("%.1f°F", reading.getTemperature()));
        }

        if (reading.getOxygenLevel() != null) {
            tvOxygenLevel.setText(reading.getOxygenLevel() + "%");
        }
    }
}
