/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package peer;

/**
 *
 * @author alijahanshahi
 */
public class PurePeer {
    String ip;
    int port;
    static int load;
    static String name = null;
    static String SharedFolder;
    public PurePeer(String i ,String n) {
        ip = i;
        port = (1000 + (int) (Math.random() * 60000));
        name = n;
    }
    public PurePeer(String i) {
        ip = i;
        port = (1000 + (int) (Math.random() * 60000));
    }
    public PurePeer(){
        port = (1000 + (int) (Math.random() * 60000));
    }
}
