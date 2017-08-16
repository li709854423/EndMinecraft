package me.alikomi.endminecraft.tasks.scan;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import me.alikomi.endminecraft.Main;
import me.alikomi.endminecraft.utils.GetMotdData;
import me.alikomi.endminecraft.utils.Util;


public class ScanInfo extends Util {
    public static void ScanMotdInfo(String ip, int port) {
        String data = null;

        data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(ip).setPort(port));//获取MOTD的json。

        log(data);
        GetMotdData motdData = new GetMotdData(data);
        log("版本： " + motdData.getVersion());
        log("在线人数： " + motdData.getOnlinePlayers());
        log("最大人数： " + motdData.getMaxPlayers());
        Main.infoData.setServerVersion(motdData.getVersion());
        Main.infoData.setOnlinePlayer(motdData.getOnlinePlayers());
        Main.infoData.setMaxPlayer(motdData.getMaxPlayers());
    }

}
