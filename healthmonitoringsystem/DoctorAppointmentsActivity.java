package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentsActivity extends AppCompatActivity implements AppointmentsAdapter.OnAppointmentActionListener {

    private static final String TAG = "DoctorAppointments";

    private TextView tvTotalAppointments;
    private RecyclerView recyclerViewAppointments;
    private LinearLayout noDataLayout;
    private ProgressBar loadingProgressBar;
    // Added btnManageRecords to the list
    private LinearLayout btnMyPatients, btnAppointments, btnManageRecords, btnProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AppointmentsAdapter appointmentsAdapter;
    private List<Appointment> appointmentsList;
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        appointmentsList = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        this.doctorId = currentUser.getUid();

        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }

    private void initViews() {
        tvTotalAppointments = findViewById(R.id.tvTotalAppointments);
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        noDataLayout = findViewById(R.id.noDataLayout);
        btnMyPatients = findViewById(R.id.btnMyPatients);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnManageRecords = findViewById(R.id.btnManageRecords); // Initialized Records button
        btnProfile = findViewById(R.id.btnProfile);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    private void setupRecyclerView() {
        appointmentsAdapter = new AppointmentsAdapter(appointmentsList, this);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.setAdapter(appointmentsAdapter);
    }

    private void setupClickListeners() {
        btnMyPatients.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorDashboardActivity.class));
            finish();
        });

        btnAppointments.setOnClickListener(v ->
                Toast.makeText(this, "You're already on Appointments", Toast.LENGTH_SHORT).show());

        // Added redirection to Records page
        btnManageRecords.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorManageRecordsActivity.class));
            finish();
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorProfileActivity.class));
            finish();
        });
    }

    private void loadAppointments() {
        if (loadingProgressBar != null) loadingProgressBar.setVisibility(View.VISIBLE);
        recyclerViewAppointments.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);

        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Could not verify doctor ID.", Toast.LENGTH_SHORT).show();
            updateUiForNoAppointments();
            return;
        }

        db.collection("appointments").whereEqualTo("doctorId", doctorId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (loadingProgressBar != null) loadingProgressBar.setVisibility(View.GONE);
                    appointmentsList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        updateUiForNoAppointments();
                    } else {
                        noDataLayout.setVisibility(View.GONE);
                        recyclerViewAppointments.setVisibility(View.VISIBLE);
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setId(document.getId());
                            appointmentsList.add(appointment);
                        }
                        appointmentsAdapter.notifyDataSetChanged();
                        tvTotalAppointments.setText(String.valueOf(appointmentsList.size()));
                    }
                })
                .addOnFailureListener(e -> {
                    if (loadingProgressBar != null) loadingProgressBar.setVisibility(View.GONE);
                    updateUiForNoAppointments();
                    Log.e(TAG, "Error loading appointments", e);
                    Toast.makeText(this, "Failed to load appointments: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void updateUiForNoAppointments() {
        if (noDataLayout != null) noDataLayout.setVisibility(View.VISIBLE);
        recyclerViewAppointments.setVisibility(View.GONE);
        tvTotalAppointments.setText("0");
    }

    @Override
    public void onAppointmentAction(Appointment appointment, String action) {
        switch (action) {
            case "Approved":
            case "Rejected":
            case "Completed":
            case "Pending":
                updateAppointmentStatus(appointment.getId(), action);
                break;
            case "Edit":
                openEditAppointmentActivity(appointment);
                break;
            case "Delete":
                showDeleteConfirmationDialog(appointment);
                break;
        }
    }

    private void updateAppointmentStatus(String appointmentId, String status) {
        if (appointmentId == null) return;
        db.collection("appointments").document(appointmentId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Appointment set to " + status, Toast.LENGTH_SHORT).show();
                    loadAppointments();
                });
    }

    private void openEditAppointmentActivity(Appointment appointment) {
        Intent intent = new Intent(this, EditAppointmentActivity.class);
        intent.putExtra("appointment", appointment);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(Appointment appointment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAppointment(appointment.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAppointment(String appointmentId) {
        if (appointmentId == null) return;
        db.collection("appointments").document(appointmentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                    loadAppointments();
                });
    }
}