import java.util.ArrayList;

public abstract class LoadBalancingStrategy {
    public ArrayList<BackendServer> servers;

    LoadBalancingStrategy(ArrayList<BackendServer> servers) {
        this.servers = servers;
    }
    public BackendServer getBackendServer(String key) {
        return servers.get(0);  // default
    }
}
