package alerts;

import com.alerts.alerts.BloodOxygenAlert;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AlertGettersTest{

    DataStorage storage;

    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
        storage = DataStorage.getDataStorageInstance();
    }

    @Test
    public void testSetters(){
        BloodOxygenAlert alert= new BloodOxygenAlert("123430","Trend Alert",230494324L,0);

        assertEquals("123430", alert.getPatientId());
        assertEquals("Trend Alert", alert.getCondition());
        assertEquals(230494324L, alert.getTimestamp());
        assertEquals(0, alert.getPriority());

    }
}
