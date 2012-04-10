/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
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
        System.out.println("Enter your current IP:");
        Scanner n = new Scanner(System.in);
        String myIP = n.next();
        System.out.println("your IP is: " + myIP);
        //generate a random port for this peer
        int myPort = randomProt();

        //getting server IP and port
        System.out.println("Enter Server IP:");
        Scanner m = new Scanner(System.in);
        String serverIP = m.next();


        System.out.println("Enter Server PORT:");
        Scanner o = new Scanner(System.in);
        String serverPort = o.next();

        //incoming socket -> for message come in and responsing them
        //outgointToServer -> for server-peer interactions
//        System.out.println("Connecting to Server : " + serverIP + ":" + serverPort);
//        ServerPeerInteractions serverPeer = new ServerPeerInteractions(serverIP, serverPort, myIP, myPort);
        InputRequestHandler requestHandler = new InputRequestHandler(myIP, myPort);
    }

    private static int randomProt() {

        return (1000 + (int) (Math.random() * 60000));
    }
}

class ServerPeerInteractions extends Thread {

    private Socket skt;
    private String serverIP, myIP, directory;
    private Integer serverPort, myPort;
    private DataOutputStream out;
    private List<String> files;

    public ServerPeerInteractions(String sIP, String sPort, String myIP, int myPort) {
        
        serverIP = sIP;
        this.myIP = myIP;
        serverPort = new Integer(sPort);
        this.myPort = new Integer(myPort);

        //getting shared folder on NETWORK
        System.out.println("Enter address of shared folder:");

        Scanner s = new Scanner(System.in);
        directory = s.next();
        try {
            skt = new Socket(serverIP, serverPort);
        }
        catch (UnknownHostException ex) {
            System.out.println("Newing socket for connecting to Server : " + serverIP + ":" + serverPort + " has a problem");
        }
        catch (IOException ex) {
            System.out.println("Newing socket for connecting to Server : " + serverIP + ":" + serverPort + " has a problem");
        }
        try {
            out = new DataOutputStream(skt.getOutputStream());
        }
        catch (IOException ex) {
            System.out.println("Newing dataOutputStream for connecting to Server : " + serverIP + ":" + serverPort + " has a problem");
        }
        //getting list of files
        files = new LinkedList<String>();
        sharedDirectoryFiles();
        connectToServer();
        this.start();
    }

    private void connectToServer() {
        String connectmessage = new String("connect;" + myIP + ";" + myPort + ";");
        for (int i = 0; i < files.size() - 1; i++) {
            connectmessage.concat(files.get(i) + ";");
        }
        byte[] temp = new byte[connectmessage.length()];
        connectmessage.concat(files.get(files.size() - 1));
        for (int i=0 ; i <connectmessage.length() ; i++)
            temp[i] = (byte) connectmessage.charAt(i);
            try {
                PrintWriter a = new PrintWriter(skt.getOutputStream(), true);
                a.println(connectmessage);
            } catch (IOException ex) {
                System.out.println("Sending CONNECT message  in ServerPeerInteraction class/ ConnectToServer has a problem!");
            }

    }

    private void sharedDirectoryFiles() {
        File firstiew = new File(directory);
        File[] list = firstiew.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                files.add(list[i].getName());
            }
        }
    }

    @Override
    public void run() {
    }
}

class InputRequestHandler {

    private ServerSocket ListenSocket;
    private int serverPort;
    private String ip;
    private Socket incoming;
    private Socket outgoingToServer;

    public InputRequestHandler(String i, int p) {
        serverPort = p;
        ip = i;
        ListenSocket = null;
        incoming = null;
        outgoingToServer = null;
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
}

class PeerHandler extends Thread {

    Socket skt;
    String ip;
    Integer DownloadPort;
    DataInputStream input;
    DataOutputStream output;
    FileInputStream reader;
    FileOutputStream writer;

    public PeerHandler(Socket s, String ip, int port) {
        try {
            skt = s;
            this.ip = ip;
            DownloadPort = new Integer(port + 1);
            input = new DataInputStream(skt.getInputStream());
            output = new DataOutputStream(skt.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOstream making problem!");
        }
        this.start();
    }

    private void sendRequestTopeer(String[] parameters) {
        //Here peer sends a give-me request to another peer with Its IP and listen port for
        String Port = parameters[3];
        String ip = parameters[2];
        String fileName = parameters[4];
        Integer peerPort = new Integer(Port);
        System.out.println("port:" + peerPort+" ip :"+ip);
        Socket download = null;
//        DataOutputStream requestForDownload = null;
        PrintWriter requestForDownload = null;
        try {
            download = new Socket(ip, peerPort);
        } catch (UnknownHostException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        } catch (IOException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        try {
//            requestForDownload = new DataOutputStream(download.getOutputStream());
            requestForDownload = new PrintWriter(download.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("newing DataOutputStream for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        String df = new String("give-me;");
        df = df.concat(this.ip + ";" + DownloadPort.toString() + ";");
        df = df.concat(fileName);
        try {
            System.out.println("sent message: "+df + " to "+ip+ " "+ peerPort);
            requestForDownload.println(df);
        } catch (Exception ex) {
            System.out.println("Sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        System.out.println("Download request sent to : " + ip + " for downloading file: " + fileName);
        Downloader downloader = new Downloader(fileName, DownloadPort);
    }

    @Override
    public void run() {
        try {
            
            int b = 0, c = 0;
            byte[] data = new byte[1024];
            char[] request = new char[1024];
            String message = new String();
            BufferedReader d = new BufferedReader(new InputStreamReader(input));
            message = d.readLine();
            String[] parameters = message.split(";");
            System.out.println("message from: " + skt.getInetAddress() + " message: "+ message);
            //response recieved from server
            // 0-> no file found
            // 1-> parameters are: ip-port-filename
            if (parameters[0].startsWith("response")) {
                if (parameters[1].startsWith("1")) {
                    //sending download request 'give-me;filename' to peer
                    //and downloading file from connection
                    sendRequestTopeer(parameters);
                } else {
                    System.out.println("Requested file was not found!");
                }
            }
            //here peer must send requested file sender class sends requested file
            else if (parameters[0].startsWith("give-me")) {
                Sender sender = new Sender(parameters[1], parameters[2], parameters[3]);
            }
            //prints search result
            else if (parameters[0].startsWith("Searchresult")){
                //todo
                //depends on recived data structure print them;
            }
            //wrong forat message recieved
            else {
                System.out.println("Wrong message format!");
            }
        }

        catch (IOException ex) {
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

/*
 * this class sendes given file to given ip and port
 */

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
