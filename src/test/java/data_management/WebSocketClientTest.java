package data_management;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketDataClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketDataClientTest {

    private DataStorage dataStorage;
    private WebSocketDataClient webSocketDataClient;

    @BeforeEach
    void setUp() throws URISyntaxException {
        dataStorage = new DataStorage();
        webSocketDataClient = new WebSocketDataClient("ws://localhost:8080", dataStorage);
    }

    @Test
    void testOnMessage_ValidMessage() throws Exception {
        String validMessage = "1,98.6,HeartRate,1620000000000";
        webSocketDataClient.onMessage(validMessage);

        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(98.6, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1620000000000L, record.getTimestamp());
    }

    @Test
    void testOnMessage_InvalidMessage() {
        String invalidMessage = "1,98.6,HeartRate"; // Missing timestamp
        webSocketDataClient.onMessage(invalidMessage);

        // Verify that no data was added due to invalid message
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(0, records.size());
    }
}


