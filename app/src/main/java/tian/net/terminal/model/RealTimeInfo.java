package tian.net.terminal.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import tian.net.terminal.common.CommonBody;
import tian.net.terminal.util.DateUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/10 下午2:26<br/>
 */

public class RealTimeInfo implements CommonBody {


    byte[] dateTime;          //6
    byte[] data;

    public static RealTimeInfo create(byte[] data) {
        RealTimeInfo rti = new RealTimeInfo();
        rti.dateTime = DateUtil.getByteDateTime();
        rti.data = data;
        return rti;
    }


    @Override
    public byte[] toBytes() {
        int l = 6 + (data != null?data.length:0);
        byte[] b = new byte[l];
        ByteBuf out  = Unpooled.buffer(l);
        out.writeBytes(dateTime);
        if(data != null) {
            out.readBytes(data);
        }
        out.readBytes(b);
        out.clear();
        return b;
    }
}
