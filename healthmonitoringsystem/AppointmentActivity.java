package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {

    private static final String TAG = "AppointmentActivity";

    // Views for booking
    private EditText etPatientName, etPatientAge, etPatientPhone, etAppointmentDate, etProblem;
    private Button btnSubmitAppointment;

    // Views for checking status
    private EditText etCheckPhone;
    private Button btnCheckStatus;
    private RecyclerView recyclerViewPatientAppointments;

    // Firebase & Adapters
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AppointmentsAdapter patientAppointmentsAdapter;
    private List<Appointment> patientAppointmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupClickListeners();
        setupPatientAppointmentsView();
    }

    private void initViews() {
        etPatientName = findViewById(R.id.etPatientName);
        etPatientAge = findViewById(R.id.etPatientAge);
        etPatientPhone = findViewById(R.id.etPatientPhone);
        etAppointmentDate = findViewById(R.id.etAppointmentDate);
        etProblem = findViewById(R.id.etProblem);
        btnSubmitAppointment = findViewById(R.id.btnSubmitAppointment);
        etCheckPhone = findViewById(R.id.etCheckPhone);
        btnCheckStatus = findViewById(R.id.btnCheckStatus);
        recyclerViewPatientAppointments = findViewById(R.id.recyclerViewAppointments);
    }

    private void setupClickListeners() {
        etAppointmentDate.setOnClickListener(v -> showDatePicker());
        btnSubmitAppointment.setOnClickListener(v -> submitAppointment());
        btnCheckStatus.setOnClickListener(v -> checkAppointmentStatus());

        findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        findViewById(R.id.btnAddReading).setOnClickListener(v -> Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnCharts).setOnClickListener(v -> {
            startActivity(new Intent(this, ChartsActivity.class));
            finish();
        });
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }

    private void setupPatientAppointmentsView() {
        patientAppointmentsList = new ArrayList<>();
        patientAppointmentsAdapter = new AppointmentsAdapter(patientAppointmentsList, null); // Null listener for patient view
        recyclerViewPatientAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPatientAppointments.setAdapter(patientAppointmentsAdapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            etAppointmentDate.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submitAppointment() {
        String name = etPatientName.getText().toString().trim();
        String ageStr = etPatientAge.getText().toString().trim();
        String phone = etPatientPhone.getText().toString().trim();
        String date = etAppointmentDate.getText().toString().trim();
        String problem = etProblem.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || date.isEmpty() || problem.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to book an appointment", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("doctors").limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                Toast.makeText(this, "No doctors are available at this time.", Toast.LENGTH_LONG).show();
                return;
            }
            String doctorId = queryDocumentSnapshots.getDocuments().get(0).getId();

            Map<String, Object> appointment = new HashMap<>();
            appointment.put("patientId", currentUser.getUid());
            appointment.put("patientName", name);
            appointment.put("patientAge", Long.parseLong(ageStr));
            appointment.put("patientPhone", phone);
            appointment.put("appointmentDate", date);
            appointment.put("problem", problem);
            appointment.put("status", "Pending");
            appointment.put("createdAt", System.currentTimeMillis());
            appointment.put("doctorId", doctorId);

            db.collection("appointments").add(appointment).addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Appointment request submitted!", Toast.LENGTH_LONG).show();
                etPatientName.setText("");
                etPatientAge.setText("");
                etPatientPhone.setText("");
                etAppointmentDate.setText("");
                etProblem.setText("");
            }).addOnFailureListener(e -> Log.e(TAG, "Failed to submit appointment", e));
        }).addOnFailureListener(e -> Log.e(TAG, "Could not find a doctor", e));
    }

    private void checkAppointmentStatus() {
        String phone = etCheckPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter your mobile number to check status", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Checking status for phone number: " + phone); // Add this log

        // This is the query that requires the Firestore index.
        db.collection("appointments").whereEqualTo("patientPhone", phone)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    patientAppointmentsList.clear();
                    Log.d(TAG, "Successfully found " + queryDocumentSnapshots.size() + " appointments for this phone number."); // Add this log
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No appointments found for this mobile number.", Toast.LENGTH_SHORT).show();
                        recyclerViewPatientAppointments.setVisibility(View.GONE);
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setId(document.getId());
                            patientAppointmentsList.add(appointment);
                        }
                        patientAppointmentsAdapter.notifyDataSetChanged();
                        recyclerViewPatientAppointments.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(e -> {
                    // This error will appear if the Firestore index is missing.
                    Log.e(TAG, "Firestore query failed on patient side: " + e.getMessage(), e);
                    Toast.makeText(this, "Error: Could not check status. Please check Logcat for details.", Toast.LENGTH_LONG).show();
                });
    }
}
