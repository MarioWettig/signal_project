package com.alerts.alerts;

import com.alerts.alerts.Alert;

public class BloodPressureAlert extends Alert {
    public BloodPressureAlert(String patientId, String condition, long timestamp, int priority) {
        super(patientId, condition, timestamp, priority);
    }
}
