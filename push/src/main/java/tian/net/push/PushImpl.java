package tian.net.push;

import android.content.Context;

import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;

import tian.net.push.netty.INetty;
import tian.net.push.netty.NettyTcp;
import tian.net.push.netty.handler.CustomHandlerProvider;
import tian.net.push.utils.Utils;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/12 下午2:32<br/>
 */

public class PushImpl implements Push {


    private enum State {
        Started,
        Shutdown
    }

    INetty netty;
    PushConfig pushConfig;


    private final AtomicReference<State> pushState = new AtomicReference<State>(State.Shutdown);

    public PushImpl() {
        pushConfig = new PushConfig();
        //netty = new RxNettyTcp(pushConfig);
        netty = new NettyTcp(pushConfig);
    }

    @Override
    public void init(Context ctx) {
        try {
            String json = Utils.convertStreamToString(ctx.getAssets().open("config.json"));
            Gson gson = new Gson();
            PushConfig config = gson.fromJson(json, PushConfig.class);
            pushConfig.setConfig(config);
            Timber.d("使用配置：" + pushConfig.toString());
        }catch (Exception e) {
            Timber.d("解析 file:///assets/config.json 失败，使用默认配置。");
            Timber.d("默认配置：" + pushConfig.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if(pushState.compareAndSet(State.Shutdown, State.Started)) {
            Timber.d("push start...");
            netty.setAutoConnect(true);
            netty.connect();
        }
    }

    @Override
    public void stop() {
        if(pushState.compareAndSet(State.Started, State.Shutdown)) {
            netty.setAutoConnect(false);
            netty.close();
            Timber.d("push shutdown...");
        }
    }

    public boolean isRunning() {
        return pushState.get() == State.Started;
    }

    @Override
    public void onNetworkStateChange(boolean isConnected) {
        netty.setAutoConnect(isConnected);
        if(isConnected) {
            netty.connect();
        }else if(netty.isConnected()){
            //TODO 马上发一次心跳？
        }
    }

    @Override
    public void send(Object msg) {
        netty.send(msg);
    }

    @Override
    public void setPushMessageListener(PushListener l) {
        netty.setListener(l);
    }

    @Override
    public void setCustomeHandlerProvider(CustomHandlerProvider provider) {
        netty.setCustomeHandlerProvider(provider);
    }

    @Override
    public PushConfig getConfig() {
        return pushConfig;
    }

    @Override
    public void healthCheck() {
        netty.healthCheck();
    }

    @Override
    public boolean isAlarm() {
        if(PushConfig.Heartbeat.MODE_ALARM.equals(netty.getMode())) {
            return true;
        }
        return false;
    }
}
