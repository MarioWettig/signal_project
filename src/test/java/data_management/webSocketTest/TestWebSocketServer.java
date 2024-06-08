package data_management.webSocketTest;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class TestWebSocketServer extends WebSocketServer {

    private WebSocket clientConnection;
    private final CountDownLatch latch;

    public TestWebSocketServer(InetSocketAddress address, CountDownLatch latch) {
        super(address);
        this.latch = latch;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Server: new connection opened");
        this.clientConnection = conn;
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Server: connection closed");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Server: received message: " + message);
        latch.countDown();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server: started successfully");
    }

    public void sendTestMessage(String message) {
        if (clientConnection != null) {
            clientConnection.send(message);
        }
    }

}
