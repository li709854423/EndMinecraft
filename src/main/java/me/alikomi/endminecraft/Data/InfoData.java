package me.alikomi.endminecraft.Data;

public class InfoData {

    private static String ip;
    private static int port;
    private static String serverVersion;
    private static int maxPlayer;
    private static int onlinePlayer;


    public InfoData(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getOnlinePlayer() {
        return onlinePlayer;
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("ip: ").append(ip);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("port: ").append(port);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("serverVersion: ").append(serverVersion);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("maxPlayer: ").append(maxPlayer);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("onlinePlayer: ").append(onlinePlayer);
        stringBuilder.append(System.getProperty("line.separator", "\n"));


        return stringBuilder.toString();
    }
}
