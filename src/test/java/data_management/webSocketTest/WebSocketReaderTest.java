package data_management.webSocketTest;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.readers.WebSocketReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebSocketReaderTest {
    private TestWebSocketServer server;
    private CountDownLatch latch;

    // have to make a test server to actually test whether the websocket reader actually works

    @BeforeEach
    public void startServer() throws Exception {
        latch = new CountDownLatch(1);
        server = new TestWebSocketServer(new InetSocketAddress("localhost", 8080), latch);
        server.start();
        Thread.sleep(1000);
    }

    @AfterEach
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void testWebSocketReader() throws Exception {
        DataStorage.resetInstance();
        DataStorage dataStorage = DataStorage.getDataStorageInstance();
        WebSocketReader reader = new WebSocketReader("ws://localhost:8080");

        Thread readerThread = new Thread(() -> reader.readData(dataStorage));
        readerThread.start();

        Thread.sleep(1000);

        String testMessage = "1,72.5,HeartRate,1622556000000";
        server.sendTestMessage(testMessage);

        latch.await(1, TimeUnit.SECONDS);

        Patient patient = dataStorage.getPatientMap().get(1);
        System.out.println(patient.getPatientRecords().get(0).getTimestamp());
        assertEquals(1, patient.getPatientId());
        PatientRecord record = patient.getPatientRecords().get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(1622556000000L, record.getTimestamp());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(72.5, record.getMeasurementValue(), 0.01);
    }
}
