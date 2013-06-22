package com.filemanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import com.filemanager.utils.Constants.Config;

/**
 * CZip Compression Utils
 * @author Aditya P 
 */
public class GzipUtil {

    static class Holder {
        static GzipUtil instance = new GzipUtil();
    }

    public static GzipUtil getSingleton() {
        return Holder.instance;
    }

    private GzipUtil() {
    }

    /**
     * This method tells whether Gzip compression is supprted or not
     * @param request
     * @return
     */
    public boolean isGZipSupported(HttpRequest request) {
        Header header = request.getFirstHeader("Accept-Encoding");
        return ((header != null) && header.getValue().toLowerCase().indexOf("gzip") != -1);
    }

   /**
    * This method performs the Gzip compression
    * @param from
    * @param to
    * @throws IOException
    */
    public void gzip(File from, File to) throws IOException {
        to.getParentFile().mkdirs();
        to.createNewFile();
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        try {
            gzip(fis, fos);
        } finally {
            fis.close();
            fos.close();
        }
    }

    /**
     * This method performs the GZip Compression
     * @param from
     * @param to
     * @throws IOException
     */
    public void gzip(InputStream from, OutputStream to) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(to);
        int count;
        byte[] buffer = new byte[Config.BUFFER_LENGTH];
        while ((count = from.read(buffer)) != -1) {
            gos.write(buffer, 0, count);
        }
        gos.finish();
        gos.flush();
    }

}
