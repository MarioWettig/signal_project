package com.data_management.readers;
import com.data_management.DataStorage;
import com.data_management.WebSocketClientData;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

public class WebSocketReader implements DataReader {
    private String serverUri;

    public WebSocketReader(String serverUri) {
        this.serverUri = serverUri;
    }

    @Override
    public void readData(DataStorage dataStorage) {
        final CountDownLatch latch = new CountDownLatch(1);

        try {
            WebSocketClient client = new WebSocketClient(new URI(serverUri)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to server");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Client: Received message: " + message);
                    String[] parts = message.split(",");
                    if (parts.length == 4) {
                        int patientId = Integer.parseInt(parts[0]);
                        double measurementValue = Double.parseDouble(parts[1]);
                        String recordType = parts[2];
                        long timestamp = Long.parseLong(parts[3]);
                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed");
                    latch.countDown();
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connect();
            latch.await(); // Wait until the connection is closed
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
