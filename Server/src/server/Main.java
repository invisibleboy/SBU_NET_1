/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohammadt
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //input port from user
        Scanner sc = new Scanner(System.in);
        Integer serverPort = sc.nextInt();

        String ip = "127.0.0.1";//args[0];

        Server server = new Server(ip, serverPort);


        while (true) {
            Socket s = null;
            try {
                s = server.getSocket().accept();
                ServerHandler h = new ServerHandler(server, s);
                h.start();
            } catch (Exception e) {
                System.out.println("Socket problem:\n" + e.getMessage().toString());
            }
        }

    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                check();
                sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    private void check() {
        ;
    }
}

class Server {

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

class File {

    String Name;
    List<Client> clients;

    public Client getClient(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getName().equals(name)) {
                return clients.get(i);
            }
        }
        return null;

    }

    public File(String Name) {
        this.Name = Name;
        clients=new ArrayList<Client>();
    }

    public boolean addClient(Client client) {
        if (!clients.contains(client)) {
            this.clients.add(client);
            return true;
        }
        return false;
    }
//todo remove 

    public List<Client> getClients() {

        Collections.sort(clients);
        return clients;
    }

    public String getName() {
        return Name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final server.File other = (server.File) obj;
        if ((this.Name == null) ? (other.Name != null) : !this.Name.equals(other.Name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.Name != null ? this.Name.hashCode() : 0);
        return hash;
    }
}

class Client implements Comparable<Client> {

    String name;
    String ip;
    String port;
    int downloadCount;
    static int InstanceCount = 0;
    boolean isAlive;

    public Client(String ip, String port, int downloadCount) {
        this.isAlive = true;
        this.ip = ip;
        this.port = port;
        this.downloadCount = downloadCount;
        InstanceCount++;
        name = "Connection" + InstanceCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public String getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public int compareTo(Client compareClient) {

        return downloadCount - compareClient.getDownloadCount();
    }
}

class ServerHandler extends Thread {

    Server server;
    Socket skt;
    DataInputStream input;
    DataOutputStream output;

    public ServerHandler(Server server, Socket s) {
        try {
            this.skt = s;
            this.server = server;
            input = new DataInputStream(skt.getInputStream());
            output = new DataOutputStream(skt.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOstream making problem!");
        }

    }

    private void sendData(String ip, int port, String data) {
        Socket s = null;
        DataOutputStream out = null;
        try {
            s = new Socket(ip, port);
            PrintWriter a = new PrintWriter(s.getOutputStream(), true);
            a.println(data);
        } catch (IOException ex) {
            System.out.println("Sending CONNECT message  in ServerPeerInteraction class/ ConnectToServer has a problem!");
        }
    }

    private void register(String ip, String port, List<String> files) {
        //todo port and ip
        Client client = new Client(ip, port, 0);
        for (int i = 0; i < files.size(); i++) {
            File F = new File(files.get(i));
            F.addClient(client);
            server.addFile(F);
        }
        String data = "hi;" + client.getName();
        sendData(client.getIp(), Integer.parseInt(client.getPort()), data);
    }

    private void refresh(String name, int dLCount, List<String> fileNames) {
        server.getClient(name).isAlive = true;
        server.getClient(name).setDownloadCount(dLCount);
        //todo refresh files list
    }

    private void remove(String name) {
        server.removeClient(name);

    }

    private void sendPeersHaveFile(String fileName) {
        String data;
        if (!server.files.contains(new File(fileName))) {
            data = "response;0";
        } else {
            int index = server.files.indexOf(new File(fileName));
            File f = server.files.get(index);
            List<Client> l = f.getClients();
            data = "response;" + l.size() + ";" + fileName;
            for (int i = 0; i < l.size(); i++) {
                data = data + ";" + l.get(i).getName() + l.get(i).getIp() + l.get(i).getPort();
            }
        }
        InetSocketAddress remote = (InetSocketAddress) (skt.getRemoteSocketAddress());
        sendData(remote.getAddress().toString(), remote.getPort(), data);
    }

    @Override
    public void run() {
        try {
//            int c = 0, cnt;
//            byte[] data = new byte[1024];
//            char[] request = new char[1024];
//            while ((cnt = input.read(data)) >= 0) {
//                for (int i = 0; i < cnt; i++) {
//                    request[c++] = (char) data[i];
//                }
//            }
            BufferedReader d = new BufferedReader(new InputStreamReader(input));
            String message = new String();
            message = d.readLine();
            System.out.println("packet received");

            String[] parameters = message.split(";");
            List<String> param = new ArrayList<String>();
            for (int i = 1; i < parameters.length; i++) {
                param.add(parameters[i]);
            }
            String requestName = parameters[0];
            System.out.println("massage from: " + skt.getInetAddress() + " massage: " + message);

            /*
             *
            hi;ip;port;file1;file2;file3;...//from peer to server

            hi;yourName //from server to peer

            alive;name;downloadingCount;files1;file2;...//from peer to server

            bye;name//from peer to server

            request;filename//from peer to server

            response;Count;filename;name;ip;port;name;ip;port;....//from server to peer

            response;0;filename//from server to peer

            //resume-pause



             *
             *
             */
            if (requestName.startsWith("hi")) {
                register(param.get(0), param.get(1), param.subList(2, param.size()));
            } else if (requestName.startsWith("alive")) {
                refresh(param.get(0), Integer.parseInt(param.get(1)), param.subList(2, param.size()));
            } else if (requestName.startsWith("bye")) {
                remove(param.get(0));
            } else if (requestName.startsWith("request")) {
                sendPeersHaveFile(param.get(0));
            } else {
                System.out.println("Bad Request!");

            }

        } catch (IOException ex) {
            System.out.println("IOstream reading problem!");
        }

    }
}
