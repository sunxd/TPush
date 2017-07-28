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
 * 2017/7/4 上午11:08<br/>
 */

public class Login implements CommonBody{

    byte[] dateTime;          //6
    short serialId;     //2
    byte[] iccId;         //20
    byte battery;         //1
    byte batteryNum;      //1
    byte[] batteryCodes;  // battery * batteryNum


    public static Login create(byte battery, byte batteryNum, byte[] batteryCodes) {
        Login l = new Login();
        l.dateTime = DateUtil.getByteDateTime();
        l.serialId = Config.M.getAndIncrementSerialId();
        l.iccId = Config.M.getIccId().getBytes();
        l.battery = battery;
        l.batteryNum = batteryNum;
        l.batteryCodes = batteryCodes;
        return l;
    }


    @Override
    public byte[] toBytes() {
        int l = 6 + 2 + 20 + 1 + 1 + battery * batteryNum;
        byte[] b = new byte[l];
        ByteBuf out  = Unpooled.buffer(l);
        out.writeBytes(dateTime);
        out.writeShort(serialId);
        out.writeBytes(iccId);
        out.writeByte(battery);
        out.writeByte(batteryNum);
        out.writeBytes(batteryCodes);
        out.readBytes(b);
        out.clear();
        return b;
    }

}
