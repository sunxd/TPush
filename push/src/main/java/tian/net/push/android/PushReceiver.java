package tian.net.push.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tian.net.push.Client;
import tian.net.push.utils.Utils;
import timber.log.Timber;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static tian.net.push.PushManager.ACTION_HEALTH_CHECK;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/15 上午11:30<br/>
 */

public class PushReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Timber.d("Action = " + action);
        if (CONNECTIVITY_ACTION.equals(action)) {//处理网络变化
            boolean hasNetwork = Utils.hasNetwork(context);
            Timber.w("网络已" + (hasNetwork?"连接":"断开"));
            if(hasNetwork) {
                if(Client.INSTANCE.isRunning()) {
                    Client.INSTANCE.onNetworkStateChange(true);
                    if(Client.INSTANCE.isAlarm()) {
                        Utils.startBroadcast(context, Client.INSTANCE.getHeatbeatStep(), ACTION_HEALTH_CHECK);
                    }
                }
            }else {
                Client.INSTANCE.onNetworkStateChange(false);
                Utils.stopBroadcast(context, ACTION_HEALTH_CHECK);
            }
        }else if(ACTION_HEALTH_CHECK.equals(action)) {
            if(Client.INSTANCE.isRunning()) {
                Client.INSTANCE.healthcheck();
                Utils.startBroadcast(context, Client.INSTANCE.getHeatbeatStep(), ACTION_HEALTH_CHECK);
            }
        }
    }

}
