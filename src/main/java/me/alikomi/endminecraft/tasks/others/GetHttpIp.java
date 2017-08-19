package me.alikomi.endminecraft.tasks.others;

import me.alikomi.endminecraft.utils.HttpReq;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class GetHttpIp {
    public static Map<String, Proxy.Type> getHttpIp(int maxAttack) {
        Map<String, Proxy.Type> ips = new HashMap<>();
        String req = HttpReq.sendPost("http://www.66ip.cn/mo.php?sxb=&tqsl=" + maxAttack + "&port=&export=&ktip=&sxa=&submit=%CC%E1++%C8%A1&textarea=", "");
        if (req != null && !req.equalsIgnoreCase("")) {
            String[] arr = req.split("<br />");
            int a = 0;
            for (String index : arr) {
                a++;
                if (a == 1 || a == 2 || a == arr.length || a == arr.length - 1) {
                    continue;
                }
                ips.put(index.trim(), Proxy.Type.HTTP);
            }
        } else {
            System.exit(1);
        }
        return ips;
    }
}
