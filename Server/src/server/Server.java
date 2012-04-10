/**
 *
 * @author mohammadt
 */
package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    List<Client> clients;
    List<File> files;
    int port;
    String ip;
    ServerSocket socket;

    public List<Client> getClients() {
        return clients;
    }

    public List<server.File> getFiles() {
        return files;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void addClient(Client client) {
        if (!this.clients.contains(client)) {
            this.clients.add(client);
        }
    }

    public void addFile(File file) {
        if (!this.files.contains(file)) {
            this.files.add(file);
        }
    }

    public Client getClient(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getName().equals(name)) {
                return clients.get(i);
            }
        }
        return null;

    }

    public File getFile(String name) {
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().equals(name)) {
                return files.get(i);
            }
        }
        return null;
    }

    public void removeClient(String name) {
        for (int i = 0; i < files.size(); i++) {
            File c = files.get(i);
            if (c.getClient(name) != null) {
                c.clients.remove(c.getClient(name));

            }
        }
        clients.remove(getClient(name));
    }
    public void checkAlives(){
        for(int i=0;i<clients.size();i++)
            if(clients.get(i).isAlive){
                clients.get(i).isAlive=false;
//                System.out.println("false kard......");
            }else{
                System.out.println(clients.get(i).getName()+" removed");
                 removeClient(clients.get(i).getName());
            }

    }
    /*
     * this function must call every x seconds to  remove disconected clients and theirs files
     */

    public void refreshFiles(Client client, List<String> files) {
        List<String> cfiles = client.getFiles();
        for (int i = 0; i < cfiles.size(); i++) {
            if (! files.contains(cfiles.get(i))) {
                //remove that file
                
                    File f = this.getFile(cfiles.get(i));
                    if (f.getClient(client.getName()) != null) {
                        f.clients.remove(client);

                    }
                
                
                cfiles.remove(i);

            }

        }
        for (int i = 0; i < files.size(); i++) {
            if (!cfiles.contains(files.get(i))) {



                    File F = new File(files.get(i));
                    if (this.files.contains(F)) {
                        F = this.getFile(files.get(i));
                    } else {

                        this.addFile(F);
                    }
                    F.addClient(client);
                    cfiles.add(files.get(i));
            }
        }

    }

    public Server(String ip, int port) {
        files = new ArrayList<File>();
        clients = new ArrayList<Client>();
        this.ip = ip;
        this.port = port;
        try {
            socket = new ServerSocket(port);
            System.out.println("Server is listening at port: " + port);
        } catch (IOException ex) {
            System.out.println("ServerSocket problem");
        }
    }
}
