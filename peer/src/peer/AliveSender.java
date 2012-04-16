/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alijahanshahi
 */
public class AliveSender extends Thread {

    LinkedList<String> files;
    Socket socket;
    String Path;
    static int time=1000;
    PurePeer peer;
    Server server;
    public AliveSender(PurePeer p, Server s) {
        peer = p;
        server = s;
        this.files = new LinkedList<String>();
    }

    @Override
    public void run() {
        File directory = new File(PurePeer.SharedFolder);
        while (true) {
            try {
                sleep((long) 10000);
            }
            catch (InterruptedException ex) {
                System.out.println("CheckFilesInDirectory class: updating file in directory thread cant sleep!");
            }
            String aliveMessage = new String();
            aliveMessage = aliveMessage.concat("alive;"+PurePeer.name+";"+PurePeer.load);
            files.clear();
            File[] list = directory.listFiles();
            for (int i = 0; i < list.length; i++)
                if (list[i].isFile())
                    files.add(list[i].getName());
            for(int i=0 ;i<files.size() ; i++)
                aliveMessage = aliveMessage.concat(";"+files.get(i));
            try {
                socket = new Socket(server.ip, server.port);
                PrintWriter p = new PrintWriter(socket.getOutputStream(), true);
                p.println(aliveMessage);
            } catch (IOException ex) {
                System.out.println("CheckFilesInDirectory class: Sending ALIVEMESSAGE for server failed!");
            }
        }
    }

    public void print() {
        System.out.println("new round!");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i));
        }
        System.out.println("round finished \n\n\n!");
    }
}
