/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clinet;

/**
 *
 * @author alijahanshahi
**/
// TCPClient.java
// A client program implementing TCP socket
import java.net.*;
import java.io.*;
import java.sql.Struct;
import java.util.Map;

public class TCPClient {
    
	public static void main (String args[])
	{// arguments supply message and hostname of destination

		Socket s = null;
		try{
                    int serverPort = 6666;
                    String ip = "localhost";
                    String data = "Hello, How are you?";
                    System.out.println("file"+data);
                    s = new Socket(ip, serverPort);
//		  DataInputStream input = new DataInputStream(s.getInputStream());
                    DataOutputStream output = new DataOutputStream( s.getOutputStream());
                    FileInputStream a=new FileInputStream("/Users/alijahanshahi/1.avi");
                    int r=0,c=0;
                    byte[] send=new byte[1024];
                    String df=new String("response;1;192.168.23.201;8888;1.avi");
                    for(int i=0;i<df.length();i++)
                        send[i]=(byte)df.charAt(i);
                    output.write(send);
//                    while((r=a.read(send))>=0){
//                        output.write(send);
//                        System.out.println(send);
//                        c++;
//                    }
                    System.out.print("sent\n");
		}
		catch (UnknownHostException e){
			System.out.println("Sock:"+e.getMessage());}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage()); }
		catch (IOException e){
			System.out.println("IO:"+e.getMessage());}
		finally {
			  if(s!=null)
				  try {s.close();
				  }
				  catch (IOException e) {/*close failed*/}
	}
  }
}

