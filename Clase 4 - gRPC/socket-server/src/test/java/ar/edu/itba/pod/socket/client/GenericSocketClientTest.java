package ar.edu.itba.pod.socket.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GenericSocketClientTest {

    public static final int PORT = 6666;
    private GenericSocketClient client;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        client = new GenericSocketClient();
        client.startConnection("127.0.0.1", PORT);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws IOException {
        client.stopConnection();
    }

    @Test
    public final void test() throws IOException {

        assertEquals("1", client.sendMessage("1"));
        assertEquals("2", client.sendMessage("1"));

        assertEquals("2", client.sendMessage("2"));
        assertEquals("2", client.sendMessage("."));

    }
}