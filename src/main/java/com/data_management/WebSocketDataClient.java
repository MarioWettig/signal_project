package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocketDataClient is a client class that connects to a WebSocket server
 * to receive and process real-time data streams. It implements the DataReader
 * interface to read and process incoming data messages.
 */
public class WebSocketDataClient extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;

    /**
     * Constructs a new WebSocketDataClient.
     *
     * @param serverUri    The URI of the WebSocket server to connect to.
     * @param dataStorage  The DataStorage instance used to store parsed data.
     * @throws URISyntaxException If the provided serverUri is not a valid URI.
     */
    public WebSocketDataClient(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        super(new URI(serverUri));
        this.dataStorage = dataStorage;
    }

    /**
     * Called when the connection to the WebSocket server is opened.
     *
     * @param handshake The handshake data sent by the server.
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to server");
    }

    /**
     * Called when a message is received from the WebSocket server.
     *
     * @param message The message received from the server.
     */
    @Override
    public void onMessage(String message) {
        try {
            readData(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code     The status code indicating the reason for closure.
     * @param reason   A string describing the reason for closure.
     * @param remote   Indicates whether the connection was closed by the remote host.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed with exit code " + code + " additional info: " + reason);
    }

    /**
     * Called when an error occurs.
     *
     * @param ex The exception representing the error that occurred.
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Reads and processes incoming data messages.
     * This method parses the message and stores the data using the DataStorage instance.
     *
     * @param message The data message received from the WebSocket server.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void readData(String message) throws IOException {
        String[] parts = message.split(",");
        if (parts.length == 4) {
            try {
                int patientId = Integer.parseInt(parts[0]);
                double measurementValue = Double.parseDouble(parts[1]);
                String recordType = parts[2];
                long timestamp = Long.parseLong(parts[3]);

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse reading: " + message);
            }
        } else {
            System.err.println("Invalid measurement format: " + message);
        }
    }
}

