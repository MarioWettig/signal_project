package com.alerts.decorators;

import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;

public class HighPriorityAlertDecorator extends AlertDecorator {


    public HighPriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void sendAlert(AlertOutputStrategy outputStrategy) {
        String message = "HIGH PRIORITY: " + getCondition() + " for patient " + getPatientId() + " at " + getTimestamp();
        outputStrategy.send(message);
    }
}
