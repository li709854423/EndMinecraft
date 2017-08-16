package me.alikomi.endminecraft.utils;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

class GetHttpIp {
    static Map<String, Proxy.Type> getHttpIp(int maxAttack) {
        Map<String, Proxy.Type> ips = new HashMap<>();
        String req = HttpReq.sendPost("http://www.89ip.cn/apijk/?&tqsl=" + (maxAttack) + "&sxa=&sxb=&tta=&ports=&ktip=&cf=1", "");

        if (req != null && !req.equalsIgnoreCase("")) {
            String[] arr = req.split("<BR>");
            int a = 0;
            for (String index : arr) {
                a++;
                if (a == 1 || a == 2 || a == arr.length || a == arr.length - 1) {
                    continue;
                }
                ips.put(index, Proxy.Type.HTTP);
            }
        } else {
            System.exit(1);
        }
        return ips;
    }
}
