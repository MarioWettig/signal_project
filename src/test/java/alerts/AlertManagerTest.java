package alerts;

import com.alerts.AlertManager;
import com.alerts.alerts.Alert;
import com.alerts.outputstrategy.AlertOutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AlertManagerTest {
    private TestOutputStrategy outputStrategy;
    private AlertManager alertManager;

    @BeforeEach
    void setUp() {
        outputStrategy = new TestOutputStrategy(1);
        alertManager = new AlertManager(outputStrategy);
    }

    @Test
    void testProcessHighPriorityAlert() throws InterruptedException {
        Alert highPriorityAlert = new Alert("patient1", "High Priority Condition", System.currentTimeMillis(), 1);

        alertManager.processAlert(highPriorityAlert);
        outputStrategy.awaitAlerts();

        assertEquals(1, outputStrategy.getAlerts().size());
        assertTrue(outputStrategy.getAlerts().get(0).contains("High Priority Condition"));
    }

    @Test
    void testSuppressDuplicateHighPriorityAlert() throws InterruptedException {
        String patientId = "patient1";
        long timestamp = System.currentTimeMillis();
        Alert highPriorityAlert1 = new Alert(patientId, "High Priority Condition", timestamp, 1);
        Alert highPriorityAlert2 = new Alert(patientId, "High Priority Condition", timestamp + 1000, 1);

        alertManager.processAlert(highPriorityAlert1);
        outputStrategy.awaitAlerts();

        outputStrategy.resetLatch(1);
        alertManager.processAlert(highPriorityAlert2);
        try {
            outputStrategy.awaitAlerts();
            fail("Expected timeout waiting for suppressed alert");
        } catch (RuntimeException e) {
            // Expected timeout
        }
        assertEquals(1, outputStrategy.getAlerts().size());

        TimeUnit.SECONDS.sleep(10);

        outputStrategy.resetLatch(1);
        alertManager.processAlert(highPriorityAlert2);
        outputStrategy.awaitAlerts();

        assertEquals(2, outputStrategy.getAlerts().size());
    }

    @Test
    void testProcessLowPriorityAlert() throws InterruptedException {
        Alert lowPriorityAlert = new Alert("patient1", "Low Priority Condition", System.currentTimeMillis(), -1);

        alertManager.processAlert(lowPriorityAlert);
        outputStrategy.awaitAlerts();

        assertEquals(1, outputStrategy.getAlerts().size());
    }

    // Custom output strategy to capture alerts for testing
    static class TestOutputStrategy implements AlertOutputStrategy {
        private final List<String> alerts = new ArrayList<>();
        private CountDownLatch latch;

        public TestOutputStrategy(int expectedAlertCount) {
            this.latch = new CountDownLatch(expectedAlertCount);
        }

        public void send(String message) {
            alerts.add(message);
            latch.countDown();
            System.out.println("Alert sent: " + message);
        }

        public List<String> getAlerts() {
            return alerts;
        }

        public void awaitAlerts() throws InterruptedException {
            if (!latch.await(5, TimeUnit.SECONDS)) { // Adding a timeout to avoid infinite wait
                throw new RuntimeException("Timeout waiting for alerts");
            }
        }

        public void resetLatch(int expectedAlertCount) {
            this.latch = new CountDownLatch(expectedAlertCount);
            System.out.println("Latch reset with count: " + expectedAlertCount); // Added for debugging
        }
    }
}
