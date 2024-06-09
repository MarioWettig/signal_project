package com.alerts.factories;

import com.alerts.alerts.Alert;

public class AlertFactory {

    /**
     * creates an alert
     * @ return new Alert
     */
    public Alert createAlert(String patientId, String condition, long timestamp){
        return new Alert(patientId, condition, timestamp);
    }
}
