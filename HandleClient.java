/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itjvaweek5_2;

/**
 *
 * @author UMOTHSE
 */
// file: HandleClient.java
// package networkingdemo;
import java.net.*;
import java.io.*;

/**
 * HandleClient implements Runnable — one instance per connected client.
 * This example reads a UTF string from the client and replies with a short acknowledgement.
 *
 * In real systems you would:
 * - keep the thread open for a session with multiple requests
 * - or use a thread-pool (ExecutorService) instead of raw Thread creation
 */
public class HandleClient implements Runnable {
    private final Socket socket;
    private final int clientId;

    public HandleClient(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        System.out.println("Handler started for client #" + clientId);
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // read one message (blocking)
            String message = in.readUTF();
            System.out.println("Received from client #" + clientId + ": " + message);

            // send a reply
            String reply = "Server (client #" + clientId + ") received your message";
            out.writeUTF(reply);
            out.flush(); // ensure bytes are sent immediately

        } catch (IOException ioe) {
            System.err.println("I/O error with client #" + clientId + ": " + ioe.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
            System.out.println("Connection with client #" + clientId + " closed.");
        }
    }
}

