

package server;
import java.util.*;
/**
 *
 * @author mohammadt
 */
public class Client implements Comparable<Client> {

    String name;
    String ip;
    String port;
    /*
     * for refresh, if one of these file changes must choose a good decision
     */
    List<String> files;
    int downloadCount;
    static int InstanceCount = 0;
    boolean isAlive;

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    

    public Client(String ip, String port, int downloadCount, List<String> files) {
        this.isAlive = true;
        this.ip = ip;
        this.port = port;
        this.downloadCount = downloadCount;
        this.files=files;
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

    public List<String> getFiles() {
        return files;
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