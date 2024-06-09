package com.alerts.alerts;

import com.alerts.outputstrategy.AlertOutputStrategy;
import com.alerts.outputstrategy.GlobalOutput;

// Represents an alert
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;
    int priority;


    public Alert(String patientId, String condition, long timestamp, int priority) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.priority = priority;
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

    public int getPriority() {return priority;}

    public void sendAlert(AlertOutputStrategy outputStrategy) {
        String message = "ALERT: patient " + patientId + ": " + condition + " at " + timestamp;
        outputStrategy.send(message);
    }
}
