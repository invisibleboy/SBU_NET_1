/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package peer;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author alijahanshahi
 */
public class PeerHandler extends Thread{
    Socket skt;
    String ip;
    int DownloadPort;
    DataInputStream input;
    PrintWriter requestForDownload;
    String name;
    PurePeer peer;
    Server server;
    public PeerHandler(Socket s, PurePeer peer , Server server) {
            skt = s;
            this.ip = peer.ip;
            DownloadPort = peer.port+1;
            this.peer = peer;
            this.server = server;
        try {
            input = new DataInputStream(skt.getInputStream());
        } catch (IOException ex) {
            System.out.println("IOstream making problem!");
        }
    }

    private void sendRequestTopeer(String[] parameters) {
        //Here peer sends a give-me request to another peer with Its IP and listen port for
        //response;Count;filename;name;ip;port;name;ip;port;....//from server to peer
        System.out.println("List of peers that have '"+parameters[2]+"' are:");

        for ( int i=3 ;i<parameters.length;i= i+3)
            System.out.println("peerName is: "+parameters[i]+" IP: "+parameters[i+1]+" Port:"+parameters[i+2]);
        String Port = parameters[5];
        String ipAdr = parameters[4];
        String fileName = parameters[2];
        Integer peerPort = new Integer(Port);
        System.out.println("port:" + peerPort + " ip :" + ipAdr);
        Socket download = null;
        try {
            download = new Socket(ipAdr, peerPort);
        } catch (UnknownHostException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        } catch (IOException ex) {
            System.out.println("newing socket for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        try {
            requestForDownload = new PrintWriter(download.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("newing DataOutputStream for sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        String df = "give-me;";
        df = df.concat(this.ip + ";" + DownloadPort + ";");
        df = df.concat(fileName);
        try {
            System.out.println("sent message: " + df + " to " + ipAdr + " " + peerPort);
            requestForDownload.println(df);
        } catch (Exception ex) {
            System.out.println("Sending give-me request to peer has a problem in peerhandler class : sendrequesttopeer function!");
        }
        System.out.println("Download request sent to : " + ipAdr + " for downloading file: " + fileName);
        try {
            ServerSocket s = new ServerSocket(DownloadPort);
            Socket downloadSocket;
            downloadSocket = s.accept();
            Downloader downloader = new Downloader(downloadSocket, fileName);
            downloader.start();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(PeerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String message = new String();
            BufferedReader d = new BufferedReader(new InputStreamReader(input));
            message = d.readLine();
            String[] parameters = message.split(";");
            System.out.println("message from: " + skt.getInetAddress() + " message: " + message);
            //response;Count;filename;name;ip;port;name;ip;port;....//from server to peer -> file name and peers have that file
            //response;0;filename//from server to peer -> means file not found
            if (parameters[0].startsWith("response")) {
                if (!parameters[1].startsWith("0")) {
                    //sending download request 'give-me;filename' to peer
                    //and downloading file from connection
                    sendRequestTopeer(parameters);
                } else {
                    System.out.println("Requested file '"+parameters[2]+"'  was not found!");
                }
            }
            //here peer must send requested file sender class sends requested file
            else if (parameters[0].startsWith("give-me")) {
                PurePeer.load++;
                Sender sender = new Sender(parameters[1], parameters[2], PurePeer.SharedFolder+"/"+parameters[3]);
                PurePeer.load--;
                
            }
            //prints search result
            else if (parameters[0].startsWith("hi")) {
                PurePeer.name = parameters[1];
            }
            else if (parameters[0].startsWith("list")){
                System.out.println("list of files exist in Network:");
                for (int i=1 ; i< parameters.length ; i++)
                    System.out.println(parameters[i]);
            }
            //wrong forat message recieved
            else {
                System.out.println("Wrong message format!");
            }
        } catch (IOException ex) {
            System.out.println("IOstream reading problem!");
        }


    }

}
