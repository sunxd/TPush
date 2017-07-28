package tian.net.push.netty.handler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import tian.net.push.netty.INetty;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/13 下午6:10<br/>
 */
@ChannelHandler.Sharable
public class ConnectWatchdog extends ChannelInboundHandlerAdapter implements TimerTask{

    private int attempts;
    private int[] retry = new int[]{2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

    private Timer timer;
    private INetty rxNettyTcp;
    private boolean reconnect;
    private boolean connected;

    public ConnectWatchdog(INetty rxNettyTcp, Timer t, boolean autoConn) {
        this.rxNettyTcp = rxNettyTcp;
        timer  = t;
        reconnect = autoConn;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Timber.w("channelActive");

        attempts = 0;
        connected = true;

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Timber.w("channelInactive");

        connected = false;
        retryConnect();

        ctx.fireChannelInactive();
    }


    public void retryConnect() {
        if(attempts < retry.length) {
            int timeout = retry[attempts];
            if(timeout < 4) timeout = 4;
            attempts ++;
            timer.newTimeout(this, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        if(!reconnect) return;
        if(connected) return;
        Timber.d("正在重连， 第%d次...", attempts);
        rxNettyTcp.reConnect();
    }

    public boolean isAutoConnect() {
        return reconnect;
    }

    public void setAutoConnect(boolean reconnect) {
        this.reconnect = reconnect;
    }
}
