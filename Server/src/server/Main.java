/**
 *
 * @author mohammadt
 */
package server;

import java.net.*;
import java.io.*;
import java.util.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //input port from user
        System.out.println("Please specify a port:");
        Scanner sc = new Scanner(System.in);
        Integer serverPort = sc.nextInt();
        String ip = "127.0.0.1";//args[0];
        Server server = new Server(ip, serverPort);
        MyThread m= new MyThread(server);
        m.start();
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
    @Override
    public void run() {
        try {

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

            request;name;filename//from peer to server

            response;Count;filename;name;ip;port;name;ip;port;....//from server to peer

            response;0;filename//from server to peer

            list;name
            
            list;filename1;filename2;...
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
                sendPeersHaveFile(param.get(0),param.get(1));
            } else if(requestName.startsWith("list")){
                sendList(param.get(0));

            } else {
                System.out.println("Bad Request!");

            }

        } catch (IOException ex) {
            System.out.println("IOstream reading problem!");
        }
    }
    private void sendData(String ip, int port, String data) {
        Socket s = null;
        DataOutputStream out = null;
        try {
            System.out.println("sending to "+ip+":"+port+" data= "+data);
            s = new Socket(ip, port);
            PrintWriter a = new PrintWriter(s.getOutputStream(), true);
            a.println(data);
            s.close();
        } catch (IOException ex) {
            System.out.println("Sending CONNECT message  in ServerPeerInteraction class/ ConnectToServer has a problem! \n"+ex.getMessage());
        }
    }

    private void register(String ip, String port, List<String> files) {
        //todo port and ip
        Client client = new Client(ip, port, 0,files);
        server.addClient(client);
        for (int i = 0; i < files.size(); i++) {

            File F = new File(files.get(i));
            if(server.files.contains(F))
            {
                F=server.getFile(files.get(i));
            }
            else
            {

                server.addFile(F);
            }
            F.addClient(client);
        }
        String data = "hi;" + client.getName();
        sendData(client.getIp(), Integer.parseInt(client.getPort()), data);
    }

    private void refresh(String name, int dLCount, List<String> fileNames) {
        Client c= server.getClient(name);
       // if(c!=null){

            c.setIsAlive(true);
            c.setDownloadCount(dLCount);
            server.refreshFiles(c, fileNames);
        //}

    }

    private void remove(String name) {
        server.removeClient(name);

    }

    private void sendPeersHaveFile(String name,String fileName) {
        String data,test="";
        if (!server.files.contains(new File(fileName))) {
            data = "response;0;"+fileName;
        } else {
            int index = server.files.indexOf(new File(fileName));
            File f = server.files.get(index);
            List<Client> l = f.getClients();
            data = "response;" + l.size() + ";" + fileName;
            for (int i = 0; i < l.size(); i++) {
                data = data + ";" + l.get(i).getName() +";"+ l.get(i).getIp()+";" + l.get(i).getPort();
            }
            for (int i = 0; i < l.size(); i++) {
                test = test + ";" + l.get(i).getName() +";"+ l.get(i).getDownloadCount();
            }
            System.out.println("---------------------"+test);
        }
        InetSocketAddress remote = (InetSocketAddress) (skt.getRemoteSocketAddress());
//        System.out.println("name"+name);
        sendData(server.getClient(name).getIp(), Integer.parseInt(server.getClient(name).getPort()), data);
    }
    private void sendList(String name)
    {
        String data="list";
        for(int i=0;i<server.files.size();i++)
            if(!server.files.get(i).clients.contains(server.getClient(name)) )
                if(server.files.get(i).clients.size()>0)
                    data=data+";"+server.files.get(i).getName();
        sendData(server.getClient(name).getIp(), Integer.parseInt(server.getClient(name).getPort()), data);

    }

    

    
}
class MyThread extends Thread {
    Server server;

    public MyThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                sleep((long)11000);
                server.checkAlives();
            } catch (InterruptedException e) {
            }
        }
    }

    
}
