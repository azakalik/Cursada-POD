package ar.edu.itba.pod.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GenericSocketServer implements Closeable {
    private static Logger logger = LoggerFactory.getLogger(GenericSocketServer.class);

    public static final int PORT = 6666;
    private int visitCount = 0;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        logger.info("starting server on port {}", port);
        boolean loop = true;

        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while (loop && (inputLine = in.readLine()) != null) {
            logger.debug("received message {}", inputLine);
            if (".".equals(inputLine)) {
                loop = false;
            } else if ("1".equals(inputLine)) {
                ++visitCount;
            }
            out.println(visitCount);
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        try (GenericSocketServer server = new GenericSocketServer()) {
            server.start(PORT);
        }
    }

}