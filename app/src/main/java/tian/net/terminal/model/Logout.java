package tian.net.terminal.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import tian.net.terminal.common.CommonBody;
import tian.net.terminal.common.Config;
import tian.net.terminal.util.DateUtil;
import tian.net.terminal.util.StringUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 上午10:47<br/>
 */

public class Logout implements CommonBody {

    byte[] dateTime;          //6
    short serialId;     //2


    public static Logout create() {
        Logout l = new Logout();
        l.dateTime = DateUtil.getByteDateTime();
        l.serialId = Config.M.getSerialId();
        return l;
    }

    @Override
    public byte[] toBytes() {
        byte[] b = new byte[8];
        ByteBuf out  = Unpooled.buffer(8);
        out.writeBytes(dateTime);
        out.writeShort(serialId);
        out.readBytes(b);
        out.clear();
        return b;
    }


}
