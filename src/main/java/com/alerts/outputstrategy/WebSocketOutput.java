package com.alerts.outputstrategy;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketOutput implements AlertOutputStrategy {
    public WebSocketClient webSocketClient;
    private String serverUri;

    public WebSocketOutput(String serverUri) {
        this.serverUri = serverUri;
        try {
            webSocketClient = new WebSocketClient(new URI(serverUri)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to WebSocket server: " + serverUri);
                }

                @Override
                public void onMessage(String message) {
                    // Handle incoming messages if needed
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from WebSocket server: " + serverUri);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
        } else {
            System.err.println("WebSocket connection is not open. Message not sent: " + message);
        }
    }
}
