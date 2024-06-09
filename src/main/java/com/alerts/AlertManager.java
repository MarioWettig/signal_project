package com.alerts;

import com.alerts.alerts.Alert;
import com.alerts.decorators.HighPriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import com.alerts.factories.AlertFactory;
import com.alerts.outputstrategy.AlertOutputStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AlertManager {
    private AlertOutputStrategy outputStrategy;
    private ExecutorService executorService;
    private Map<String, Long> lastHighPriorityAlertTime;
    private static final long HIGH_PRIORITY_ALERT_INTERVAL = TimeUnit.SECONDS.toMillis(30); // Example interval of 30 seconds

    public AlertManager(AlertOutputStrategy outputStrategy) {
        this.outputStrategy = outputStrategy;
        this.executorService = Executors.newCachedThreadPool();
        this.lastHighPriorityAlertTime = new ConcurrentHashMap<>();
    }

    public void processAlert(Alert alert) {
        executorService.submit(() -> {
            if (alert.getPriority() == 1) {
                synchronized (lastHighPriorityAlertTime) {
                    Long lastAlertTime = lastHighPriorityAlertTime.get(alert.getPatientId());
                    long currentTime = alert.getTimestamp();
                    if (lastAlertTime == null || (currentTime - lastAlertTime) >= HIGH_PRIORITY_ALERT_INTERVAL) {
                        lastHighPriorityAlertTime.put(alert.getPatientId(), currentTime);
                        sendAlert(alert);
                    }
                }
            } else {
                sendAlert(alert);
            }
        });
    }

    private void sendAlert(Alert alert) {
        alert = applyDecorators(alert);
        alert.sendAlert(outputStrategy);
    }

    private Alert applyDecorators(Alert alert) {
        if (alert.getPriority() == -1) {
            alert = new RepeatedAlertDecorator(alert); // Example: 3 consecutive checks for low priority
        } else if (alert.getPriority() == 1) {
            alert = new HighPriorityAlertDecorator(alert); // Example: Repeat 3 times for high priority
        }
        return alert;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
