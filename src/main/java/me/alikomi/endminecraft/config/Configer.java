package me.alikomi.endminecraft.config;

import me.alikomi.endminecraft.utils.Util;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Configer extends Util {
    private static Map<Object, Object> resultMap = new HashMap<>();

    public Configer(File file) throws FileNotFoundException {
        final Yaml yaml = new Yaml();
        Iterable<Object> result = yaml.loadAll(new FileInputStream(file));
        List<Object> relist = copyIterator(result);
        relist.forEach((v) -> {
            if (v instanceof LinkedHashMap) {
                ((LinkedHashMap) v).forEach((k, v2) -> resultMap.put(k.toString(), v2));
            }
        });
    }

    private static <T> List<T> copyIterator(Iterable<T> iter) {
        List<T> copy = new ArrayList<>();
        iter.forEach(copy::add);
        return copy;
    }

    public <T> T get(T lx,String link) {
        if (!link.contains(".")) {
            if (resultMap.containsKey(link)) {
                return (T)resultMap.get(link);
            } else {
                return null;
            }
        }
        Object obj = getNext(link);
        if (obj == null) return null;
        return (T)obj;
    }


    private Object getNext(String link) {
        int i = 0;
        Map<Object, Object> resultMaptmp = resultMap;
        int l = link.split("\\.").length;
        for (String s : link.split("\\.")) {
            log(s);
            i++;
            if (resultMaptmp.containsKey(s)) {
                if (i == l) {
                    return resultMaptmp.get(s);
                }
                if (resultMaptmp.get(s) instanceof Map) {
                    resultMaptmp = (Map<Object, Object>) resultMaptmp.get(s);
                }
            } else {
                return null;
            }
        }
        return null;
    }

}
