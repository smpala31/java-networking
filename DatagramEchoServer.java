/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itjvaweek5_2;

/**
 *
 * @author UMOTHSE
 */
// file: DatagramEchoServer.java
// package networkingdemo;


import java.net.*;
import java.io.*;

/**
 * Simple UDP echo server using DatagramSocket + DatagramPacket.
 * - listens on PORT
 * - receives packets (datagrams)
 * - prints sender info and payload
 * - sends the same data back (echo)
 *
 * UDP is connectionless: packets may be lost or arrive out-of-order.
 */
public class DatagramEchoServer {
    public static final int PORT = 5000; // textbook uses 5000

    public static void main(String[] args) {
        System.out.println("Starting UDP DatagramEchoServer on port " + PORT);
        // try-with-resources will close the socket if main exits or exception occurs
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            // buffer reused for simplicity — always respect the returned packet length
            byte[] buffer = new byte[1024];

            while (true) {
                // prepare packet for receiving
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                // this call blocks until a packet arrives
                socket.receive(receivePacket);

                // extract useful pieces of information
                InetAddress clientAddress = receivePacket.getAddress(); // hostname/IP
                int clientPort = receivePacket.getPort();               // sender port
                int length = receivePacket.getLength();                 // actual data length
                // construct String from the received bytes (respect length)
                String message = new String(receivePacket.getData(), 0, length, "UTF-8");

                System.out.printf("Received %d bytes from %s:%d -> \"%s\"%n",
                                  length, clientAddress.getHostAddress(), clientPort, message);

                // create an echo packet and send it back to the client
                byte[] sendData = message.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

                socket.send(sendPacket); // send is non-blocking on many platforms (but still may throw IOException)
            }

        } catch (SocketException se) {
            System.err.println("Socket error when creating/using DatagramSocket: " + se.getMessage());
        } catch (IOException ioe) {
            System.err.println("IO error while receiving/sending datagram: " + ioe.getMessage());
        }
    }
}

