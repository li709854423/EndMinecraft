package me.alikomi.endminecraft;

import me.alikomi.endminecraft.config.Configer;
import me.alikomi.endminecraft.data.BugData;
import me.alikomi.endminecraft.data.InfoData;
import me.alikomi.endminecraft.tasks.scan.ScanBug;
import me.alikomi.endminecraft.tasks.scan.ScanInfo;
import me.alikomi.endminecraft.update.Updater;
import me.alikomi.endminecraft.utils.Menu;
import me.alikomi.endminecraft.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends Util {

    public static Configer configer;

    private final static String version = "1.2.1";
    public static boolean leLe = false;

    public static String minecraftVersion = "";
    public static BugData bugData = null;
    public static InfoData infoData = null;

    private static String ip;
    public static int port = 25565;

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException, IOException {

        setProgram(args);
        checkVersion();
        getInfo();
        scanServer();
        scanBug();
        showMenu();
    }

    private static void setProgram(String[] args) throws InterruptedException {
        if (args == null || args.length==0) {
            return;
        }
        minecraftVersion = args[0];
        if (args.length >= 2) {
            configer = loadConfig(args[1]);
        }
    }

    private static Configer loadConfig(String filename) throws InterruptedException {
        File file = new File(filename);
        if (! file.exists()) {
            log("配置文件不存在，程序将于5秒后关闭");
            Thread.sleep(5000);
            System.exit(6);
        }
        if (! file.canRead()) {
            log("配置文件不可读，程序将于5秒后关闭");
            Thread.sleep(5000);
            System.exit(7);
        }
        return new Configer(file);

    }

    private static void getInfo() {
        log("欢迎使用EndMinecraft压测程序", "", "官方QQ群： 473516200", "", "=======================", "", "请输入ip地址");
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
    }

    private static void scanServer() {
        log("正在探测服务器信息，请稍后");
        ScanInfo.ScanMotdInfo(ip, port);
    }

    private static void scanBug() {
        log("是否开始进服前漏洞探测y/n");
        if ("y".equalsIgnoreCase(sc.next())) {
            bugData = new BugData(ip, port);
            ScanBug.scanMOTD(ip, port);
            ScanBug.scanTAB(ip, port);
            log("漏洞检测结果： ", bugData.toString());
        }
    }

    private static void showMenu() throws IOException, InterruptedException {
        Menu menu = new Menu(sc, ip, port);
        while (true) {
            log("请输入攻击方式：", "1 : MOTD攻击", "2 : 分布式假人攻击(集群压测)", "3 : 单ipTAB攻击");
            log("4 : 分布式MOTD压测(集群MOTD，测试功能)", "5 ： 分布式假人攻击(集群压测)操死乐乐模式(绕过乐乐插件检测，测试功能)");
            log("========================");
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
                case 4: {
                    menu._4();
                    break;
                }
                case 5: {
                    leLe = true;
                    menu._2();
                    break;
                }
                default: {
                    log("您的选择有误，请重新选择");
                    break;
                }
            }
        }
    }

    private static void checkVersion() throws InterruptedException {
        Updater updater = new Updater(version);
        log("当前软件版本：" + version);
        if (!updater.check()) {
            log("发现更新！，即将打开浏览器下载，程序退出");
            Thread.sleep(3000);
            try {
                String url = updater.getNewVersion();
                java.net.URI uri = java.net.URI.create(url);
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    dp.browse(uri);
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        log("更新检查完毕。未发现更新");
    }
}