
public class Main {
    public static void main(String[] args) throws Exception {
        LoadBalancer loadBalancer = new LoadBalancer(LoadBalancingStrategyEnum.ROUND_ROBIN);

        loadBalancer.start();
    }
}