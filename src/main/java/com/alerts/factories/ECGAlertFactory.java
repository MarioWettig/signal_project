package com.alerts.factories;

import com.alerts.alerts.Alert;
import com.alerts.alerts.ECGAlert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
