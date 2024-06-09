package com.health_processor;

import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;
import com.alerts.strategies.*;
import com.data_management.Patient;

import java.util.List;

public class HealthDataProcessor implements DataListener {
    private List<StrategyPattern> strategies;
    private AlertOutputStrategy outputStrategy;

    public HealthDataProcessor(AlertOutputStrategy outputStrategy, List<StrategyPattern> strategies) {
        this.outputStrategy = outputStrategy;
        if (strategies == null) {
            initializeStrategies();
        } else {
            this.strategies = strategies;
        }
    }

    private void initializeStrategies() {
        strategies.add(new BloodPressureStrategy());
        strategies.add(new HeartRateStrategy());
        strategies.add(new HypotensiveHypoxemiaStrategy());
        strategies.add(new OxygenSaturationStrategy());
    }

    @Override
    public void onDataAdded(Patient patient) {
        for (StrategyPattern strategy : strategies) {
            Alert alert = strategy.checkAlert(patient);
            if (alert != null) {
                alert.sendAlert(outputStrategy);
            }
        }
    }
}
