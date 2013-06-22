package com.filemanager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import com.filemanager.utils.Constants.Config;
import com.filemanager.server.handlers.HttpDelHandler;
import com.filemanager.server.handlers.HttpDownHandler;
import com.filemanager.server.handlers.HttpFBHandler;
import com.filemanager.server.handlers.HttpProgressHandler;
import com.filemanager.server.handlers.HttpUpHandler;
import com.filemanager.utils.CommonUtil;

/**
 * This is the webserver class which is kind of itself a process for the application
 * @author Anubhav
 *
 */
public class WebServer extends Thread {

    static final String TAG = "WebServer";
    static final boolean DEBUG = false || Config.DEV_MODE;

    public static final int ERR_UNEXPECT = 0x0101;
    public static final int ERR_PORT_IN_USE = 0x0102;
    public static final int ERR_TEMP_NOT_FOUND = 0x0103;

    private int port;
    private String webRoot;

    private ServerSocket serverSocket;
    /* package */static boolean isLoop;

    private OnWebServListener mListener;

    private ExecutorService pool; // 线程池

    public WebServer(int port, final String webRoot) {
        super();
        this.port = port;
        this.webRoot = webRoot;
        isLoop = false;

        pool = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try {
            // Decide if port is in use.
            if (CommonUtil.getSingleton().isLocalPortInUse(port)) {
                if (mListener != null) {
                    mListener.onError(ERR_PORT_IN_USE);
                }
                return;
            }
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            BasicHttpProcessor httpproc = new BasicHttpProcessor();
            httpproc.addInterceptor(new ResponseDate());
            httpproc.addInterceptor(new ResponseServer());
            httpproc.addInterceptor(new ResponseContent());
            httpproc.addInterceptor(new ResponseConnControl());
            HttpService httpService = new HttpService(httpproc,
                    new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());
            HttpParams params = new BasicHttpParams();
            params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                    .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                    .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                    .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                    .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "WebServer/1.1");
            httpService.setParams(params);
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            reqistry.register(UrlPattern.DOWNLOAD, new HttpDownHandler(webRoot));
            reqistry.register(UrlPattern.DELETE, new HttpDelHandler(webRoot));
            reqistry.register(UrlPattern.UPLOAD, new HttpUpHandler(webRoot));
            reqistry.register(UrlPattern.PROGRESS, new HttpProgressHandler());
            reqistry.register(UrlPattern.BROWSE, new HttpFBHandler(webRoot));
            httpService.setHandlerResolver(reqistry);
            if (mListener != null) {
                mListener.onStarted();
            }
            isLoop = true;
            while (isLoop && !Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                conn.bind(socket, params);
                Thread t = new WorkerThread(httpService, conn, mListener);
                t.setDaemon(true); 
                pool.execute(t); 
            }
        } catch (IOException e) {
            if (isLoop) { 
                if (mListener != null) {
                    mListener.onError(ERR_UNEXPECT);
                }
                if (DEBUG)
                    e.printStackTrace();
                isLoop = false;
            }
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (mListener != null) {
                    mListener.onStopped();
                }
            } catch (IOException e) {
            }
        }
    }

    public void close() {
        isLoop = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
        }
    }

    public interface OnWebServListener {
        void onStarted();

        void onStopped();

        void onError(int code);
    }

    public void setOnWebServListener(OnWebServListener mListener) {
        this.mListener = mListener;
    }

}
