package me.alikomi.endminecraft;

import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.ScanInfo;
import me.alikomi.endminecraft.utils.Util;

import java.util.Scanner;

public class Main extends Util {

    public static BugData bugData;
    public static InfoData infoData;

    public static String ip;
    public static int port = 25565;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        log("请输入ip地址");
        ip = sc.next();
        log("请输入端口");
        port = sc.nextInt();
        infoData = new InfoData(ip, port);
        log("是否开始进服前漏洞探测y/n");
        if ("y".equalsIgnoreCase(sc.next())) {
            ScanInfo.ScanMotdInfo(ip,port);
            /**
             bugData = new BugData(ip, port);
            ScanBug.scanMOTD(ip, port);
            ScanBug.scanTAB(ip,port);
            log("漏洞检测结果： ");
            log(bugData.toString());
             */
        }
    }

}
