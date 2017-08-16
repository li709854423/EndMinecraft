package me.alikomi.endminecraft.utils;

import java.io.IOException;
import java.net.*;

class TestProxy extends Util {
    static boolean test(String ip, int port, Proxy proxy) throws IOException {
        Socket socket = new Socket(proxy);
        try {
            socket.connect(new InetSocketAddress(ip,port),3000);
            return true;

        } catch (Exception e) {
            log("代理： " + proxy.toString() + " 不可用");
            return false;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            if (socket !=null) socket.close();
        }
    }
}
