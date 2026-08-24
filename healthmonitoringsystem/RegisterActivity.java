package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etUsername, etEmail, etPhone, etPassword, etSpecialization, etLicense, etDateOfBirth;
    private Button btnRegister, btnPatientType, btnDoctorType;
    private TextView tvLogin;
    private ImageView ivPasswordToggle;
    private LinearLayout doctorFields, dobLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userType = "patient";
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupClickListeners();
        updateUserTypeSelection();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etSpecialization = findViewById(R.id.etSpecialization);
        etLicense = findViewById(R.id.etLicense);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        btnRegister = findViewById(R.id.btnRegister);
        btnPatientType = findViewById(R.id.btnPatientType);
        btnDoctorType = findViewById(R.id.btnDoctorType);
        tvLogin = findViewById(R.id.tvLogin);
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle);
        doctorFields = findViewById(R.id.doctorFields);
        dobLayout = findViewById(R.id.dobLayout);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> registerUser());
        btnPatientType.setOnClickListener(v -> { userType = "patient"; updateUserTypeSelection(); });
        btnDoctorType.setOnClickListener(v -> { userType = "doctor"; updateUserTypeSelection(); });
        tvLogin.setOnClickListener(v -> { startActivity(new Intent(this, LoginActivity.class)); finish(); });
        ivPasswordToggle.setOnClickListener(v -> togglePasswordVisibility());
        etDateOfBirth.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, day) -> etDateOfBirth.setText(day + "/" + (month + 1) + "/" + year),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateUserTypeSelection() {
        if ("doctor".equals(userType)) {
            btnDoctorType.setBackgroundResource(R.drawable.button_type_selected);
            btnDoctorType.setTextColor(Color.WHITE);
            btnPatientType.setBackgroundResource(R.drawable.button_type_unselected);
            btnPatientType.setTextColor(getResources().getColor(R.color.blue_primary));
            doctorFields.setVisibility(View.VISIBLE);
            dobLayout.setVisibility(View.GONE);
        } else {
            btnPatientType.setBackgroundResource(R.drawable.button_type_selected);
            btnPatientType.setTextColor(Color.WHITE);
            btnDoctorType.setBackgroundResource(R.drawable.button_type_unselected);
            btnDoctorType.setTextColor(getResources().getColor(R.color.blue_primary));
            doctorFields.setVisibility(View.GONE);
            dobLayout.setVisibility(View.VISIBLE);
        }
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

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dob = etDateOfBirth.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Registering...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    saveUserData(uid, name, email, dob, phone, username);
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Register");
                    Toast.makeText(this, "Auth Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveUserData(String uid, String name, String email, String dob, String phone, String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("username", username);
        user.put("userType", userType);
        user.put("createdAt", System.currentTimeMillis());

        if ("patient".equals(userType)) {
            user.put("dateOfBirth", dob);
        } else {
            user.put("specialization", etSpecialization.getText().toString().trim());
            user.put("licenseNumber", etLicense.getText().toString().trim());
        }

        String collection = "doctor".equals(userType) ? "doctors" : "users";

        db.collection(collection).document(uid).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Register");
                    Toast.makeText(this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}