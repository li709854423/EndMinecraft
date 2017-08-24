package me.alikomi.endminecraft.tasks.attack;

import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.packetlib.Client;

public class TabWithOneIp_17_8 {
    public static void qwq(Client mc) {
        mc.getSession().send(new ClientTabCompletePacket("/"));
    }
}
