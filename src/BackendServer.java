public class BackendServer {
    private String host;
    private int port;
    private boolean isHealthy;

    public BackendServer(String host, int port, boolean isHealthy) {
        this.host = host;
        this.port = port;
        this.isHealthy = isHealthy;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHealthy(boolean healthy) {
        isHealthy = healthy;
    }
}
