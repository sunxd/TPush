package tian.net.push;

import android.content.Context;

import tian.net.push.netty.handler.CustomHandlerProvider;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/15 下午2:17<br/>
 */

public class Client{

    public static Client INSTANCE = getInstance();

    private Push push;

    private Client() {
        push = new PushImpl();
    }

    private static Client getInstance() {
        if (INSTANCE == null) {
            synchronized (Client.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Client();
                }
            }
        }
        return INSTANCE;
    }


    public void init(Context context) {
        push.init(context);
    }

    public void start() {
        push.start();
    }

    public void stop() {
        push.stop();
    }

    public void onNetworkStateChange(boolean isConnected) {
        push.onNetworkStateChange(isConnected);
    }

    public boolean isRunning() {
        return push.isRunning();
    }

    public void send(Object msg) {
        push.send(msg);
    }

    public void setPushMessageListener(PushListener l) {
        push.setPushMessageListener(l);
    }

    public void setCustomeHandlerProvider(CustomHandlerProvider provider) {
        push.setCustomeHandlerProvider(provider);
    }

    public int getHeatbeatStep() {
        return push.getConfig().getHeartbeat().getStep();
    }

    public void healthcheck() {
        push.healthCheck();
    }

    public boolean isAlarm() {
        return push.isAlarm();
    }

    public void setHeartbeatEntity(Object heartbeatEntity) {
        push.getConfig().setHeatbeatEntity(heartbeatEntity);
    }
}
