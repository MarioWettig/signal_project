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
        // Create a mock real-time strategy that generates an alert
        MockRealTimeStrategy realTimeStrategy = new MockRealTimeStrategy(true);
        healthDataProcessor = new HealthDataProcessor(outputStrategy, Arrays.asList(realTimeStrategy));

        // Create a mock patient
        Patient mockPatient = new Patient(1);
        healthDataProcessor.onDataAdded(mockPatient);

        // Wait for alerts
        outputStrategy.awaitAlerts(5, TimeUnit.SECONDS);

        // Verify the alert
        assertEquals(1, outputStrategy.getAlerts().size());
        assertTrue(outputStrategy.getAlerts().get(0).contains("Mock RealTime Condition"));
    }

    @Test
    void testPeriodicStrategiesGenerateAlerts() throws InterruptedException {
        // Create a mock periodic strategy that generates an alert
        MockPeriodicStrategy periodicStrategy = new MockPeriodicStrategy(true);
        healthDataProcessor = new HealthDataProcessor(outputStrategy, null);

        // Create a mock patient and add it to the data storage
        Patient mockPatient = new Patient(1);
        DataStorage.getDataStorageInstance().addPatientData(mockPatient.getPatientId(), 80.0, "MockData", System.currentTimeMillis());

        // Wait for the periodic check to run
        TimeUnit.SECONDS.sleep(16); // Wait for the periodic check interval

        // Verify the alert
        assertEquals(1, outputStrategy.getAlerts().size());
        assertTrue(outputStrategy.getAlerts().get(0).contains("Mock Periodic Condition"));
    }
}
