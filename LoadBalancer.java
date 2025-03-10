import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoadBalancer {
    public static int LB_PORT = 9000;
    private ArrayList<BackendServer> servers = new ArrayList<>();
    private LoadBalancingStrategy strategy;

    LoadBalancer(LoadBalancingStrategyEnum strategy) {
        init();
        this.strategy = getStrategy(strategy);
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(LB_PORT), 0);
        server.createContext("/", new LoadBalancer.MyHandler(this));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("load balancer started at port: " + LB_PORT);
    }

    private void init() {
        // start these servers manually outside this process [nodejs api-server prototype]
        servers.add(new BackendServer("localhost", 8081, true));
        servers.add(new BackendServer("localhost", 8082, true));
        servers.add(new BackendServer("localhost", 8083, true));
        servers.add(new BackendServer("localhost", 8084, true));
        servers.add(new BackendServer("localhost", 8085, true));
    }

    BackendServer getBackendServer(String key) {
        return this.strategy.getBackendServer(key);
    }

    public LoadBalancingStrategy getStrategy(LoadBalancingStrategyEnum strategy) {
        switch (strategy) {
            case RANDOM:
                return new RandomStrategy(servers);
            case HASH_BASED:
                return new HashBasedStrategy(servers);
            default:
                return new RoundRobinStrategy(servers);
        }
    }

    static class MyHandler implements HttpHandler {
        private LoadBalancer loadBalancer;

        MyHandler(LoadBalancer loadBalancer) {
            this.loadBalancer = loadBalancer;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            // Select a backend server with LB strategy
            BackendServer server = loadBalancer.getBackendServer(t.getRemoteAddress().getHostName());
            String host = "http://" + server.getHost() + ":" + server.getPort();
            URL url = new URL(host);
            System.out.println("selected backend-server : " + host);

            // Call the API and get the response from backend server
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                responseBuffer.append(inputLine);
            in.close();

            // Send back the response to client
            String response = responseBuffer.toString();
            t.sendResponseHeaders(responseCode, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}