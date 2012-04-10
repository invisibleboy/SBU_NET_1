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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {

    public static void main(String args[]) {// arguments supply message and hostname of destination

        Socket s = null;
        try {
            int serverPort = 18009;
            String ip = "localhost";
            String data = "Hello, How are you?";
            System.out.println("file" + data);
            s = new Socket(ip, serverPort);
//            DataOutputStream out = new DataOutputStream(s.getOutputStream());
//            FileInputStream a = new FileInputStream("/Users/alijahanshahi/1.avi");
            int r = 0, c = 0;
//                    byte[] send=new byte[1024];
            String df = new String("response;1;localhost;19106;1.mp4");
//                    for(int i=0;i<df.length();i++)
//                        send[i]=(byte)df.charAt(i);
//                    output.write(send);
            String connectMassage = new String("asadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsssasadasdsadsasasdsadasdsss");

            PrintWriter a = null;
            try {
                a = new PrintWriter(s.getOutputStream(),true);
            } catch (IOException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(df);
            a.println(df);
//                    int t=0;
//                    byte[] send = new byte[1024];
//                    while((t = a.read(send))>=0){
//                        out.write(send, 0 , t);
//                        for (int i=0; i<1024 ; i++)
//                        System.out.print((char)send[i]);
        }catch(Exception e){
            ;
        }


//        byte[] temp = new byte[df.length()];
//        for ( int i=0 ; i< df.length() ; i++)
//            temp[i] = (byte) df.charAt(i);
//        byte[] send = new byte[1024];
//        int t = temp.length/1024;
//        int i2 = 0;
//        int byteNum = 1024;
//        int remainBytes = temp.length;
////        System.out.println(remainBytes);
//        while (t >= 0) {
//            int k = 0 ;
//            int l = remainBytes;
//            for (int i = i2*1024; i < (i2+1)*1024  ; i++) {
//                if(remainBytes<0)
//                    break;
//                send[k] = temp[i];
//                k++;
//                remainBytes--;
//                System.out.println(remainBytes);
//            }
//            System.out.println("---"+remainBytes);
//            if( remainBytes >= 1024){
//                byteNum=1024;
//            }
//            else{
//                byteNum = l;
//            }
//            try {
//
//                out.write(send, 0, byteNum );
//            } catch (IOException ex) {
//                   System.out.println("efefe"); ;
//            }
//            i2++;
//            t--;
//        }


        System.out.print("sent\n");
//    }
//    catch
//
//
//
//        (UnknownHostException e) {
//            System.out.println("Sock:" + e.getMessage());
//    }
//    catch
//
//
//
//        (EOFException e) {
//            System.out.println("EOF:" + e.getMessage());
//    }
//    catch
//
//
//
//        (IOException e) {
//            System.out.println("IO:" + e.getMessage());
//    }
//    finally
//
//    {
//        if (s != null) {
//            try {
//                s.close();
//            } catch (IOException e) {/*close failed*/
//
//            }
//        }
//    }
}

}
