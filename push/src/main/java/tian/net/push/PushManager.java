package tian.net.push;

import android.content.Context;
import android.content.Intent;

import tian.net.push.android.PushService;
import tian.net.push.netty.handler.CustomHandlerProvider;
import tian.net.push.utils.Utils;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/16 上午11:06<br/>
 */

public class PushManager {

    public static final String PUSH_ACTION = "tian.net.push.PUSH_SERVICE";

    public static final String RECEIVER_ACTION = "tian.net.push.RECEIVER_MSG";

    public static final String CONNECTED_ACTION = "tian.net.push.RECEIVER_CONNECTED";

    public static final String DISCONNECTED_ACTION = "tian.net.push.RECEIVER_DISCONNECTED";

    public static final String MESSAGE = "tian.net.push.MESSAGE";

    public static final String ACTION_HEALTH_CHECK = "tian.net.push.HEARTBEAT";

    private Context context;

    public static PushManager M = getInstance();

    private static PushManager getInstance() {
        if(M == null) {
            synchronized (PushManager.class) {
                if(M == null) {
                    M = new PushManager();
                }
            }
        }
        return M;
    }


    public void init(Context ctx) {
        context = ctx;
        Client.INSTANCE.init(context);
    }

    public void startPush() {
        Intent intent = new Intent(PUSH_ACTION);
        intent.setClass(context, PushService.class);
        context.startService(intent);
    }

    public void stopPush() {
        Utils.stopBroadcast(context, ACTION_HEALTH_CHECK);
        Intent intent = new Intent(PUSH_ACTION);
        intent.setClass(context, PushService.class);
        context.stopService(intent);
    }

    public void send(Object txt) {
        Client.INSTANCE.send(txt);
    }

    public PushManager setCustomeHandlerProvider(CustomHandlerProvider provider) {
        Client.INSTANCE.setCustomeHandlerProvider(provider);
        return this;
    }

    public PushManager setHeartbeatEntity(Object o) {
        Client.INSTANCE.setHeartbeatEntity(o);
        return this;
    }
}
