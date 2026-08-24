package com.harshdi.healthmonitoringsystem;

import java.util.Date;

public class HealthReading {
    private String id;
    private String userId;
    private Date timestamp;
    private Long heartRate;
    private Long bloodPressureSystolic;
    private Long bloodPressureDiastolic;
    private Double temperature;
    private Long oxygenLevel;
    private String healthStatus; // Added this field

    public HealthReading() {}

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public Date getTimestamp() { return timestamp; }
    public Long getHeartRate() { return heartRate; }
    public Long getBloodPressureSystolic() { return bloodPressureSystolic; }
    public Long getBloodPressureDiastolic() { return bloodPressureDiastolic; }
    public Double getTemperature() { return temperature; }
    public Long getOxygenLevel() { return oxygenLevel; }
    public String getHealthStatus() { return healthStatus; } // Added

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public void setHeartRate(Long heartRate) { this.heartRate = heartRate; }
    public void setBloodPressureSystolic(Long bloodPressureSystolic) { this.bloodPressureSystolic = bloodPressureSystolic; }
    public void setBloodPressureDiastolic(Long bloodPressureDiastolic) { this.bloodPressureDiastolic = bloodPressureDiastolic; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public void setOxygenLevel(Long oxygenLevel) { this.oxygenLevel = oxygenLevel; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; } // Added
}