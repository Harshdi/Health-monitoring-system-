package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private TextView tvWelcomeUser, tvTotalReadings, tvLastReading, tvHealthStatus;
    private TextView tvAvgHeartRate, tvLastBP;
    private RecyclerView recyclerViewRecentReadings;
    private LinearLayout btnHome, btnAddAppointment, btnCharts, btnProfile, noDataLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private HealthReadingsAdapter readingsAdapter;
    private List<HealthReading> readingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        readingsList = new ArrayList<>();

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadUserData();
        loadDashboardData();
    }

    private void initViews() {
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvTotalReadings = findViewById(R.id.tvTotalReadings);
        tvLastReading = findViewById(R.id.tvLastReading);
        tvHealthStatus = findViewById(R.id.tvHealthStatus);
        tvAvgHeartRate = findViewById(R.id.tvAvgHeartRate);
        tvLastBP = findViewById(R.id.tvLastBP);
        recyclerViewRecentReadings = findViewById(R.id.recyclerViewRecentReadings);
        noDataLayout = findViewById(R.id.noDataLayout);

        btnHome = findViewById(R.id.btnHome);
        btnAddAppointment = findViewById(R.id.btnAddReading);
        btnCharts = findViewById(R.id.btnCharts);
        btnProfile = findViewById(R.id.btnProfile);
    }

    private void setupRecyclerView() {
        readingsAdapter = new HealthReadingsAdapter(readingsList);
        recyclerViewRecentReadings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecentReadings.setAdapter(readingsAdapter);
    }

    private void setupClickListeners() {
        btnCharts.setOnClickListener(v -> startActivity(new Intent(this, ChartsActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        btnAddAppointment.setOnClickListener(v -> startActivity(new Intent(this, AppointmentActivity.class)));
    }

    private void loadDashboardData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        Log.d(TAG, "Loading data for user: " + currentUser.getUid());

        // Standardized query that requires the index (userId Asc, timestamp Desc)
        db.collection("health_readings")
                .whereEqualTo("userId", currentUser.getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    readingsList.clear();
                    int totalCount = snapshots.size();
                    tvTotalReadings.setText(String.valueOf(totalCount));

                    if (totalCount == 0) {
                        tvHealthStatus.setText("No data yet");
                        tvLastReading.setText("N/A");
                        noDataLayout.setVisibility(View.VISIBLE);
                        recyclerViewRecentReadings.setVisibility(View.GONE);
                        return;
                    }

                    noDataLayout.setVisibility(View.GONE);
                    recyclerViewRecentReadings.setVisibility(View.VISIBLE);
                    
                    long hrSum = 0;
                    int hrCount = 0;
                    HealthReading latest = null;

                    for (QueryDocumentSnapshot doc : snapshots) {
                        try {
                            HealthReading reading = doc.toObject(HealthReading.class);
                            reading.setId(doc.getId());
                            
                            if (reading.getHeartRate() != null) {
                                hrSum += reading.getHeartRate();
                                hrCount++;
                            }
                            
                            if (latest == null) {
                                latest = reading;
                                // Get the status from the document since it might not be in the POJO
                                String status = doc.getString("healthStatus");
                                tvHealthStatus.setText(status != null ? status : "Recorded");
                            }
                            
                            if (readingsList.size() < 5) {
                                readingsList.add(reading);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing reading: " + e.getMessage());
                        }
                    }

                    readingsAdapter.notifyDataSetChanged();

                    // Update Metrics using the latest entry
                    if (latest != null) {
                        if (latest.getTimestamp() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
                            tvLastReading.setText(sdf.format(latest.getTimestamp()));
                        }
                        if (latest.getBloodPressureSystolic() != null && latest.getBloodPressureDiastolic() != null) {
                            tvLastBP.setText(latest.getBloodPressureSystolic() + "/" + latest.getBloodPressureDiastolic() + " mmHg");
                        }
                    }

                    if (hrCount > 0) {
                        tvAvgHeartRate.setText((hrSum / hrCount) + " BPM");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Firestore error: " + e.getMessage());
                    // Special handling for index error to guide the user
                    if (e.getMessage() != null && e.getMessage().contains("index")) {
                        Toast.makeText(this, "Setting up database... please wait 2 mins", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Failed to load readings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(doc -> {
                if (doc.exists()) {
                    String name = doc.getString("name");
                    tvWelcomeUser.setText("Hello, " + (name != null ? name : "User") + "!");
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }
}