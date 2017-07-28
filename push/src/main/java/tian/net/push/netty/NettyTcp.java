package tian.net.push.netty;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import tian.net.push.PushConfig;
import tian.net.push.PushListener;
import tian.net.push.netty.handler.ChannelHandlerHolder;
import tian.net.push.netty.handler.ConnectWatchdog;
import tian.net.push.netty.handler.CustomHandlerProvider;
import tian.net.push.netty.handler.HeartbeatHandler;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/19 上午11:14<br/>
 */

public class NettyTcp implements ChannelHandlerHolder, INetty{


    public enum State {connecting, connected, disconnecting, disconnected}

    private final AtomicReference<State> state = new AtomicReference<>(State.disconnected);

    PushConfig config;
    private Timer timer;
    private ConnectWatchdog connectWatchDog;
    private PushListener listener;

    CustomHandlerProvider handlerProvider;

    private SocketChannel socketChannel;


    public NettyTcp(PushConfig config) {
        Timber.tag("PushConn");
        this.config = config;
        timer = new HashedWheelTimer();
        connectWatchDog = new ConnectWatchdog(this, timer, true);
    }


    public void connect(){
        if(state.compareAndSet(State.disconnected, State.connecting)) {
            doConnect(config.host, config.port);
        }
    }

    private void doConnect(final String host, final int port) {
        Timber.d("doConnect...");
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(handlers());
            }
        });

        try {
            ChannelFuture future =bootstrap.connect(host,port).sync();
            if (future.isSuccess()) {
                Timber.d("doConnect success...");
                socketChannel = (SocketChannel)future.channel();
                state.set(State.connected);
                if(listener != null) listener.onConnected();
            }else {
                Timber.d("doConnect fail...");
                state.set(State.disconnected);
                connectWatchDog.retryConnect();
            }
        }catch (Exception e) {
            Timber.d("doConnect error = " + e.getMessage());
            //e.printStackTrace();
            if(socketChannel != null) {
                socketChannel.close();
                socketChannel = null;
            }
            state.set(State.disconnected);
            connectWatchDog.retryConnect();
        }

    }


    public void reConnect() {
        close();
        doReConnect();
    }

    private void doReConnect() {
        Timber.d("doReConnect...");
        connect();
    }

    public void close() {
        if(state.compareAndSet(State.connected, State.disconnecting)) {
            doClose();
        }
    }

    @Override
    public void setListener(PushListener l) {
        listener = l;
    }

    @Override
    public void setCustomeHandlerProvider(CustomHandlerProvider provider) {
        handlerProvider = provider;
    }

    @Override
    public void setAutoConnect(boolean auto) {
        connectWatchDog.setAutoConnect(auto);
    }

    @Override
    public void send(Object o) {
        if(socketChannel != null) {
            socketChannel.writeAndFlush(o);
            //Timber.d("send = " + o.toString());
        }
    }

    @Override
    public void healthCheck() {
        Timber.d("heartbeat...");
        Object o = config.getHeatbeatEntity();
        send(o!=null?o:config.getHeartbeat().getPing());
    }

    @Override
    public String getMode() {
        return config.getHeartbeat().getMode();
    }

    private void doClose() {
        Timber.d("doClose...");
        if(socketChannel != null) {
            if(socketChannel.isOpen()) {
                socketChannel.close();
                socketChannel = null;
            }
        }
        state.set(State.disconnected);
    }


    @Override
    public ChannelHandler[] handlers() {
        LinkedList<ChannelHandler> handlers = new LinkedList<ChannelHandler>();
        if(handlerProvider != null) {
            handlers.addAll(Arrays.asList(handlerProvider.handlers()));
        }else {

        }
        handlers.addFirst(connectWatchDog);
        if(PushConfig.Heartbeat.MODE_NETTY.equals(config.getHeartbeat().getMode())) {
            handlers.addFirst(new IdleStateHandler(0, 0, config.getHeartbeat().getStep()));
            handlers.add(new HeartbeatHandler(this));
        }
        handlers.addLast(new NettyClientHandler());
        Timber.d("handler = " + handlers.toString());
        return handlers.toArray(new ChannelHandler[handlers.size()]);
    }

    /**
     * 消息接收
     */
    class NettyClientHandler extends SimpleChannelInboundHandler<Object> {

        /*@Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Timber.d("receive = " + msg.toString());
            if(listener != null) {
                listener.receiver(msg);
            }
        }*/

        /*@Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            Timber.e("channelInactive...");
        }*/

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //super.exceptionCaught(ctx, cause);
            state.set(State.disconnected);
            if(listener != null) listener.onDisConnected();
            Timber.e("exceptionCaught = " + cause.getMessage());
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            //Timber.d("receive = " + msg.toString());
            if(listener != null) {
                listener.receiver(msg);
            }
        }
    }

    public boolean isConnected() {
        return state.get() == State.connected;
    }

}
