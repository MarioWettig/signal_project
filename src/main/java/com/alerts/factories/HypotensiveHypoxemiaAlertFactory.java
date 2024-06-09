package com.alerts.factories;

import com.alerts.alerts.Alert;
import com.alerts.alerts.ECGAlert;
import com.alerts.alerts.HypotensiveHypoxemiaAlert;

public class HypotensiveHypoxemiaAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new HypotensiveHypoxemiaAlert(patientId, condition, timestamp);
    }
}
