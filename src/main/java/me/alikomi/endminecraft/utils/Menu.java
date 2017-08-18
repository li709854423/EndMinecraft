package me.alikomi.endminecraft.utils;

import me.alikomi.endminecraft.tasks.attack.DistributedBotAttack;
import me.alikomi.endminecraft.tasks.attack.MotdAttack;
import me.alikomi.endminecraft.tasks.attack.TabWithOneIp;

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
        log("请输入攻击时间(单位：蛤)");//我就是这么暴力
        int time = sc.nextInt() * 1000;
        log("请输入线程数");
        int thread = sc.nextInt();
        MotdAttack attack = new MotdAttack(ip, port, time, thread);
        attack.startAttack();
    }

    public void _2() throws IOException, InterruptedException {
        log("分布式假人压测选择", "请选择是否开启TAB发包 y/n");
        boolean tabenable = false;
        if ("y".equalsIgnoreCase(sc.next())) tabenable = true;
        log("请输入攻击时长！(s)");
        long time = sc.nextLong();
        log("请输入最大攻击数");
        int maxAttack = sc.nextInt();
        log("请输入每次加入服务器间隔(ms)");
        int sleepTime = sc.nextInt();
        log("请输入方式：", "1.通过API获取", "2.通过本地获取");
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
            default: {
                ips = getHttpIp(maxAttack);
                break;
            }
        }
        DistributedBotAttack distributedBotAttack = new DistributedBotAttack(ip, port, time * 1000, sleepTime, ips, tabenable);
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
}
