package ch.jamiete.mcping;


import java.net.Proxy;

/**
 * Storage class for {@link MinecraftPing} options.
 */
public class MinecraftPingOptions {

    private String hostname;
    private int port = 25565;
    private int timeout = 4200;
    private String charset = "UTF-8";
    private Proxy proxy = null;

    public MinecraftPingOptions setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public MinecraftPingOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public MinecraftPingOptions setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public MinecraftPingOptions setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public MinecraftPingOptions setProxy (Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public String getCharset() {
        return this.charset;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

}
