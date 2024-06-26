package com.data_management.readers;
import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
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
                    processMessage(message, dataStorage);
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

    public void processMessage(String message, DataStorage dataStorage) {
        //System.out.println("Client: Received message: " + message);
        String[] parts = message.split(",");
        if (parts.length == 4) {
            int patientId = Integer.parseInt(parts[0].trim());
            double measurementValue = Double.parseDouble(parts[1].trim());
            String recordType = parts[2].trim();
            long timestamp = Long.parseLong(parts[3].trim());
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            //System.out.println("Client: Data added to DataStorage");
        }
    }
}
