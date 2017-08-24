package me.alikomi.endminecraft.tasks.scan;

import me.alikomi.endminecraft.Main;
import me.alikomi.endminecraft.tasks.attack.DistributedBotAttack;
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

public class ScanBug_19 extends Util {
    private static boolean reTab = false;

    public static void scanTAB(String ip, int port) {
        MinecraftProtocol mc = new MinecraftProtocol(DistributedBotAttack.getRandomString(6));
        final Client client = new Client(ip, port, mc, new TcpSessionFactory());
        log("正在扫描TAB漏洞，请稍后", "正在连接服务器");
        client.getSession().addListener(new SessionListener() {

            public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                if (packetReceivedEvent.getPacket() instanceof ServerChatPacket) {
                    log(((ServerChatPacket) packetReceivedEvent.getPacket()).getMessage());
                }

                if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {

                    client.getSession().send(new ClientChatPacket("/register qwnmopzx123 qwnmopzx123"));
                    client.getSession().send(new ClientChatPacket("/login qwnmopzx123"));

                    client.getSession().send(new ClientTabCompletePacket("/",false));

                    log("TAB包发送成功！正在等待返回");
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (reTab) {
                            log("存在TAB漏洞！");
                        } else {
                            log("TAB漏洞不存在!");
                        }
                        client.getSession().disconnect("TAB检测完毕，断开连接");
                    }).start();
                    log("服务器连接成功，正在扫描");
                }

                if (packetReceivedEvent.getPacket() instanceof ServerTabCompletePacket) {
                    reTab = true;
                    log("收到服务器返回TAB包, 长度为：" + ((ServerTabCompletePacket) packetReceivedEvent.getPacket()).getMatches().length);
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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
