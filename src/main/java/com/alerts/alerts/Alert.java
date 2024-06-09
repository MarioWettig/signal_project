package com.alerts.alerts;

import com.alerts.outputstrategy.AlertOutputStrategy;
import com.alerts.outputstrategy.GlobalOutput;

// Represents an alert
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;


    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void sendAlert(AlertOutputStrategy outputStrategy) {
        String message = "Alert for patient " + patientId + ": " + condition + " at " + timestamp;
        outputStrategy.send(message);
    }
}
