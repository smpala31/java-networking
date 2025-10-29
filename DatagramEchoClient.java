/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itjvaweek5_2;

/**
 *
 * @author UMOTHSE
 */
// file: DatagramEchoClient.java
// package networkingdemo;
import java.net.*;
import java.io.*;

/**
 * Simple UDP client:
 * - sends a single datagram to the server
 * - waits for a reply (with a timeout to show unreliability)
 *
 * Run: java networkingdemo.DatagramEchoClient "Hello UDP"
 */
public class DatagramEchoClient {
    public static final int SERVER_PORT = 5000;
    public static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) {
        String message = args.length > 0 ? String.join(" ", args) : "Hello from UDP client";

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddr = InetAddress.getByName(SERVER_HOST);
            byte[] sendData = message.getBytes("UTF-8");

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddr, SERVER_PORT);
            socket.send(sendPacket);
            System.out.println("Sent packet to " + SERVER_HOST + ":" + SERVER_PORT + " -> " + message);

            // prepare to receive the echo; set timeout so we don't wait forever
            socket.setSoTimeout(3000); // 3 seconds
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(receivePacket);
                String reply = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                System.out.println("Reply from server: " + reply);
            } catch (SocketTimeoutException ste) {
                System.out.println("No reply from server (timeout). This illustrates UDP unreliability.");
            }

        } catch (IOException ioe) {
            System.err.println("UDP client IO error: " + ioe.getMessage());
        }
    }
}

