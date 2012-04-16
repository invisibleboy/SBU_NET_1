/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package peer;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


class Downloader extends Thread{

    Socket recieveSocket;
    FileOutputStream writer;
    DataInputStream input;
    String fileName;
    public Downloader(Socket s, String fileName) {
        this.fileName = fileName;
        recieveSocket = s;
        try {
            File f = new File(PurePeer.SharedFolder+"/Downloaded"+fileName);
            input = new DataInputStream(recieveSocket.getInputStream());
            writer = new FileOutputStream(f);

        } catch (IOException ex) {
            System.out.println("IO: downloader has problem "+ex.getMessage());
        }
    }

    @Override
    public  void run(){

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
        try {
            recieveSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
