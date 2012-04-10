
/**
 *
 * @author mohammadt
 */
package server;

import java.util.*;

public class File {
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
