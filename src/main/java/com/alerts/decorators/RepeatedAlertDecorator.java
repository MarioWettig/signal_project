package com.alerts.decorators;

import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;

import java.util.HashMap;
import java.util.Map;


public class RepeatedAlertDecorator extends AlertDecorator {
    private static Map<String, Integer> alertCountMap = new HashMap<>();
    private int requiredConsecutiveChecks = 3;

    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void sendAlert(AlertOutputStrategy outputStrategy) {
        String patientId = getPatientId();
        alertCountMap.put(patientId, alertCountMap.getOrDefault(patientId, 0) + 1);

        if (alertCountMap.get(patientId) >= requiredConsecutiveChecks) {
            decoratedAlert.sendAlert(outputStrategy);
            alertCountMap.put(patientId, 0);
        }
    }
}
