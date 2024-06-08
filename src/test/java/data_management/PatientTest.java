package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

public class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(1);
    }

    @Test
    void testAddRecord() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(100.0, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1714376789050L, record.getTimestamp());
        assertEquals(1, record.getPatientId());
    }

    @Test
    void testGetRecordsWithinRange() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);
        patient.addRecord(120.0, "HeartRate", 1714376789051L);
        patient.addRecord(80.0, "BloodPressure", 1714376789052L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789051L);

        assertEquals(2, records.size());

        PatientRecord firstRecord = records.get(0);
        assertEquals(100.0, firstRecord.getMeasurementValue());
        assertEquals("HeartRate", firstRecord.getRecordType());
        assertEquals(1714376789050L, firstRecord.getTimestamp());

        PatientRecord secondRecord = records.get(1);
        assertEquals(120.0, secondRecord.getMeasurementValue());
        assertEquals("HeartRate", secondRecord.getRecordType());
        assertEquals(1714376789051L, secondRecord.getTimestamp());
    }

    @Test
    void testGetRecordsOutsideRange() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);
        patient.addRecord(120.0, "HeartRate", 1714376789051L);
        patient.addRecord(80.0, "BloodPressure", 1714376789052L);

        List<PatientRecord> records = patient.getRecords(1714376789053L, 1714376789054L);

        assertTrue(records.isEmpty());
    }

    @Test
    void testGetRecordsEmptyPatient() {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertTrue(records.isEmpty());
    }

    @Test
    void testAddMultipleRecords() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);
        patient.addRecord(120.0, "HeartRate", 1714376789051L);
        patient.addRecord(80.0, "BloodPressure", 1714376789052L);

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(3, records.size());
    }

    @Test
    void testRecordsExceedingMaxLimit() {
        for (int i = 0; i < 1001; i++) {
            patient.addRecord(i, "TestRecord", 1714376789050L + i);
        }

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(1000, records.size());

        // Ensure the first record is removed
        PatientRecord firstRecord = records.get(0);
        assertEquals(1.0, firstRecord.getMeasurementValue());
    }

    @Test
    void testAddRecordsWithDifferentTypes() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);
        patient.addRecord(80.0, "BloodPressure", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(2, records.size());

        PatientRecord firstRecord = records.get(0);
        assertEquals("HeartRate", firstRecord.getRecordType());

        PatientRecord secondRecord = records.get(1);
        assertEquals("BloodPressure", secondRecord.getRecordType());
    }

    @Test
    void testAddRecordsWithDuplicateTimestamps() {
        patient.addRecord(100.0, "HeartRate", 1714376789050L);
        patient.addRecord(120.0, "HeartRate", 1714376789050L);

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(2, records.size());
    }
}
