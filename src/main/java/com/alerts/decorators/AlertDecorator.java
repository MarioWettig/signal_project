package com.alerts.decorators;

import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;

public class AlertDecorator extends Alert {
    protected Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp(), decoratedAlert.getPriority());
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public void sendAlert(AlertOutputStrategy outputStrategy) {
        decoratedAlert.sendAlert(outputStrategy);
    }
}
