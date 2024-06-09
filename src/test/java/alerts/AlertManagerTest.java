package alerts;

import com.alerts.AlertManager;
import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertManagerTest {
    private TestOutputStrategy outputStrategy;
    private AlertManager alertManager;

    @BeforeEach
    void setUp() {
        outputStrategy = new TestOutputStrategy();
        alertManager = new AlertManager(outputStrategy);
    }

    @Test
    void testProcessHighPriorityAlert() {
        Alert highPriorityAlert = new Alert("patient1", "High Priority Condition", System.currentTimeMillis(), 1);

        alertManager.processAlert(highPriorityAlert);

        assertEquals(1, outputStrategy.getAlerts().size());
        assertEquals(highPriorityAlert.getCondition(), outputStrategy.getAlerts().get(0).getCondition());
    }

    @Test
    void testSuppressDuplicateHighPriorityAlert() throws InterruptedException {
        String patientId = "patient1";
        long timestamp = System.currentTimeMillis();
        Alert highPriorityAlert1 = new Alert(patientId, "High Priority Condition", timestamp, 1);
        Alert highPriorityAlert2 = new Alert(patientId, "High Priority Condition", timestamp + 1000, 1);

        alertManager.processAlert(highPriorityAlert1);
        alertManager.processAlert(highPriorityAlert2);

        assertEquals(1, outputStrategy.getAlerts().size());

        TimeUnit.SECONDS.sleep(30); // Wait for the reset interval

        alertManager.processAlert(highPriorityAlert2);

        assertEquals(2, outputStrategy.getAlerts().size());
    }

    @Test
    void testProcessLowPriorityAlert() {
        Alert lowPriorityAlert = new Alert("patient1", "Low Priority Condition", System.currentTimeMillis(), -1);

        alertManager.processAlert(lowPriorityAlert);

        assertEquals(1, outputStrategy.getAlerts().size());
        assertEquals(lowPriorityAlert.getCondition(), outputStrategy.getAlerts().get(0).getCondition());
    }

    // Custom output strategy to capture alerts for testing
    static class TestOutputStrategy implements AlertOutputStrategy {
        private final List<Alert> alerts = new ArrayList<>();

        public void send(String message) {
            if (message.startsWith("ALERT:")) {
                parseBasicAlert(message);
            } else if (message.startsWith("HIGH PRIORITY:")) {
                parseHighPriorityAlert(message);
            }
        }

        private void parseBasicAlert(String message) {
            String[] parts = message.split(", ");
            String patientId = parts[0].split(" ")[2];
            String condition = parts[1];
            long timestamp = Long.parseLong(parts[2].split(" at ")[1]);
            alerts.add(new Alert(patientId, condition, timestamp, 0)); // Assuming default priority 0 for basic alerts
        }

        private void parseHighPriorityAlert(String message) {
            String[] parts = message.split(", ");
            String condition = parts[0].substring(15); // Skip "HIGH PRIORITY: "
            String patientId = parts[1].split(" ")[2];
            System.out.println(130);

            long timestamp = Long.parseLong(parts[2].split("at ")[1]);

            System.out.println(timestamp);
            alerts.add(new Alert(patientId, condition, timestamp, 1));
        }

        public List<Alert> getAlerts() {
            return alerts;
        }
    }
}
