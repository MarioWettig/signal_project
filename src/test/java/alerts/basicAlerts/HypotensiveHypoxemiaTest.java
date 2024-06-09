package alerts.basicAlerts;

import com.alerts.alerts.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HypotensiveHypoxemiaStrategy;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HypotensiveHypoxemiaTest {
    private DataStorage storage;
    HypotensiveHypoxemiaStrategy strategy = new HypotensiveHypoxemiaStrategy();


    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
        storage = DataStorage.getDataStorageInstance();
    }

    @Test
    public void testHypotensive() {

        storage.addPatientData(123430, 23, "SystolicPressure", System.currentTimeMillis());
        storage.addPatientData(123430, 90, "Saturation", System.currentTimeMillis());

        Alert alert = this.strategy.checkAlert(this.storage.getPatientMap().get(123430));
        assertEquals("Hypotensive Hypoxemia", alert.getCondition());
    }

}
