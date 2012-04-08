/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package peer;


import java.net.*;
import java.io.*;
/**
 *
 * @author alijahanshahi
 */
public class Main {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws IOException {

        Integer serverPort = new Integer(args[0]);
        String ip="localhost";//args[0];
        ServerSocket ListenSocket = null;
        try {
            ListenSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening at port: "+ serverPort );
        } catch (IOException ex) {
            System.out.println("ServerSocket problem");
        }

        while(true){
            Socket s=null;
            try{
                 s=ListenSocket.accept();
                 PeerHandler h=new PeerHandler(s,ip);
            }
            catch(Exception e){
                System.out.println("Socket problem");
            }
        }

    }

}


class PeerHandler extends Thread{
    Socket skt;
    String ip;
    DataInputStream input;
    DataOutputStream output;
    FileInputStream reader;
    FileOutputStream writer;
    public PeerHandler(Socket s,String ip) {
        try {
            skt = s;
            this.ip=ip;
            input = new DataInputStream(skt.getInputStream());
            output = new DataOutputStream(skt.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOstream making problem!");
        }
        this.start();
    }
    @Override
    public void run(){
        try {
            int b = 0,c = 0;
            byte[] data = new byte[1024];
            char[] request= new char[1024];
            if ((b = input.read(data)) >= 0) {
                for( int i=0;i<1024;i++){
                    request[c]=(char)data[i];
                    c++;
                }
            }
            else{
                System.out.println("file finished");
            }
            String massage = new String(request);
            String[] parameters= massage.split(";");
            for (int i=0;i<parameters.length;i++)
                System.out.println(parameters[i]);
            System.out.println("massage from: " +skt.getInetAddress()+" massage: "+parameters[0]);
            //response recieved from server
            // 0-> no file found
            // 1-> parameters are: ip-port-filename
            if (parameters[0].startsWith("response")){
                if(parameters[1].startsWith("1")){
                    //sending download request 'give-me;filename' to peer
                    //and downloading file from connection
                    int r=0;
                    Integer peerPort=new Integer(parameters[3]);
                    byte[] send=new byte[1024];
                    String df=new String("give-me;");
                    Socket download=new Socket(parameters[2],peerPort );
                    DataOutputStream requestForDownload = new DataOutputStream(download.getOutputStream());
                    df=df.concat(parameters[4]);
                    for(int i=0;i<df.length();i++)
                        send[i]=(byte)df.charAt(i);
                    requestForDownload.write(send);
                    System.out.println("Download request sent to : " + parameters[2] + " for downloading file: "+parameters[4]);
                    Downloader downloader= new Downloader(skt, parameters[4]);
                    return;
                }
                else{
                    System.out.println("Requested file was not found!");
                }
            }
            //here peer must send requested file
            else if (parameters[0].startsWith("give-me")){
                Sender sender = new Sender(skt, parameters[1]);
            }
            else{
                System.out.println("Wrong massage format!");
            }
        }
        catch (IOException ex) {
            System.out.println("IOstream reading problem!");
        }
        
    }

}


class Downloader {
    Socket skt;
    FileOutputStream writer;
    DataInputStream input;
    String fileName;
    public Downloader(Socket s,String fileName) {
        skt = s;
        this.fileName = fileName;
        try {
            input = new DataInputStream(skt.getInputStream());
            writer = new FileOutputStream("2.txt");
        }
        catch (IOException ex) {
            System.out.println("IO: downloader has problem");
        }
        run();
    }
    private void run(){

            int t = 0,c = 0;
            byte[] data= new byte[1024];
            try {
                while ((t = input.read(data)) >= 0) {
                    writer.write(data);
                    System.out.println(c);
                    c++;
                }
                System.out.println("Downloading "+fileName+" from "+skt.getInetAddress()+" finishish successfully!");
            }
            catch (IOException ex) {
                System.out.println("IO: downloader in while a problem accured");
            }     
    }
}


class Sender extends Thread{
    Socket sendSocket;
    private String filename;
    private FileInputStream reader;
    private DataOutputStream sender;
    Socket s;
    public Sender(Socket s, String fileName) {
        sendSocket=s;
        this.filename = fileName;
        try {
            sender = new DataOutputStream(sendSocket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IO: Getting socketOututStream has a problem in Sender class!");
        }

        try {
            reader = new FileInputStream(this.filename);
            
        } catch (FileNotFoundException ex) {
           System.out.println("FileNotFound: Newing fileInputStream has a problem in Sender class!");
        }
        this.start();
    }
    @Override
    public void run(){
        try {
            int cnt = 0,c=0;
            byte[] data = new byte[1024];
            while ((cnt = reader.read(data)) >= 0) {
                sender.write(data);
                System.out.println(c);
                c++;
            }
            System.out.println("Sending File: "+filename+" To "+sendSocket.getInetAddress() + " finished successfully!");
        }
        catch (IOException ex) {
            System.out.println("IO: Reading/Sending data has a problem in Sender class!");
        }
        
    }
}