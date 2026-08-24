package com.harshdi.healthmonitoringsystem;

import java.io.Serializable;

// Make sure it implements Serializable to pass between activities
public class Appointment implements Serializable {

    // These variable names MUST EXACTLY MATCH the field names in your Firestore documents
    private String id;
    private String patientName;
    private Long patientAge;
    private String patientPhone;
    private String appointmentDate;
    private String problem;
    private String status;
    private Long createdAt;
    private String patientId; // **FIX: Added this field**
    private String doctorId;  // **FIX: Added this field**

    // Default constructor is required for Firestore
    public Appointment() {}

    // --- GETTERS ---
    public String getId() { return id; }
    public String getPatientName() { return patientName; }
    public Long getPatientAge() { return patientAge; }
    public String getPatientPhone() { return patientPhone; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getProblem() { return problem; }
    public String getStatus() { return status; }
    public Long getCreatedAt() { return createdAt; }
    public String getPatientId() { return patientId; } // **FIX: Added getter**
    public String getDoctorId() { return doctorId; }   // **FIX: Added getter**

    // --- SETTERS ---
    public void setId(String id) { this.id = id; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientAge(Long patientAge) { this.patientAge = patientAge; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setProblem(String problem) { this.problem = problem; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    public void setPatientId(String patientId) { this.patientId = patientId; } // **FIX: Added setter**
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }   // **FIX: Added setter**
}
