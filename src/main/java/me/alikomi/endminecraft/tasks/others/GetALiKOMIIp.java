package me.alikomi.endminecraft.tasks.others;

import me.alikomi.endminecraft.utils.HttpReq;
import me.alikomi.endminecraft.utils.Util;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GetALiKOMIIp extends Util {
    public static Map<String, Proxy.Type> getALiKOMIIp(int maxAttack, Scanner sc) throws InterruptedException {
        boolean jy = true;
        String pass = "";
        String user = "";
        while (jy) {
            log("请输入账号（如果您没有账号的话，请去官网注册: http://proxy.alikomi.me/）");
            user = sc.next();
            if (user.matches("[A-Za-z0-9_]*")) {
                jy = false;
            }
        }
        jy = true;
        while (jy) {
            log("请输入密码");
            pass = sc.next();
            if (pass.matches("[A-Za-z0-9_]*")) {
                jy = false;
            }
        }
        String re = HttpReq.sendPost("http://proxys.alikomi.me:9988/", "user=" + user + "&pass=" + pass);
        if (re.contains("<static>") && re.contains("</static>")) {
            String status = re.substring(re.indexOf("<static>"), re.indexOf("</static>"));
            log(status);
            Thread.sleep(2000);
            if (re.contains("<ip>") && re.contains("</ip>")) {
                String iplist = re.substring(re.indexOf("<ip>"), re.indexOf("</ip>"));
                log(iplist);
                if (iplist.contains("<br />")) {
                    Map<String, Proxy.Type> ips = new HashMap<>();
                    int jc = 0;
                    for (String s : iplist.split("<br />")) {
                        ips.put(s, Proxy.Type.HTTP);
                        jc++;
                        if (jc >= maxAttack) return ips;
                    }
                    return ips;
                } else {
                    log("ip错误！！", "程序将5秒后关闭");
                    Thread.sleep(5000);
                    System.exit(4);
                    return null;
                }
            } else {
                log("获取ip失败！！", "程序将5秒后关闭");
                Thread.sleep(5000);
                System.exit(3);
                return null;
            }
        } else

        {
            log("获取失败！！！，未知状态！", "程序将5秒后关闭");
            Thread.sleep(5000);
            System.exit(2);
            return null;
        }

    }
}
