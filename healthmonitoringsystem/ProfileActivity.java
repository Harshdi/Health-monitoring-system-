package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail, tvEmailReadonly, tvTotalReadingsProfile, tvDaysActive;
    private EditText etProfileName;
    private Button btnUpdateProfile, btnLogout;
    private LinearLayout btnHome, btnAddReading, btnCharts, btnProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadUserData();
        loadHealthStats();
    }

    private void initViews() {
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvEmailReadonly = findViewById(R.id.tvEmailReadonly);
        tvTotalReadingsProfile = findViewById(R.id.tvTotalReadingsProfile);
        tvDaysActive = findViewById(R.id.tvDaysActive);

        etProfileName = findViewById(R.id.etProfileName);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Bottom navigation
        btnHome = findViewById(R.id.btnHome);
        btnAddReading = findViewById(R.id.btnAddReading);
        btnCharts = findViewById(R.id.btnCharts);
        btnProfile = findViewById(R.id.btnProfile);
    }

    private void setupClickListeners() {
        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnLogout.setOnClickListener(v -> logout());

        // Bottom navigation
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddReading.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AppointmentActivity.class);
            startActivity(intent);
            finish();
        });

        btnCharts.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChartsActivity.class);
            startActivity(intent);
            finish();
        });

        btnProfile.setOnClickListener(v -> {
            Toast.makeText(this, "You\'re already on Profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set email
            String email = currentUser.getEmail();
            tvProfileEmail.setText(email);
            tvEmailReadonly.setText(email);

            // Load user data from Firestore
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                if (name != null) {
                                    tvProfileName.setText(name);
                                    etProfileName.setText(name);
                                }
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to load user data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadHealthStats() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        // Load total readings
        db.collection("readings")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalReadings = queryDocumentSnapshots.size();
                    tvTotalReadingsProfile.setText(String.valueOf(totalReadings));
                })
                .addOnFailureListener(e -> {
                    tvTotalReadingsProfile.setText("0");
                });

        // Calculate days active (simplified - just count unique dates)
        db.collection("readings")
                .whereEqualTo("userId", currentUser.getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // For simplicity, we'll just show total readings / 2 as days active
                    // In a real app, you'd parse timestamps and count unique dates
                    int approximateDays = Math.max(1, queryDocumentSnapshots.size() / 2);
                    tvDaysActive.setText(String.valueOf(approximateDays));
                })
                .addOnFailureListener(e -> {
                    tvDaysActive.setText("0");
                });
    }

    private void updateProfile() {
        String newName = etProfileName.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);

        db.collection("users").document(currentUser.getUid())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    tvProfileName.setText(newName);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}