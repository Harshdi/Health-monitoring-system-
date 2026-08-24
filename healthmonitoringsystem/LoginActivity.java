package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ImageView ivPasswordToggle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off);
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility);
        }
        isPasswordVisible = !isPasswordVisible;
        etPassword.setSelection(etPassword.getText().length());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    checkUserTypeAndRedirect(authResult.getUser().getUid());
                })
                .addOnFailureListener(e -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login");
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void checkUserTypeAndRedirect(String userId) {
        // 1. Check if user is a Patient (in 'users' collection)
        db.collection("users").document(userId).get().addOnSuccessListener(userDoc -> {
            if (userDoc.exists()) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } else {
                // 2. If not patient, check if user is a Doctor (in 'doctors' collection)
                db.collection("doctors").document(userId).get().addOnSuccessListener(docDoc -> {
                    if (docDoc.exists()) {
                        startActivity(new Intent(this, DoctorDashboardActivity.class));
                        finish();
                    } else {
                        // Profile missing in database
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                        mAuth.signOut();
                        Toast.makeText(this, "Profile not found. Please register again.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> handleDbError(e.getMessage()));
            }
        }).addOnFailureListener(e -> handleDbError(e.getMessage()));
    }

    private void handleDbError(String error) {
        btnLogin.setEnabled(true);
        btnLogin.setText("Login");
        Toast.makeText(this, "Database error: " + error, Toast.LENGTH_LONG).show();
    }
}