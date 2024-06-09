package com.alerts.alerts;

import com.alerts.alerts.Alert;

public class BloodOxygenAlert extends Alert {

    public BloodOxygenAlert(String patientId, String condition, long timestamp, int priority) {
        super(patientId, condition, timestamp, priority);
    }

}
