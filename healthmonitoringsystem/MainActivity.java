package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button btnPatient, btnDoctor;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserTypeAndRedirect(currentUser.getUid());
            return;
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnPatient = findViewById(R.id.btnPatient);
        btnDoctor = findViewById(R.id.btnDoctor);
    }

    private void setupClickListeners() {
        btnPatient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("userType", "patient");
            startActivity(intent);
        });

        btnDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("userType", "doctor");
            startActivity(intent);
        });
    }

    private void checkUserTypeAndRedirect(String userId) {
        // First check doctors collection
        FirebaseFirestore.getInstance().collection("doctors").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User is a doctor
                        startActivity(new Intent(MainActivity.this, DoctorDashboardActivity.class));
                        finish();
                    } else {
                        // Check users collection (patient)
                        FirebaseFirestore.getInstance().collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    if (userDoc.exists()) {
                                        // User is a patient
                                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                        finish();
                                    } else {
                                        // User not found, sign out
                                        mAuth.signOut();
                                    }
                                });
                    }
                });
    }
}
