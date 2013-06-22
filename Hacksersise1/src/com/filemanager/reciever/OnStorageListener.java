package com.filemanager.reciever;

/**
 * This is a listener which will be implemented when external storage is mounted or unmounted
 * @author Anubhav
 *
 */
public interface OnStorageListener {

   
    void onMounted();

    
    void onUnmounted();

}
