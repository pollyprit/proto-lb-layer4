import java.util.ArrayList;

public class RoundRobinStrategy extends LoadBalancingStrategy {
    private int index = 0;

    RoundRobinStrategy(ArrayList<BackendServer> servers) {
        super(servers);
    }

    public BackendServer getBackendServer(String key) {
        BackendServer server = servers.get(index);
        index = (index + 1) % servers.size();
        return server;
    }
}
