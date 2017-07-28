package tian.net.push.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import tian.net.push.netty.INetty;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/13 下午6:24<br/>
 */

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private INetty netty;


    public HeartbeatHandler(INetty rxNettyTcp) {
        this.netty = rxNettyTcp;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.ALL_IDLE) {
                netty.healthCheck();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
