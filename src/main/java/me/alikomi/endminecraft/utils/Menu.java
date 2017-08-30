package me.alikomi.endminecraft.utils;

import me.alikomi.endminecraft.Main;
import me.alikomi.endminecraft.tasks.attack.*;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Scanner;

public class Menu extends Util {
    private String ip;
    private Scanner sc;
    private int port;

    public Menu(Scanner sc, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.sc = sc;
    }

    public void _1() {
        log("MOTD攻击选择");
        if (Main.bugData != null) {
            if (Main.bugData.getMotdBug()) {
                log("恭喜！服务器有motdbug，秒蹦他吧");
            } else {
                log("本服务器没有motdbug哦！可能本功能会无效，请选择其他功能吧~");
            }
        }
        log("请输入攻击时间(单位：蛤)");//我就是这么暴力
        int time = sc.nextInt() * 1000;
        log("请输入线程数");
        int thread = sc.nextInt();
        MotdAttack attack = new MotdAttack(ip, port, time, thread);
        attack.startAttack();
    }

    public void _2() throws IOException, InterruptedException {
        log("分布式假人压测选择", "请输入攻击时长！(s)");
        long time = sc.nextLong();
        log("请输入最大攻击数");
        int maxAttack = sc.nextInt();
        log("请输入每次加入服务器间隔(ms)");
        int sleepTime = sc.nextInt();
        Map<String, Proxy.Type> ips = getProxy(maxAttack);
        DistributedBotAttack distributedBotAttack = new DistributedBotAttack(ip, port, time * 1000, sleepTime, ips);
        distributedBotAttack.startAttack();
    }

    public void _3() {
        log("请输入线程数");
        int thread = sc.nextInt();
        log("请输入攻击用户名");
        String username = sc.next();
        TabWithOneIp tabWithOneIp = new TabWithOneIp(ip, port, thread, username);
        tabWithOneIp.startAttack();
    }

    public void _4() throws IOException, InterruptedException {
        log("分布式MOTD压测选择", "请输入攻击时长(s)");
        long time = sc.nextLong();
        log("请输入最大攻击数");
        int maxAttack = sc.nextInt();
        log("请输入每次刷MOTD间隔(s)");
        int sleepTime = sc.nextInt();
        Map<String, Proxy.Type> ips = getProxy(maxAttack);
        DistributedMotdAttack distributedMotdAttack = new DistributedMotdAttack(ip, port, time * 1000, sleepTime * 1000, ips);
        distributedMotdAttack.startAttack();
    }

    private Map<String, Proxy.Type> getProxy(int maxAttack) throws IOException, InterruptedException {
        log("请输入代理ip列表获取方式：", "1.通过API获取", "2.通过本地获取", "3.官方获取");
        Map<String, Proxy.Type> ips;
        switch (sc.nextInt()) {
            case 1: {
                ips = getHttpIp(maxAttack);
                break;
            }
            case 2: {
                ips = getFileIp(maxAttack);
                break;
            }
            case 3: {
                ips = getALiKOMIIp(maxAttack, sc);
                break;
            }
            default: {
                ips = getALiKOMIIp(maxAttack, sc);
                break;
            }
        }
        return ips;
    }
}
