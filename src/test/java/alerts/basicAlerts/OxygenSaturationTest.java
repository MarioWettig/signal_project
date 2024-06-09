package alerts.basicAlerts;

import com.alerts.alerts.Alert;
import com.alerts.strategies.HypotensiveHypoxemiaStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OxygenSaturationTest {
    private DataStorage storage;
    OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();

    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
        storage = DataStorage.getDataStorageInstance();
    }

    @Test
    public void testLowSaturation() {

        storage.addPatientData(123430, 23, "SystolicPressure", System.currentTimeMillis());
        storage.addPatientData(123430, 90, "Saturation", System.currentTimeMillis());

        Alert alert = this.strategy.checkAlert(this.storage.getPatientMap().get(123430));
        assertEquals("Low Saturation", alert.getCondition());
    }

    @Test
    public void testSaturationDrop() {

        storage.addPatientData(123430, 99, "Saturation", System.currentTimeMillis());
        storage.addPatientData(123430, 92, "Saturation", System.currentTimeMillis());

        Alert alert = this.strategy.checkAlert(this.storage.getPatientMap().get(123430));
        assertEquals("Saturation drop", alert.getCondition());

    }


}