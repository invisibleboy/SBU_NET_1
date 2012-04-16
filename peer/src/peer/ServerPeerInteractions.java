/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alijahanshahi
 */
class ServerPeerInteractions extends Thread {

    private Socket skt;
    private PurePeer peer;
    private int myPort, serverPort;
    private String myIP;
    private Server server;
    private DataOutputStream out;
    private LinkedList<String> files;

    public ServerPeerInteractions(Server se, PurePeer p) {
        peer = p;
        server = new Server(se.ip, se.port);
        myIP = peer.ip;
        serverPort = server.port;
        myPort = peer.port;

        //getting list of aliveMessage
        files = new LinkedList<String>();
        sharedDirectoryFiles();
        connectToServer();

        AliveSender aliveSender = new AliveSender(peer, server);
        aliveSender.start();
        /*
        sending connect message to server with a string in below format:
        hi;IP;port;filename1;filename2;...;...
         */
        this.start();
    }

    private void connectToServer() {
        String connectmessage = new String("hi;" + myIP + ";" + myPort + ";");
        for (int i = 0; i < files.size() - 1; i++) {
            connectmessage = connectmessage.concat(files.get(i) + ";");
        }
        connectmessage = connectmessage.concat(files.get(files.size() - 1));
        try {
            skt = new Socket(server.ip, server.port);
            System.out.println("CONNECT message sent to server : " + connectmessage);
            PrintWriter a = new PrintWriter(skt.getOutputStream(), true);
            a.println(connectmessage);
            skt.close();

        } catch (IOException ex) {
            System.out.println("Sending CONNECT message  in ServerPeerInteraction class/ ConnectToServer has a problem!");
        }

    }

    private void sharedDirectoryFiles() {
        System.out.println("folder:" + peer.SharedFolder);
        File firstiew = new File(peer.SharedFolder);
        File[] list = firstiew.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                files.add(list[i].getName());
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Welcome to NAPSTER ver 1.0!");
        System.out.println("please write your choose form list below: (you can get list of below with typeing 'help' ");
        System.out.println("'list' for getting list of files that exist in network from server");
        System.out.println("'download;[filename]'  for downloading 'filename' file form network");
        System.out.println("'bye'  for disconnecting from network");
        Scanner d = new Scanner(System.in);
        String userCommand = new String();
        while (!userCommand.startsWith("bye")) {
            userCommand = d.nextLine();
            System.out.println("command= " + userCommand);
            if (userCommand.startsWith("list")) {
                String message = new String("list;" + PurePeer.name);
                try {
                    skt = new Socket(server.ip, server.port);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("list message sent to server : " + message);
                PrintWriter a = null;
                try {
                    a = new PrintWriter(skt.getOutputStream(), true);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                a.println(message);
                try {
                    skt.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (userCommand.startsWith("download")) {
                String[] f = new String[2];
                f = userCommand.split(";");
                String message = new String("request;" + PurePeer.name + ";" + f[1]);
                try {
                    skt = new Socket(server.ip, server.port);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("download message sent to server : " + message);
                PrintWriter a = null;
                try {
                    a = new PrintWriter(skt.getOutputStream(), true);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                a.println(message);
                try {
                    skt.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (userCommand.startsWith("bye")) {
                String message = new String("bye;" + PurePeer.name);
                try {
                    skt = new Socket(server.ip, server.port);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("bye message sent to server : " + message);
                PrintWriter a = null;
                try {
                    a = new PrintWriter(skt.getOutputStream(), true);
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                a.println(message);
                try {
                    skt.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerPeerInteractions.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            } else if (userCommand.startsWith("help")) {
                System.out.println("\n\n\n\nplease write your choose form list below: (you can get list of below with typeing 'help' ");
                System.out.println("'list' for getting list of files that exist in network from server");
                System.out.println("'download;[filename]'  for downloading 'filename' file form network");
                System.out.println("'bye'  for disconnecting from network");
            }
            else{
                System.out.println("bad command format!");
            }
        }
    }
}
