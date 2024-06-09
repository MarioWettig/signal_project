package com.health_processor;

import com.alerts.AlertManager;
import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;
import com.alerts.strategies.*;
import com.data_management.DataStorage;
import com.data_management.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthDataProcessor implements DataListener {
    private List<StrategyPattern> realTimeStrategies;
    private List<StrategyPattern> periodicStrategies;
    private AlertManager alertManager;
    private ScheduledExecutorService scheduler;


    public HealthDataProcessor(AlertOutputStrategy outputStrategy, List<StrategyPattern> realTimeStrategies, List<StrategyPattern> periodicStrategies) {
        this.alertManager = new AlertManager(outputStrategy);

        if (realTimeStrategies == null) initializeRealTimeStrategies();
        else this.realTimeStrategies = new ArrayList<>();
        if (periodicStrategies == null) initializePeriodicStrategies();
        else this.periodicStrategies = new ArrayList<>();

        this.scheduler = Executors.newScheduledThreadPool(1);
        startPeriodicChecks();
    }


    private void initializeRealTimeStrategies() {
        realTimeStrategies = new ArrayList<>();
        realTimeStrategies.add(new HeartRateStrategy());
        realTimeStrategies.add(new HypotensiveHypoxemiaStrategy());
    }

    private void initializePeriodicStrategies() {
        periodicStrategies = new ArrayList<>();
        periodicStrategies.add(new BloodPressureStrategy());
        periodicStrategies.add(new OxygenSaturationStrategy());
    }

    public void onDataAdded(Patient patient) {
        for (StrategyPattern strategy : realTimeStrategies) {
            Alert alert = strategy.checkAlert(patient);
            if (alert != null) {
                alertManager.processAlert(alert);
            }
        }
    }

    private void startPeriodicChecks() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Patient patient : DataStorage.getDataStorageInstance().getAllPatients()) {
                for (StrategyPattern strategy : periodicStrategies) {
                    Alert alert = strategy.checkAlert(patient);
                    if (alert != null) {
                        alertManager.processAlert(alert);
                    }
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }


}
