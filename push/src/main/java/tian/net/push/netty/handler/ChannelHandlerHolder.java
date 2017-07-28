package tian.net.push.netty.handler;

import io.netty.channel.ChannelHandler;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/13 下午6:04<br/>
 */

public interface ChannelHandlerHolder {

    ChannelHandler[] handlers();
}
