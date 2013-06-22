package com.filemanager.server.views;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import com.filemanager.utils.Constants.Config;
import com.filemanager.server.TempCacheFilter;
import com.filemanager.server.entity.GzipByteArrayEntity;
import com.filemanager.server.entity.GzipFileEntity;
import com.filemanager.utils.GzipUtil;

import android.util.Log;

/**
 * This class defines a custom template view
 * @author Anubhav
 *
 */
public class TempView extends BaseView<String, Map<String, Object>> {

    static final String TAG = "TempView";
    static final boolean DEBUG = false || Config.DEV_MODE;

    
    /**
     * render the custom template view
     */
    @Override
    public HttpEntity render(HttpRequest request, final String tempFile,
            final Map<String, Object> data) throws IOException {
        if (Config.USE_GZIP && GzipUtil.getSingleton().isGZipSupported(request)) {
            if (Config.USE_FILE_CACHE && TempCacheFilter.isCacheTemp(tempFile)) {
                File cacheFile = new File(Config.FILE_CACHE_DIR, tempFile + Config.EXT_GZIP);
                return renderFromCacheGzipFile(cacheFile, tempFile, data);
            } else {
                if (DEBUG)
                    Log.d(TAG, "Directly return gzip stream for " + tempFile);
                String html = TempHandler.render(tempFile, data);
                return new GzipByteArrayEntity(html.getBytes(), false);
            }
        } else if (Config.USE_FILE_CACHE && TempCacheFilter.isCacheTemp(tempFile)) {
            File cacheFile = new File(Config.FILE_CACHE_DIR, tempFile);
            return renderFromCacheFile(cacheFile, tempFile, data);
        }
        String html = TempHandler.render(tempFile, data);
        return new StringEntity(html, Config.ENCODING);
    }

    private HttpEntity renderFromCacheGzipFile(File cacheFile, String tempFile,
            Map<String, Object> data) throws IOException {
        if (cacheFile.exists()) {
            if (DEBUG)
                Log.d(TAG, "Read from cache " + cacheFile);
        } else {
            String html = TempHandler.render(tempFile, data);
            writeStringToGzipFile(cacheFile, html);
            if (DEBUG)
                Log.d(TAG, "Cache to " + cacheFile + " and read it.");
        }
        return new GzipFileEntity(cacheFile, "text/html", true);
    }

    private void writeStringToGzipFile(File file, String data) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        InputStream is = new ByteArrayInputStream(data.getBytes());
        try {
            GzipUtil.getSingleton().gzip(is, os);
        } finally {
            os.close();
            is.close();
        }
    }

    private HttpEntity renderFromCacheFile(File cacheFile, String tempFile, Map<String, Object> data)
            throws IOException {
        if (cacheFile.exists()) {
            if (DEBUG)
                Log.d(TAG, "Read from cache " + cacheFile);
        } else {
            String html = TempHandler.render(tempFile, data);
            writeStringToFile(cacheFile, html);
            if (DEBUG)
                Log.d(TAG, "Cache to " + cacheFile + " and read it.");
        }
        return new FileEntity(cacheFile, "text/html");
    }

    private void writeStringToFile(File file, String data) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

}
