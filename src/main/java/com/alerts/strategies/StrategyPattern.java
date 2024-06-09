package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.data_management.Patient;

public interface StrategyPattern {

    public Alert checkAlert(Patient patient);
}
