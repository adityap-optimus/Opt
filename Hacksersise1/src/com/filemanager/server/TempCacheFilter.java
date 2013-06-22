package com.filemanager.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * THis is cache class
 * @author Anubhav
 *
 */
public class TempCacheFilter {

    private static final Set<String> cacheSet;

    static {
        cacheSet = new HashSet<String>();
    }

    public static void addCacheTemp(String tempFile) {
        cacheSet.add(tempFile);
    }

    public static void addCacheTemps(String... tempFiles) {
        cacheSet.addAll(Arrays.asList(tempFiles));
    }

    public static boolean isCacheTemp(String tempFile) {
        return cacheSet.contains(tempFile);
    }
}
