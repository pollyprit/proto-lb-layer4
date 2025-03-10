import java.util.ArrayList;

public class HashBasedStrategy extends LoadBalancingStrategy {
    private int index = 0;

    HashBasedStrategy(ArrayList<BackendServer> servers) {
        super(servers);
    }

    @Override
    public BackendServer getBackendServer(String key) {
        long hash = 7;

        for (int i = 0; i < key.length(); ++i)
            hash = hash * 31 + key.charAt(i);

        long index = hash % servers.size();
        return servers.get((int)index);
    }
}
