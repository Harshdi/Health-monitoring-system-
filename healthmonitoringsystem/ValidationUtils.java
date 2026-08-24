package com.harshdi.healthmonitoringsystem;

public class ValidationUtils {

    /**
     * Validates if the heart rate is within a reasonable range.
     * @param heartRate The heart rate in beats per minute (bpm).
     * @return true if valid, false otherwise.
     */
    public static boolean isValidHeartRate(int heartRate) {
        return heartRate >= 30 && heartRate <= 220;
    }

    /**
     * Gets the category for a given heart rate.
     * @param heartRate The heart rate in beats per minute (bpm).
     * @return A string describing the category (e.g., "Normal", "Low", "High").
     */
    public static String getHeartRateCategory(int heartRate) {
        if (heartRate < 60) {
            return "Low";
        } else if (heartRate <= 100) {
            return "Normal";
        } else {
            return "High";
        }
    }

    /**
     * Validates if the blood pressure values are within a reasonable range.
     * @param systolic The systolic pressure.
     * @param diastolic The diastolic pressure.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidBloodPressure(int systolic, int diastolic) {
        return systolic > 0 && diastolic > 0 && systolic > diastolic;
    }

    /**
     * Gets the category for a given blood pressure reading.
     * @param systolic The systolic pressure.
     * @param diastolic The diastolic pressure.
     * @return A string describing the blood pressure category.
     */
    public static String getBloodPressureCategory(int systolic, int diastolic) {
        if (systolic < 120 && diastolic < 80) {
            return "Normal";
        } else if (systolic >= 120 && systolic <= 129 && diastolic < 80) {
            return "Elevated";
        } else if ((systolic >= 130 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
            return "High Blood Pressure (Stage 1)";
        } else if (systolic >= 140 || diastolic >= 90) {
            return "High Blood Pressure (Stage 2)";
        } else if (systolic > 180 || diastolic > 120) {
            return "Hypertensive Crisis";
        } else {
            return "Unable to determine category";
        }
    }
}
