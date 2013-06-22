package com.filemanager.reciever;

import java.util.HashMap;
import java.util.Map;

import com.filemanager.utils.Constants.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * This is a broadcast receiver for web server availabel/unavailable
 * @author Anubhav
 *
 */
public class WSReceiver extends BroadcastReceiver {

    static final String TAG = "WSReceiver";
    static final boolean DEBUG = false || Config.DEV_MODE;

    public static final String ACTION_SERV_AVAILABLE = "org.join.action.SERV_AVAILABLE";
    public static final String ACTION_SERV_UNAVAILABLE = "org.join.action.SERV_UNAVAILABLE";

    public static final String PERMIT_WS_RECEIVER = "org.join.ws.permission.WS_RECEIVER";

    private static Map<Context, WSReceiver> mReceiverMap = new HashMap<Context, WSReceiver>();

    private OnWsListener mListener;

    public WSReceiver(OnWsListener listener) {
        mListener = listener;
    }

    /**
     * Registers the web server available scenario
     * @param context
     * @param listener
     */
    public static void register(Context context, OnWsListener listener) {
        if (mReceiverMap.containsKey(context)) {
            if (DEBUG)
                Log.d(TAG, "This context already registered.");
            return;
        }

        WSReceiver receiver = new WSReceiver(listener);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERV_AVAILABLE);
        filter.addAction(ACTION_SERV_UNAVAILABLE);
        context.registerReceiver(receiver, filter);

        mReceiverMap.put(context, receiver);

        if (DEBUG)
            Log.d(TAG, "WSReceiver registered.");
    }

    /**
     * Unregisters the receiver for web server
     * @param context
     */
    public static void unregister(Context context) {
        WSReceiver receiver = mReceiverMap.remove(context);
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;

            if (DEBUG)
                Log.d(TAG, "WSReceiver unregistered.");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DEBUG)
            Log.d(TAG, action);
        if (mListener == null) {
            return;
        }
        if (ACTION_SERV_AVAILABLE.equals(action)) {
            mListener.onServAvailable();
        } else { // ACTION_SERV_UNAVAILABLE
            mListener.onServUnavailable();
        }
    }

}
