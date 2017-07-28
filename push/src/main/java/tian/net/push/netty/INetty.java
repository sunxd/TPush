package tian.net.push.netty;

import tian.net.push.PushListener;
import tian.net.push.netty.handler.CustomHandlerProvider;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/19 下午2:42<br/>
 */

public interface INetty {

    void connect();
    void reConnect();
    void close();

    boolean isConnected();

    void setListener(PushListener listener);
    void setCustomeHandlerProvider(CustomHandlerProvider provider);
    void setAutoConnect(boolean auto);

    void send(Object protocol);

    void healthCheck();

    String getMode();
}
