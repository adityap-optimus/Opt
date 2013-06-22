package com.filemanager.reciever;

/**
 * This is a interface listener implemented when change in network state will happen
 * @author Anubhav
 *
 */
public interface OnNetworkListener {

   
    void onConnected(boolean isWifi);

    
    void onDisconnected();

}
