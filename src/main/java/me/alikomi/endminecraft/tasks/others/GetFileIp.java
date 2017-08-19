package me.alikomi.endminecraft.tasks.others;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class GetFileIp {
    public static Map<String, Proxy.Type> getFileIp(int maxAttack) throws IOException {
        int hs = 0;
        Map<String, Proxy.Type> ips = new HashMap<>();
        File file = new File("sockets.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            ips.put(tempString, Proxy.Type.SOCKS);
            hs++;
            if (hs >= maxAttack) return ips;
        }
        reader.close();

        file = new File("https.txt");
        reader = new BufferedReader(new FileReader(file));
        tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            ips.put(tempString, Proxy.Type.HTTP);
            hs++;
            if (hs >= maxAttack) return ips;
        }
        reader.close();
        return ips;
    }
}
