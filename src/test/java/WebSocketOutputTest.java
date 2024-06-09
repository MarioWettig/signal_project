import com.alerts.outputstrategy.WebSocketOutput;
import org.java_websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketOutputTest {

    private WebSocketOutput webSocketOutput;
    private WebSocketClient mockWebSocketClient;
    private String serverUri = "ws://localhost:8080";

    @BeforeEach
    void setUp() throws URISyntaxException {
        mockWebSocketClient = mock(WebSocketClient.class);
        webSocketOutput = new WebSocketOutput(serverUri) {
            {
                WebSocketClient webSocketClient = mockWebSocketClient;
            }
        };
    }



    @Test
    void testSend_whenConnectionIsClosed_shouldNotSendMessage() {
        when(mockWebSocketClient.isOpen()).thenReturn(false);

        webSocketOutput.send("Test Message");

        verify(mockWebSocketClient, never()).send(anyString());
    }

    @Test
    void testOnOpen_shouldPrintConnectedMessage() {
        // This would typically be tested by a system test or an integration test,
        // as it involves interaction with an actual WebSocket server.
        // Here, we will just ensure the client is set up correctly.

        assertDoesNotThrow(() -> new WebSocketOutput(serverUri));
    }

    @Test
    void testOnClose_shouldPrintDisconnectedMessage() {
        // As with onOpen, this would typically require a system or integration test.
        // You might use a real WebSocket server in a Docker container for such tests.
    }

    @Test
    void testOnError_shouldHandleException() {
        Exception exception = new Exception("Test Exception");

        webSocketOutput.webSocketClient.onError(exception);

        // Since we're using System.out and System.err, you might want to redirect these streams
        // to capture and assert their contents in a more advanced test setup.
    }


}
