package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorProfileActivity extends AppCompatActivity {

    private TextView tvDoctorName, tvDoctorEmail, tvDoctorPhone, tvSpecialization,
            tvLicenseNumber, tvDoctorId, tvVerificationStatus;
    private Button btnLogout;
    private LinearLayout btnMyPatients, btnAppointments, btnManageRecords, btnProfile;
    private LinearLayout btnEditProfile, btnChangePassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(DoctorProfileActivity.this, MainActivity.class));
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadDoctorProfile();
    }

    private void initViews() {
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorEmail = findViewById(R.id.tvDoctorEmail);
        tvDoctorPhone = findViewById(R.id.tvDoctorPhone);
        tvSpecialization = findViewById(R.id.tvSpecialization);
        tvLicenseNumber = findViewById(R.id.tvLicenseNumber);
        tvDoctorId = findViewById(R.id.tvDoctorId);
        tvVerificationStatus = findViewById(R.id.tvVerificationStatus);
        btnLogout = findViewById(R.id.btnLogout);

        // Bottom navigation
        btnMyPatients = findViewById(R.id.btnMyPatients);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnManageRecords = findViewById(R.id.btnManageRecords);
        btnProfile = findViewById(R.id.btnProfile);

        // Highlight Profile tab
        ImageView ivProfile = findViewById(R.id.ivProfile);
        TextView tvProfile = findViewById(R.id.tvProfile);
        if (ivProfile != null) ivProfile.setColorFilter(getResources().getColor(R.color.blue_primary));
        if (tvProfile != null) tvProfile.setTextColor(getResources().getColor(R.color.blue_primary));

        // Profile options
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }

    private void setupClickListeners() {
        btnMyPatients.setOnClickListener(v -> {
            startActivity(new Intent(DoctorProfileActivity.this, DoctorDashboardActivity.class));
            finish();
        });

        btnAppointments.setOnClickListener(v -> {
            startActivity(new Intent(DoctorProfileActivity.this, DoctorAppointmentsActivity.class));
            finish();
        });

        btnManageRecords.setOnClickListener(v -> {
            startActivity(new Intent(DoctorProfileActivity.this, DoctorManageRecordsActivity.class));
            finish();
        });

        btnProfile.setOnClickListener(v -> {
            Toast.makeText(this, "You're already on Profile", Toast.LENGTH_SHORT).show();
        });

        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit profile feature coming soon", Toast.LENGTH_SHORT).show();
        });

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> logoutDoctor());
    }

    private void loadDoctorProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("doctors").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Populate profile data
                                tvDoctorName.setText(document.getString("name"));
                                tvDoctorEmail.setText(document.getString("email"));
                                tvDoctorPhone.setText(document.getString("phone"));
                                tvSpecialization.setText(document.getString("specialization"));
                                tvLicenseNumber.setText(document.getString("licenseNumber"));
                                tvDoctorId.setText(document.getString("doctorId"));

                                // Verification status
                                Boolean isVerified = document.getBoolean("isVerified");
                                if (isVerified != null && isVerified) {
                                    tvVerificationStatus.setText("✅ Verified");
                                    tvVerificationStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                } else {
                                    tvVerificationStatus.setText("⏳ Pending Verification");
                                    tvVerificationStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                                }
                            }
                        } else {
                            Toast.makeText(DoctorProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void logoutDoctor() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DoctorProfileActivity.this, MainActivity.class));
        finish();
    }
}