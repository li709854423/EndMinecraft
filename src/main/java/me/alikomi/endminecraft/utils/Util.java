package me.alikomi.endminecraft.utils;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;

public class Util {
    public  static void log(Object msg) {
        System.out.println(msg);
    }

    public static Map<String, Proxy.Type> getHttpIp(int maxAttack) {
        return GetHttpIp.getHttpIp(maxAttack);
    }

    public static Map<String, Proxy.Type> getFileIp(int maxAttack) throws IOException {
        return GetFileIp.getFileIp(maxAttack);
    }

    public static boolean testProxy(String ip, int port, Proxy proxy) {
        try {
            return TestProxy.test(ip,port,proxy);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

}
