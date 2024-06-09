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
    private static final long HIGH_PRIORITY_ALERT_INTERVAL = TimeUnit.SECONDS.toMillis(10);

    public AlertManager(AlertOutputStrategy outputStrategy) {
        this.outputStrategy = outputStrategy;
        this.executorService = Executors.newCachedThreadPool();
        this.lastHighPriorityAlertTime = new ConcurrentHashMap<>();
    }

    public void processAlert(Alert alert) {
        executorService.submit(() -> {
            System.out.println("Processing alert for patient: " + alert.getPatientId()); // Debug log
            if (alert.getPriority() == 1) {
                synchronized (lastHighPriorityAlertTime) {
                    Long lastAlertTime = lastHighPriorityAlertTime.get(alert.getPatientId());
                    long currentTime = alert.getTimestamp();
                    if (lastAlertTime == null || (currentTime - lastAlertTime) >= HIGH_PRIORITY_ALERT_INTERVAL) {
                        lastHighPriorityAlertTime.put(alert.getPatientId(), currentTime);
                        sendAlert(alert);
                    } else {
                        System.out.println("High priority alert suppressed for patient: " + alert.getPatientId()); // Debug log
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
            alert = new RepeatedAlertDecorator(alert);
        } else if (alert.getPriority() == 1) {
            alert = new HighPriorityAlertDecorator(alert);
        }
        return alert;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
