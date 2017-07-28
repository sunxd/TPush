package tian.net.terminal.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tian.net.terminal.util.CacheUtils;
import tian.net.terminal.util.StringUtil;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午3:56<br/>
 */

public class TransporterEncoder extends MessageToByteEncoder<Transporter> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Transporter msg, ByteBuf out) throws Exception {
        byte[] body = msg.getBodys();

        out.writeBytes(Protocol.MAGIC);     // 2
        out.writeByte(msg.getCmd());        // 1
        out.writeByte(msg.getRes());        // 1
        out.writeBytes(msg.getVin());       // 17
        out.writeByte(msg.getEncryption()); // 1
        out.writeShort(body.length);// 2
        out.writeBytes(body);
        out.writeByte(msg.getCheck());      // 1

        ByteBuf bb = out.copy();
        int l = 25 + body.length;
        byte[] bytes = new byte[25 + body.length];
        bb.readBytes(bytes, 0, l);
        Timber.d("Encode = " + StringUtil.byte2HexStr(bytes));
    }
}
