/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.net.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alijahanshahi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {


        //getting peer ip in network
        System.out.println("Enter Server ip:");
        Scanner n = new Scanner(System.in);
        String ip = n.next();
        System.out.println("your IP is: " + ip);
        int serverPort = randomProt();
        //incoming socket -> for massage come in and responsing them
        //outgointToServer -> for server-peer interactions
        ServerSocket ListenSocket = null;
        Socket incoming = null;
        Socket outgoingToServer = null;
        try {
            ListenSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening on port: " + serverPort);
        } catch (IOException ex) {
            System.out.println("ServerSocket problem");
        }
        try {
            while (true) {
                incoming = ListenSocket.accept();
                PeerHandler h = new PeerHandler(incoming, ip, serverPort);
            }
        } catch (Exception e) {
            System.out.println("Socket problem");
        }

    }
    private static int randomProt(){

        return (1000 + (int)(Math.random()*60000));
    }
}

class PeerHandler extends Thread {

    Socket skt;
    String ip;
    Integer mainPort;
    DataInputStream input;
    DataOutputStream output;
    FileInputStream reader;
    FileOutputStream writer;

    public PeerHandler(Socket s, String ip, int port) {
        try {
            skt = s;
            this.ip = ip;
            mainPort = new Integer(port + 1);
            input = new DataInputStream(skt.getInputStream());
            output = new DataOutputStream(skt.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOstream making problem!");
        }
        this.start();
    }

    private void sendRequestTopeer(String Port, String ip, String fileName) {
        //
        Integer peerPort = new Integer(Port);
        System.out.println("port:" + peerPort);
        Socket download = null;
        DataOutputStream requestForDownload = null;
        try {
            download = new Socket(ip, peerPort);
        } catch (UnknownHostException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        } catch (IOException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        try {
            requestForDownload = new DataOutputStream(download.getOutputStream());
        } catch (IOException ex) {
            System.out.println("newing DataOutputStream for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        String df = new String("give-me;");
        df = df.concat(this.ip + ";" + mainPort.toString() + ";");
        df = df.concat(fileName);
        System.out.println("sent massage : " + df);
//         System.out.println("ip & port: "+this.ip+" "+mainPort);
        byte[] send = new byte[1024];
        for (int i = 0; i < df.length(); i++) {
            send[i] = (byte) df.charAt(i);
        }
        try {
            requestForDownload.write(send);
        } catch (IOException ex) {
            System.out.println("sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        System.out.println("Download request sent to : " + ip + " for downloading file: " + fileName);
        Downloader downloader = new Downloader(fileName, mainPort);
    }

    @Override
    public void run() {
        try {
            int b = 0, c = 0;
            byte[] data = new byte[1024];
            char[] request = new char[1024];
            if ((b = input.read(data)) >= 0) {
                for (int i = 0; i < 1024; i++) {
                    request[c] = (char) data[i];
                    c++;
                }
            } else {
                System.out.println("file finished");
            }
            String massage = new String(request);
            String[] parameters = massage.split(";");
            System.out.println("massage from: " + skt.getInetAddress() + " massage: " + massage);
            //response recieved from server
            // 0-> no file found
            // 1-> parameters are: ip-port-filename
            if (parameters[0].startsWith("response")) {
                if (parameters[1].startsWith("1")) {
                    //sending download request 'give-me;filename' to peer
                    //and downloading file from connection
                    sendRequestTopeer(parameters[3], parameters[2], parameters[4]);
                } else {
                    System.out.println("Requested file was not found!");
                }
            } //here peer must send requested file
            else if (parameters[0].startsWith("give-me")) {
                Sender sender = new Sender(parameters[1], parameters[2], parameters[3]);
            } else {
                System.out.println("Wrong massage format!");
            }
        } catch (IOException ex) {
            System.out.println("IOstream reading problem!");
        }

    }
}

class Downloader {

    int ListenPortForFile;
    ServerSocket listenSocket;
    Socket recieveSocket;
    FileOutputStream writer;
    DataInputStream input;
    String fileName;

    public Downloader(String fileName, int port) {
        this.fileName = fileName;
        ListenPortForFile = port;
        try {
            listenSocket = new ServerSocket(ListenPortForFile);
        } catch (IOException ex) {
            System.out.println("IO: listenSocket for reciving file in downloader class has problem");
        }
        try {
            recieveSocket = listenSocket.accept();
        } catch (IOException ex) {
            System.out.println("IO: recieveSocket for reciving file in downloader class has problem");
        }
        try {
            input = new DataInputStream(recieveSocket.getInputStream());
            writer = new FileOutputStream("downloaded " + fileName);
        } catch (IOException ex) {
            System.out.println("IO: downloader has problem");
        }
        Download();
        try {
            listenSocket.close();
        } catch (IOException ex) {
            System.out.println("Closing listenSocket on reciever peer has a problem!");
        }
    }

    private void Download() {

        int t = 0, c = 0;
        byte[] data = new byte[1024];
        try {
            System.out.println("Downloading file '" + fileName + "' has been Started!");
            while ((t = input.read(data)) >= 0) {
                writer.write(data, 0, t);
            }
            System.out.println("Downloading " + fileName + " finished successfully!\n");
        } catch (Exception ex) {
            System.out.println("IO: downloader in while a problem accured");
        }
    }
}

class Sender {

    private Socket sendSocket;
    private String filename;
    private Integer port;
    private String ip;
    private FileInputStream reader;
    private DataOutputStream sender;

    public Sender(String ip, String port, String fileName) {
        this.filename = fileName;
        this.port = new Integer(port);
        this.ip = ip;
        try {
            sendSocket = new Socket(ip, this.port);
            sender = new DataOutputStream(sendSocket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IO: newing socketOututStream has a problem in Sender class!");
        }

        try {
            reader = new FileInputStream(this.filename);

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound: Newing fileInputStream has a problem in Sender class!");
        }
        Send();
    }

    public void Send() {
        try {
            int cnt = 0, c = 0;
            byte[] data = new byte[1024];
            while ((cnt = reader.read(data)) >= 0) {
                sender.write(data, 0, cnt);
            }
            sender.close();
            System.out.println("Sending File: " + filename + " To " + sendSocket.getInetAddress() + " finished successfully!\n");
        } catch (IOException ex) {
            System.out.println("IO: Reading/Sending data has a problem in Sender class!");
        }

    }
}
