package alerts.basicAlerts;

import com.alerts.alerts.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertHeartRateTest {
    private DataStorage storage;
    HeartRateStrategy strategy = new HeartRateStrategy();


    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
        storage = DataStorage.getDataStorageInstance();
    }

    @Test
    public void abnormalHearRateTest(){
        storage.addPatientData(123430,1000,"ECG",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Abnormal Heart Rate",alert.getCondition());
    }

    @Test
    public void irregularHeartRateTest(){
        storage.addPatientData(123430,20,"ECG",System.currentTimeMillis());
        storage.addPatientData(123430,25,"ECG",System.currentTimeMillis());
        storage.addPatientData(123430,14,"ECG",System.currentTimeMillis());
        storage.addPatientData(123430,30,"ECG",System.currentTimeMillis());
        storage.addPatientData(123430,40,"ECG",System.currentTimeMillis());
        storage.addPatientData(123430,80,"ECG",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Irregular Heart Rate",alert.getCondition());
    }

}
