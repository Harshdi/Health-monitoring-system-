package com.harshdi.healthmonitoringsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DoctorDashboardActivity extends AppCompatActivity implements PatientsAdapter.OnPatientActionListener {

    private TextView tvTotalPatients, tvDoctorName;
    private RecyclerView recyclerViewPatients;
    private LinearLayout noDataLayout;
    private LinearLayout btnAppointments, btnManageRecords, btnProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PatientsAdapter patientsAdapter;
    private List<Patient> patientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        patientsList = new ArrayList<>();

        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctorData();
        loadAllPatients();
    }

    private void initViews() {
        tvTotalPatients = findViewById(R.id.tvTotalPatients);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        recyclerViewPatients = findViewById(R.id.recyclerViewPatients);
        noDataLayout = findViewById(R.id.noDataLayout);

        btnAppointments = findViewById(R.id.btnAppointments);
        btnManageRecords = findViewById(R.id.btnManageRecords);
        btnProfile = findViewById(R.id.btnProfile);
    }

    private void setupRecyclerView() {
        patientsAdapter = new PatientsAdapter(patientsList, this);
        recyclerViewPatients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPatients.setAdapter(patientsAdapter);
    }

    private void setupClickListeners() {
        btnAppointments.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorAppointmentsActivity.class));
            finish();
        });

        btnManageRecords.setOnClickListener(v -> {
            Toast.makeText(this, "Please select a patient from the list below", Toast.LENGTH_SHORT).show();
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorProfileActivity.class));
            finish();
        });
    }

    @Override
    public void onPatientClick(Patient patient) {
        // Redirect to Manage Records with patient data
        Intent intent = new Intent(this, DoctorManageRecordsActivity.class);
        intent.putExtra("patientId", patient.getUserId());
        intent.putExtra("patientName", patient.getName());
        startActivity(intent);
    }

    private void loadDoctorData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("doctors").document(currentUser.getUid()).get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            tvDoctorName.setText("Dr. " + document.getString("name"));
                        }
                    });
        }
    }

    private void loadAllPatients() {
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            patientsList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Patient patient = document.toObject(Patient.class);
                patient.setUserId(document.getId());
                patientsList.add(patient);
            }
            patientsAdapter.notifyDataSetChanged();
            tvTotalPatients.setText(String.valueOf(patientsList.size()));
            if (noDataLayout != null) noDataLayout.setVisibility(patientsList.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override public void onEditClick(Patient patient) {}
    @Override public void onDeleteClick(Patient patient) {}
}