package tian.net.push.android;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

import tian.net.push.Client;
import tian.net.push.MessageWrap;
import tian.net.push.PushManager;
import tian.net.push.PushListener;
import tian.net.push.utils.Utils;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/7 下午3:31<br/>
 */

public class PushService extends Service implements PushListener {

    PushReceiver pushReceiver;

    LocalBroadcastManager cbm;

    @Override
    public void onCreate() {
        super.onCreate();
        //Timber.plant(new Timber.DebugTree());
        Timber.d("PushService.create()");

        cbm = LocalBroadcastManager.getInstance(this);

        pushReceiver = new PushReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(PushManager.ACTION_HEALTH_CHECK);
        registerReceiver(pushReceiver, intentFilter);

        Client.INSTANCE.init(this);
        Client.INSTANCE.setPushMessageListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("PushService.onStartCommand()");

        Client.INSTANCE.start();

        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushReceiver);
        Utils.stopBroadcast(this, PushManager.ACTION_HEALTH_CHECK);
        Client.INSTANCE.stop();
        Timber.d("PushService.onDestroy()");
    }

    @Override
    public void receiver(Object message) {
        Intent intent = new Intent(PushManager.RECEIVER_ACTION);
        MessageWrap mw = new MessageWrap();
        mw.setO(message);
        intent.putExtra(PushManager.MESSAGE, mw);
        cbm.sendBroadcast(intent);
    }

    @Override
    public void onConnected() {
        Intent intent = new Intent(PushManager.CONNECTED_ACTION);
        cbm.sendBroadcast(intent);
    }

    @Override
    public void onDisConnected() {
        Intent intent = new Intent(PushManager.DISCONNECTED_ACTION);
        cbm.sendBroadcast(intent);
    }
}
