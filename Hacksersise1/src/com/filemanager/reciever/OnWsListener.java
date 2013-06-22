package com.filemanager.reciever;

/**
 * This is a listener/interface to check the server avalibility
 * @author Anubhav
 *
 */
public interface OnWsListener {

   
    void onServAvailable();

    
    void onServUnavailable();

}
