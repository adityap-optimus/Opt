package com.filemanager.service;

import com.filemanager.utils.Constants.Config;
import com.filemanager.reciever.NetworkReceiver;
import com.filemanager.reciever.OnNetworkListener;
import com.filemanager.reciever.OnStorageListener;
import com.filemanager.reciever.StorageReceiver;
import com.filemanager.reciever.WSReceiver;
import com.filemanager.utils.CommonUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @brief 应用�?��?��?务
 * @author join
 */
public class WSService extends Service implements OnNetworkListener, OnStorageListener {

    static final String TAG = "WSService";
    static final boolean DEBUG = false || Config.DEV_MODE;

    public static final String ACTION = "org.join.service.WS";

    public boolean isWebServAvailable = false;

    private boolean isNetworkAvailable;
    private boolean isStorageMounted;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkReceiver.register(this, this);
        StorageReceiver.register(this, this);

        CommonUtil mCommonUtil = CommonUtil.getSingleton();
        isNetworkAvailable = mCommonUtil.isNetworkAvailable();
        isStorageMounted = mCommonUtil.isExternalStorageMounted();

        isWebServAvailable = isNetworkAvailable && isStorageMounted;
        notifyWebServAvailable(isWebServAvailable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkReceiver.unregister(this);
        StorageReceiver.unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(boolean isWifi) {
        isNetworkAvailable = true;
        notifyWebServAvailableChanged();
    }

    @Override
    public void onDisconnected() {
        isNetworkAvailable = false;
        notifyWebServAvailableChanged();
    }

    @Override
    public void onMounted() {
        isStorageMounted = true;
        notifyWebServAvailableChanged();
    }

    @Override
    public void onUnmounted() {
        isStorageMounted = false;
        notifyWebServAvailableChanged();
    }

    private void notifyWebServAvailable(boolean isAvailable) {
        if (DEBUG)
            Log.d(TAG, "isAvailable:" + isAvailable);
        // Notify if web service is available.
        String action = isAvailable ? WSReceiver.ACTION_SERV_AVAILABLE
                : WSReceiver.ACTION_SERV_UNAVAILABLE;
        Intent intent = new Intent(action);
        sendBroadcast(intent, WSReceiver.PERMIT_WS_RECEIVER);
    }

    private void notifyWebServAvailableChanged() {
        boolean isAvailable = isNetworkAvailable && isStorageMounted;
        if (isAvailable != isWebServAvailable) {
            notifyWebServAvailable(isAvailable);
            isWebServAvailable = isAvailable;
        }
    }

}
