package com.alerts.health_processor;

import com.alerts.AlertManager;
import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;
import com.alerts.strategies.*;
import com.data_management.Patient;

import java.util.ArrayList;
import java.util.List;


public class HealthDataProcessor implements DataListener {
    private List<StrategyPattern> realTimeStrategies;
    private AlertManager alertManager;


    public HealthDataProcessor(AlertOutputStrategy outputStrategy, List<StrategyPattern> realTimeStrategies) {
        this.alertManager = new AlertManager(outputStrategy);

        if (realTimeStrategies == null) initializeRealTimeStrategies();
        else this.realTimeStrategies = realTimeStrategies;

    }

    private void initializeRealTimeStrategies() {
        realTimeStrategies = new ArrayList<>();
        realTimeStrategies.add(new HeartRateStrategy());
        realTimeStrategies.add(new HypotensiveHypoxemiaStrategy());
        realTimeStrategies.add(new BloodPressureStrategy());
        realTimeStrategies.add(new OxygenSaturationStrategy());
    }


    public void onDataAdded(Patient patient) {
        for (StrategyPattern strategy : realTimeStrategies) {
            Alert alert = strategy.checkAlert(patient);
            if (alert != null) {
                alertManager.processAlert(alert);
            }
        }
    }

}
