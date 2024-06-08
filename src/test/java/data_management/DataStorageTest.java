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
        DataStorage.resetInstance();
        storage = DataStorage.getInstance();
    }


    @Test
    void testAddAndGetRecords() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

        assertEquals(2, records.size());

        PatientRecord firstRecord = records.get(0);
        assertEquals(100.0, firstRecord.getMeasurementValue());
        assertEquals("WhiteBloodCells", firstRecord.getRecordType());
        assertEquals(1714376789050L, firstRecord.getTimestamp());

        PatientRecord secondRecord = records.get(1);
        assertEquals(200.0, secondRecord.getMeasurementValue());
        assertEquals("WhiteBloodCells", secondRecord.getRecordType());
        assertEquals(1714376789051L, secondRecord.getTimestamp());
    }

    @Test
    void testAddAndGetRecordsForMultiplePatients() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 200.0, "RedBloodCells", 1714376789051L);

        List<PatientRecord> recordsPatient1 = storage.getRecords(1, 1714376789050L, 1714376789051L);
        List<PatientRecord> recordsPatient2 = storage.getRecords(2, 1714376789050L, 1714376789051L);

        assertEquals(1, recordsPatient1.size());
        assertEquals(1, recordsPatient2.size());

        PatientRecord recordPatient1 = recordsPatient1.get(0);
        assertEquals(100.0, recordPatient1.getMeasurementValue());
        assertEquals("WhiteBloodCells", recordPatient1.getRecordType());
        assertEquals(1714376789050L, recordPatient1.getTimestamp());

        PatientRecord recordPatient2 = recordsPatient2.get(0);
        assertEquals(200.0, recordPatient2.getMeasurementValue());
        assertEquals("RedBloodCells", recordPatient2.getRecordType());
        assertEquals(1714376789051L, recordPatient2.getTimestamp());
    }

    @Test
    void testGetRecordsForNonExistentPatient() {
        List<PatientRecord> records = storage.getRecords(999, 1714376789050L, 1714376789051L);
        assertEquals(0, records.size());
    }

    @Test
    void testGetRecordsWhenNoRecordsExistInRange() {
        storage.addPatientData(3, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(3, 200.0, "WhiteBloodCells", 1714376789052L);
        List<PatientRecord> records = storage.getRecords(3, 1714376789051L, 1714376789051L);

        assertEquals(0, records.size());
    }

}
