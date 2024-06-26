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
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

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
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

        outputStrategy.resetLatch(1);
        alertManager.processAlert(highPriorityAlert2);
        boolean timedOut = !outputStrategy.awaitAlerts(2, TimeUnit.SECONDS);
        assertTrue(timedOut, "Expected timeout waiting for suppressed alert");
        assertEquals(1, outputStrategy.getAlerts().size());

        TimeUnit.SECONDS.sleep(5);

        outputStrategy.resetLatch(1);

        timestamp = System.currentTimeMillis();
        alertManager.processAlert(new Alert(patientId, "High Priority Condition", timestamp + 100000, 1));
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

        assertEquals(2, outputStrategy.getAlerts().size());
    }

    @Test
    void testProcessMediumPriorityAlert() throws InterruptedException {
        Alert lowPriorityAlert = new Alert("patient1", "Medium Priority Condition", System.currentTimeMillis(), 0);

        alertManager.processAlert(lowPriorityAlert);
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

        assertEquals(1, outputStrategy.getAlerts().size());
    }

    @Test
    void testProcessLowPriorityAlert() throws InterruptedException {
        Alert lowPriorityAlert = new Alert("patient1", "Low Priority Condition", System.currentTimeMillis(), -1);

        alertManager.processAlert(lowPriorityAlert);
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

        assertEquals(0, outputStrategy.getAlerts().size());
    }
}
