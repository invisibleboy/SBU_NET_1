/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author alijahanshahi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        LinkedList<String> a = new LinkedList<String>();
//        AliveSender d = new AliveSender(".", a);
//        d.start();
        PurePeer peer = new PurePeer();
        //getting peer ip in network
        System.out.println("Enter your current IP:");
        Scanner n = new Scanner(System.in);
        peer.ip = n.next();

        System.out.println("Enter address of your Shared directory:");
        Scanner k = new Scanner(System.in);
        peer.SharedFolder = n.next();
        System.out.println("you shared:"+peer.SharedFolder);
        //getting server IP and port
        System.out.println("Enter Server IP:");
        Scanner m = new Scanner(System.in);
        String serverIP = m.next();

        System.out.println("Enter Server PORT:");
        Scanner o = new Scanner(System.in);
        int serverPort = o.nextInt();

        Server server = new Server(serverIP, serverPort);
        //incoming socket -> for message come in and responsing them
        //outgointToServer -> for server-peer interactions
        System.out.println("Connecting to Server : " + serverIP + ":" + serverPort);
        ServerPeerInteractions serverPeer = new ServerPeerInteractions(server,peer);
        ServerSocket ListenSocket=null;
        Socket incoming=null;
        try {
            ListenSocket = new ServerSocket(peer.port);
            System.out.println("this peer is listening on port: " + peer.port);
        } catch (IOException ex) {
            System.out.println("ServerSocket problem");
        }
        try {
            while (true) {
                incoming = ListenSocket.accept();
                PeerHandler h = new PeerHandler(incoming, peer , server);
                h.start();
            }
        } catch (Exception e) {
            System.out.println("Socket problem");
        }
 
    }
}