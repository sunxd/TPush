package tian.net.push;

import android.content.Context;

import tian.net.push.netty.handler.CustomHandlerProvider;


/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/7 下午3:37<br/>
 */

public interface Push {

    void init(Context context);

    void start();

    void stop();


    void onNetworkStateChange(boolean isConnected);

    boolean isRunning();

    void send(Object msg);

    void setPushMessageListener(PushListener l);

    void setCustomeHandlerProvider(CustomHandlerProvider provider);

    PushConfig getConfig();

    void healthCheck();

    boolean isAlarm();
}
