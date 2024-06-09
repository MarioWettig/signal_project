package com.alerts.factories;

import com.alerts.alerts.Alert;
import com.alerts.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp, int priority) {
        return new BloodPressureAlert(patientId, condition, timestamp, priority);
    }
}
