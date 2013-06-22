package com.filemanager.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Anubhav
 *
 */
public class Progress {

    private static Map<String, Integer> progressMap = new HashMap<String, Integer>();

    public static void update(String id, int progress) {
        progressMap.put(id, progress);
        if (progress == 100) {
            progressMap.remove(id);
        }
    }

    public static int get(String id) {
        Integer p = progressMap.get(id);
        return p == null ? -1 : p;
    }

    public static void clear() {
        progressMap.clear();
    }

}
