package tian.net.push.netty.handler;

import io.netty.channel.ChannelHandler;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/19 下午6:05<br/>
 */

public interface CustomHandlerProvider {

    ChannelHandler[] handlers();
}
