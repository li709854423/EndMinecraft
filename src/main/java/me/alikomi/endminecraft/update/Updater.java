package me.alikomi.endminecraft.update;

import me.alikomi.endminecraft.utils.HttpReq;

public class Updater {
    private String version= "";

    public Updater(String ver) {
        version = ver;
    }
    public boolean check() {
        return HttpReq.sendPost("http://proxys.alikomi.me:9988/version.txt", "").equalsIgnoreCase(version);
    }
    public String getNewVersion() {
        return HttpReq.sendPost("http://proxys.alikomi.me:9988/download.txt", "");
    }
}
