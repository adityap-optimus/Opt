package com.filemanager.utils;

import java.io.IOException;

import net.asfun.jangod.lib.TagLibrary;
import com.filemanager.server.jangod.tag.ResColorTag;
import com.filemanager.server.jangod.tag.ResStrTag;
import com.filemanager.server.jangod.tag.UUIDTag;

import com.filemanager.utils.Constants.Config;
import com.filemanager.server.TempCacheFilter;
import com.filemanager.service.WSService;
import com.filemanager.activity.PreferActivity;
import com.filemanager.utils.CopyUtil;

import android.app.Application;
import android.content.Intent;

/**
 * This a webservice application class
 * @author Anubhav
 *
 */
public class WSApplication extends Application {

    private static WSApplication self;

    private Intent wsServIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        self = this;
        wsServIntent = new Intent(WSService.ACTION);

        initAppDir();
        initJangod();
        initAppFilter();

        if (!Config.DEV_MODE) {
            new CrashHandler(this);
        }

        PreferActivity.restoreAll();
    }

    public static WSApplication getInstance() {
        return self;
    }

    public void startWsService() {
        startService(wsServIntent);
    }

    public void stopWsService() {
        stopService(wsServIntent);
    }

    /**
     * This function initialzes the app directories
     */
    private void initAppDir() {
        CopyUtil mCopyUtil = new CopyUtil(getApplicationContext());
        // mCopyUtil.deleteFile(new File(Config.SERV_ROOT_DIR)); // 清�?��?务文件目录
        try {
            mCopyUtil.assetsCopy("ws", Config.SERV_ROOT_DIR, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initJangod() {
        /* custom tags */
        TagLibrary.addTag(new ResStrTag());
        TagLibrary.addTag(new ResColorTag());
        TagLibrary.addTag(new UUIDTag());
        /* custom filters */
    }

    private void initAppFilter() {
        /* TempCacheFilter */
        TempCacheFilter.addCacheTemps("403.html", "404.html", "503.html");
        /* GzipFilter */
    }

}
