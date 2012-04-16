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
            System.out.println("IO: newing socketOututStream has a problem in Sender class!"+ex.getMessage());
        }

        try {
            reader = new FileInputStream(this.filename);

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound: Newing fileInputStream has a problem in Sender class!"+ex.getMessage());
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
        try {
            sendSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
