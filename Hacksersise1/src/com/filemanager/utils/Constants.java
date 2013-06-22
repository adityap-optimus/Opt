package com.filemanager.utils;

import android.os.Environment;

/**
 * 
 * @author Aditya P
 */
public final class Constants {

	public static String APP_DIR_NAME = "/.ws/";
	public static String APP_DIR = Environment.getExternalStorageDirectory()
			+ APP_DIR_NAME;

	public static class Config {
		public static final boolean DEV_MODE = false;

		public static int PORT = 7766;
		public static String WEBROOT = "/";

		public static final String SERV_ROOT_DIR = APP_DIR + "root/";

		public static final String SERV_TEMP_DIR = SERV_ROOT_DIR + "temp/";

		public static final String ENCODING = "UTF-8";

		public static boolean ALLOW_DOWNLOAD = true;
		public static boolean ALLOW_DELETE = true;
		public static boolean ALLOW_UPLOAD = true;

		/**
		 * The threshold, in bytes, below which items will be retained in memory
		 * and above which they will be stored as a file.
		 */
		public static final int THRESHOLD_UPLOAD = 1024 * 1024; // 1MB

		public static boolean USE_GZIP = true;
		
		public static final String EXT_GZIP = ".gz"; // used in cache

		public static boolean USE_FILE_CACHE = true;
		public static final String FILE_CACHE_DIR = APP_DIR + "cache/";

		public static final int BUFFER_LENGTH = 4096;
	}

}
