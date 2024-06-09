package com.alerts.decorators;

import com.alerts.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    public PriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }
}
