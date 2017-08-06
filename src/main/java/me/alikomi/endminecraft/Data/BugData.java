package me.alikomi.endminecraft.Data;

public class BugData {
    private static String ip;
    private static int port;
    private static boolean motd = false;

    public BugData(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean getMotdBug() {
        return motd;
    }
    public void setMotdBug(boolean info) {
        motd = info;
    }


    public String toString() {

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("ip: ").append(ip);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("port: ").append(port);
        stringBuilder.append(System.getProperty("line.separator", "\n"));
        stringBuilder.append("motdBug: ").append(motd);
        stringBuilder.append(System.getProperty("line.separator", "\n"));

        return stringBuilder.toString();
    }

}
