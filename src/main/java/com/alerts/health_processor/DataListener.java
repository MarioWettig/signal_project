package com.alerts.health_processor;

import com.data_management.Patient;

public interface DataListener {
    void onDataAdded(Patient patient);
}
