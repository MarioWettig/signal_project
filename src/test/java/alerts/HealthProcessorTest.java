package alerts;

import com.alerts.health_processor.HealthDataProcessor;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HealthProcessorTest {
    private TestOutputStrategy outputStrategy;
    private HealthDataProcessor healthDataProcessor;

    @BeforeEach
    void setUp() {
        outputStrategy = new TestOutputStrategy(1);
    }

    @Test
    void testRealTimeStrategiesGenerateAlerts() throws InterruptedException {
        MockRealTimeStrategy realTimeStrategy = new MockRealTimeStrategy(true);
        healthDataProcessor = new HealthDataProcessor(outputStrategy, Arrays.asList(realTimeStrategy));

        Patient mockPatient = new Patient(1);
        healthDataProcessor.onDataAdded(mockPatient);

        boolean alertsReceived = outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);
        assertTrue(alertsReceived, "Alerts were not received in the expected time");

        assertEquals(1, outputStrategy.getAlerts().size());
        assertTrue(outputStrategy.getAlerts().get(0).contains("Mock RealTime Condition"));
    }

    @Test
    void testNoAlertsWhenRealTimeStrategyDoesNotGenerateAlerts() throws InterruptedException {
        MockRealTimeStrategy realTimeStrategy = new MockRealTimeStrategy(false);
        healthDataProcessor = new HealthDataProcessor(outputStrategy, Arrays.asList(realTimeStrategy));

        Patient mockPatient = new Patient(1);
        healthDataProcessor.onDataAdded(mockPatient);

        boolean alertsReceived = outputStrategy.awaitAlerts(1, TimeUnit.SECONDS);
        assertFalse(alertsReceived, "No alerts should have been received");
        assertEquals(0, outputStrategy.getAlerts().size());
    }

    @Test
    void testMultipleRealTimeStrategiesGenerateAlerts() throws InterruptedException {
        MockRealTimeStrategy realTimeStrategy1 = new MockRealTimeStrategy(true);
        MockRealTimeStrategy realTimeStrategy2 = new MockRealTimeStrategy(true);
        healthDataProcessor = new HealthDataProcessor(outputStrategy, Arrays.asList(realTimeStrategy1, realTimeStrategy2));

        Patient mockPatient = new Patient(1);
        outputStrategy.resetLatch(2);
        healthDataProcessor.onDataAdded(mockPatient);

        boolean alertsReceived = outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);
        assertTrue(alertsReceived, "Alerts were not received in the expected time");

        assertEquals(2, outputStrategy.getAlerts().size());
    }

    private void addDummyDataToPatient(Patient patient) {
        patient.addRecord(100.0, "ECG", System.currentTimeMillis());
        patient.addRecord(120.0, "BloodPressure", System.currentTimeMillis());
        patient.addRecord(90.0, "OxygenSaturation", System.currentTimeMillis());
    }

    @Test
    void testInitializationWithNullRealTimeStrategies() throws InterruptedException {
        healthDataProcessor = new HealthDataProcessor(outputStrategy, null);

        Patient mockPatient = new Patient(1);
        addDummyDataToPatient(mockPatient);
        healthDataProcessor.onDataAdded(mockPatient);

        outputStrategy.resetLatch(1);
        boolean alertsReceived = outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);
        assertTrue(alertsReceived, "Alerts were not received in the expected time");
        assertEquals(1, outputStrategy.getAlerts().size());
    }
}
