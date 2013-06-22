package com.filemanager.server.views;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.entity.FileEntity;
import com.filemanager.utils.Constants.Config;
import com.filemanager.server.GzipFilter;
import com.filemanager.server.entity.GzipFileEntity;
import com.filemanager.utils.GzipUtil;
import com.filemanager.utils.MIME;

import android.util.Log;

/**
 * This is a custom view to show a file
 * 
 * @author Anubhav
 * 
 */
public class FileView extends BaseView<File, String> {

	static final String TAG = "FileView";
	static final boolean DEBUG = false || Config.DEV_MODE;

	/**
	 * This function renders the file view
	 */
	@Override
	public HttpEntity render(HttpRequest request, final File file,
			String contentType) throws IOException {
		if (contentType == null) {
			String mine = MIME.getFromFile(file);
			contentType = null == mine ? "charset=" + Config.ENCODING : mine
					+ ";charset=" + Config.ENCODING;
		}
		if (Config.USE_GZIP && GzipUtil.getSingleton().isGZipSupported(request)
				&& GzipFilter.isGzipFile(file)) {
			if (Config.USE_FILE_CACHE) {
				File cacheFile = new File(Config.FILE_CACHE_DIR, file.getName()
						+ Config.EXT_GZIP);
				if (cacheFile.exists()) {
					if (DEBUG)
						Log.d(TAG, "Read from cache " + cacheFile);
				} else {
					GzipUtil.getSingleton().gzip(file, cacheFile);
					if (DEBUG)
						Log.d(TAG, "Cache to " + cacheFile + " and read it.");
				}
				return new GzipFileEntity(cacheFile, contentType, true);
			} else {
				if (DEBUG)
					Log.d(TAG, "Directly return gzip stream for " + file);
				return new GzipFileEntity(file, contentType, false);
			}
		}
		return new FileEntity(file, contentType);
	}

}
