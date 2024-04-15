package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
/**
 *
 * This implementation listens for client connections on a specified port and sends data to connected clients.
 *
 * @author mariowettig
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket; // The server socket that waits for new client connections
    private Socket clientSocket; // The client socket used to communicate with the connected client
    private PrintWriter out; // output streams that sends the data

    /**
     * Constructs a TcpOutputStrategy object that listens for client connections on the specified port.
     *
     * @param port the port number on which to listen for client connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port); // Initialize the server socket
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept(); // Accept a client connection
                    out = new PrintWriter(clientSocket.getOutputStream(), true); // Initialize the output stream
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs patient data to the connected client.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp in which the data was collected
     * @param label     the label that describes the data
     * @param data      the patient data to be written into a file then stored in the directory
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) { // Check if the output stream is initialized
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data); // Format the message
            out.println(message); // Send the message to the client
        }
    }
}
