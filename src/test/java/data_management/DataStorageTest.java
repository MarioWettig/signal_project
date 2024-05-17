package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }


    @Test
    void testAddAndGetRecords() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

        // Validate the size of the retrieved records
        assertEquals(2, records.size());

        // Validate the first record
        PatientRecord firstRecord = records.get(0);
        assertEquals(100.0, firstRecord.getMeasurementValue());
        assertEquals("WhiteBloodCells", firstRecord.getRecordType());
        assertEquals(1714376789050L, firstRecord.getTimestamp());

        // Validate the second record
        PatientRecord secondRecord = records.get(1);
        assertEquals(200.0, secondRecord.getMeasurementValue());
        assertEquals("WhiteBloodCells", secondRecord.getRecordType());
        assertEquals(1714376789051L, secondRecord.getTimestamp());
    }


}
