package com.harshdi.healthmonitoringsystem;

public class Reading {
    private String id;
    private String type;
    private String value;
    private String timestamp;
    private String notes;
    private Long heartRate;
    private Long systolic;
    private Long diastolic;

    public Reading() {
        // Default constructor required for Firestore
    }

    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getValue() { return value; }
    public String getTimestamp() { return timestamp; }
    public String getNotes() { return notes; }
    public Long getHeartRate() { return heartRate; }
    public Long getSystolic() { return systolic; }
    public Long getDiastolic() { return diastolic; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setValue(String value) { this.value = value; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setHeartRate(Long heartRate) { this.heartRate = heartRate; }
    public void setSystolic(Long systolic) { this.systolic = systolic; }
    public void setDiastolic(Long diastolic) { this.diastolic = diastolic; }
}