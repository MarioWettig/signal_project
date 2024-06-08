package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.readers.TextFileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TextFileReaderTest {

    @TempDir
    Path tempDir;

    private DataStorage dataStorage;
    private TextFileReader textFileReader;

    @Test
    void testReadData() throws IOException {
        File tempFile = tempDir.resolve("patient_data.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Patient ID: 11, Timestamp: 1714376789050, Label: HeartRate, Data: 100.0\n");
            writer.write("Patient ID: 12, Timestamp: 1714376789051, Label: HeartRate, Data: 120.0\n");
            writer.write("Patient ID: 13, Timestamp: 1714376789052, Label: BloodPressure, Data: 80.0\n");
        }

        DataStorage.resetInstance();
        dataStorage = DataStorage.getInstance();
        textFileReader = new TextFileReader(tempFile.getAbsolutePath());
        textFileReader.readData(dataStorage);

        List<PatientRecord> recordsPatient1 = dataStorage.getRecords(11, 1714376789050L, 1714376789050L);
        assertEquals(1, recordsPatient1.size());

        PatientRecord record1 = recordsPatient1.get(0);
        assertEquals(11, record1.getPatientId());
        assertEquals(1714376789050L, record1.getTimestamp());
        assertEquals("HeartRate", record1.getRecordType());
        assertEquals(100.0, record1.getMeasurementValue());

        List<PatientRecord> recordsPatient2 = dataStorage.getRecords(12, 1714376789051L, 1714376789051L);
        assertEquals(1, recordsPatient2.size());

        PatientRecord record2 = recordsPatient2.get(0);
        assertEquals(12, record2.getPatientId());
        assertEquals(1714376789051L, record2.getTimestamp());
        assertEquals("HeartRate", record2.getRecordType());
        assertEquals(120.0, record2.getMeasurementValue());

        List<PatientRecord> recordsPatient3 = dataStorage.getRecords(13, 1714376789052L, 1714376789052L);
        assertEquals(1, recordsPatient3.size());

        PatientRecord record3 = recordsPatient3.get(0);
        assertEquals(13, record3.getPatientId());
        assertEquals(1714376789052L, record3.getTimestamp());
        assertEquals("BloodPressure", record3.getRecordType());
        assertEquals(80.0, record3.getMeasurementValue());
    }
}
