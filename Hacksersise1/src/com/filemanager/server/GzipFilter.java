package com.filemanager.server;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.filemanager.utils.CommonUtil;

/**
 * This is a GZIP compression class
 * @author Anubhav
 *
 */
public class GzipFilter {

     private static final Set<String> gzipSet;

    static {
        gzipSet = new HashSet<String>();
        addGzipExtension("htm");
        addGzipExtension("html");
        addGzipExtension("js");
        addGzipExtension("css");
    }

    public static void addGzipExtension(String extension) {
        gzipSet.add(extension);
    }

    public static void addGzipExtensions(String... extensions) {
        gzipSet.addAll(Arrays.asList(extensions));
    }

    public static boolean isGzipExtension(String extension) {
        return gzipSet.contains(extension);
    }

    public static boolean isGzipFile(File file) {
        return gzipSet.contains(CommonUtil.getSingleton().getExtension(file));
    }

}
