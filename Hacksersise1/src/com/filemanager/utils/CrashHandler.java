package com.filemanager.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import com.aditya.hacksersise1.R;
import com.filemanager.utils.CommonUtil;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
/**
 * This a generic crash / ANF exception handler
 * @author Anubhav
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {

    static final String LOG_NAME = ".crash";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    public CrashHandler(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }

    }

    /**
     * Handle the uncaught exception
     * @param ex
     * @return
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext,
                        mContext.getString(R.string.info_crash, Constants.APP_DIR + LOG_NAME),
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * Saves the application crash info to a file
     * @param ex
     */
    private void saveCrashInfoToFile(Throwable ex) {
        final StackTraceElement[] stack = ex.getStackTrace();
        final String message = ex.getMessage();
        File logFile = new File(Constants.APP_DIR + LOG_NAME);
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        FileWriter fw = null;
        CommonUtil mCommonUtil = CommonUtil.getSingleton();
        final String lineFeed = "\r\n";
        try {
            fw = new FileWriter(logFile, true);
            fw.write(mCommonUtil.currentTime(mCommonUtil.FORMAT_YMDHMS).toString() + lineFeed
                    + lineFeed);
            fw.write(message + lineFeed);
            for (int i = 0; i < stack.length; i++) {
                fw.write(stack[i].toString() + lineFeed);
            }
            fw.write(lineFeed);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fw)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
