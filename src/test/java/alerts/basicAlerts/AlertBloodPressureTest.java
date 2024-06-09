package alerts.basicAlerts;

import com.alerts.alerts.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertBloodPressureTest {
    private DataStorage storage;
    BloodPressureStrategy strategy= new BloodPressureStrategy();


    @BeforeEach
    void setUp() {
        DataStorage.resetInstance();
        storage = DataStorage.getDataStorageInstance();
    }

    @Test
    public void testThreshHoldDiastolic(){

        storage.addPatientData(123430,23,"DiastolicPressure",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Critical Threshold pressure",alert.getCondition());


        storage.addPatientData(123430,300,"DiastolicPressure",System.currentTimeMillis());

        Alert alert2=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Critical Threshold pressure",alert2.getCondition());
    }

    @Test
    public void testThreshHoldSystolic(){

        storage.addPatientData(123430,23,"SystolicPressure",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Critical Threshold pressure",alert.getCondition());


        storage.addPatientData(123430,300,"SystolicPressure",System.currentTimeMillis());

        Alert alert2=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Critical Threshold pressure",alert2.getCondition());
    }

    @Test
    public void testTrendUp(){

        storage.addPatientData(123430,65,"DiastolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,76,"DiastolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,87,"DiastolicPressure",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Trend Alert",alert.getCondition());

        storage.addPatientData(123430,95,"SystolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,105,"SystolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,115,"SystolicPressure",System.currentTimeMillis());

        Alert alert2=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Trend Alert",alert2.getCondition());
    }

    @Test
    public void testTrendDown(){

        storage.addPatientData(123430,100,"DiastolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,90,"DiastolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,70,"DiastolicPressure",System.currentTimeMillis());

        Alert alert=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Trend Alert",alert.getCondition());

        storage.addPatientData(123430,120,"SystolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,110,"SystolicPressure",System.currentTimeMillis());
        storage.addPatientData(123430,100,"SystolicPressure",System.currentTimeMillis());

        Alert alert2=strategy.checkAlert(storage.getPatientMap().get(123430));
        assertEquals("Trend Alert",alert2.getCondition());
    }



}
