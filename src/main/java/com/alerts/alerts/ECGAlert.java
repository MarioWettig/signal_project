package com.alerts.alerts;

import com.alerts.alerts.Alert;

public class ECGAlert extends Alert {
    public ECGAlert(String patientId, String condition, long timestamp, int priority) {
        super(patientId, condition, timestamp, priority);
    }
}
