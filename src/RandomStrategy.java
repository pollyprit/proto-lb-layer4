import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy extends LoadBalancingStrategy {
    private Random random = new Random();

    RandomStrategy(ArrayList<BackendServer> servers) {
        super(servers);
    }

    @Override
    public BackendServer getBackendServer(String key) {
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
