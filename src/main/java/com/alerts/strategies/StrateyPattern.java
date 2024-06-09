package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.data_management.Patient;

public interface StrateyPattern {
    public Alert checkAlert(Patient patient);
}
