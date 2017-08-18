package me.alikomi.endminecraft;

import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.scan.ScanBug;
import me.alikomi.endminecraft.tasks.scan.ScanInfo;
import me.alikomi.endminecraft.utils.Menu;
import me.alikomi.endminecraft.utils.Util;

import java.io.IOException;
import java.util.*;

public class Main extends Util {

    public static BugData bugData;
    public static InfoData infoData;

    private static String ip;
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
            log("请输入端口");
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
            log("漏洞检测结果： ", bugData.toString());
        }
        Menu menu = new Menu(sc, ip, port);

        while (true) {
            log("请输入攻击方式：", "1 : MOTD攻击", "2 : 分布式假人压测", "3 : 单ipTAB压测", "========================");
            switch (sc.nextInt()) {
                case 1: {
                    menu._1();
                    break;
                }
                case 2: {
                    menu._2();
                    break;
                }
                case 3: {
                    menu._3();
                    break;
                }
            }
        }
    }
}
