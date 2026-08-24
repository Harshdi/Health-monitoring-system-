package com.harshdi.healthmonitoringsystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditAppointmentActivity extends AppCompatActivity {

    private EditText etPatientName, etPatientAge, etPatientPhone, etAppointmentDate, etProblem;
    private Button btnSaveChanges;
    private Appointment appointment;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        db = FirebaseFirestore.getInstance();

        initViews();

        // Get the serializable appointment object from the intent
        Serializable serializable = getIntent().getSerializableExtra("appointment");
        if (serializable instanceof Appointment) {
            appointment = (Appointment) serializable;
            populateAppointmentData();
        } else {
            Toast.makeText(this, "Error: Could not load appointment data.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etAppointmentDate.setOnClickListener(v -> showDatePickerDialog());
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void initViews() {
        etPatientName = findViewById(R.id.etPatientName);
        etPatientAge = findViewById(R.id.etPatientAge);
        etPatientPhone = findViewById(R.id.etPatientPhone);
        etAppointmentDate = findViewById(R.id.etAppointmentDate);
        etProblem = findViewById(R.id.etProblem);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
    }

    private void populateAppointmentData() {
        etPatientName.setText(appointment.getPatientName());
        if (appointment.getPatientAge() != null) {
            // Convert Long to String for setText
            etPatientAge.setText(String.valueOf(appointment.getPatientAge()));
        }
        etPatientPhone.setText(appointment.getPatientPhone());
        etAppointmentDate.setText(appointment.getAppointmentDate());
        etProblem.setText(appointment.getProblem());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
            etAppointmentDate.setText(selectedDate);
        }, year, month, day).show();
    }

    private void saveChanges() {
        String patientName = etPatientName.getText().toString().trim();
        String patientAgeStr = etPatientAge.getText().toString().trim();
        String patientPhone = etPatientPhone.getText().toString().trim();
        String appointmentDate = etAppointmentDate.getText().toString().trim();
        String problem = etProblem.getText().toString().trim();

        if (patientName.isEmpty() || patientAgeStr.isEmpty() || patientPhone.isEmpty() || appointmentDate.isEmpty() || problem.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the updated data in a Map to send to Firestore
        Map<String, Object> updatedAppointment = new HashMap<>();
        updatedAppointment.put("patientName", patientName);
        updatedAppointment.put("patientAge", Long.parseLong(patientAgeStr)); // Convert back to Long
        updatedAppointment.put("patientPhone", patientPhone);
        updatedAppointment.put("appointmentDate", appointmentDate);
        updatedAppointment.put("problem", problem);

        // Update the document in Firestore using its unique ID
        db.collection("appointments").document(appointment.getId())
                .update(updatedAppointment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Appointment updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the edit screen and go back to the appointments list
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
