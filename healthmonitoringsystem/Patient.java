package com.harshdi.healthmonitoringsystem;

public class Patient {
    private String userId;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String lastReading;
    private String healthStatus;

    public Patient() {
        // Default constructor required for Firestore
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getLastReading() { return lastReading; }
    public String getHealthStatus() { return healthStatus; }

    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setLastReading(String lastReading) { this.lastReading = lastReading; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
}
