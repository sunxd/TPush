package tian.net.terminal.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.util.StringUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午3:53<br/>
 */

public class Transporter {

    private byte cmd;
    private byte res;
    private byte[] vin;
    private byte encryption;
    private byte check;

    private byte[] bodys;
    private transient CommonBody commonBody;


    public byte[] getBodys() {
        return bodys;
    }

    public void setBodys(byte[] bodys) {
        this.bodys = bodys;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getRes() {
        return res;
    }

    public void setRes(byte res) {
        this.res = res;
    }

    public byte[] getVin() {
        return vin;
    }

    public void setVin(byte[] vin) {
        this.vin = vin;
    }

    public byte getEncryption() {
        return encryption;
    }

    public void setEncryption(byte encryption) {
        this.encryption = encryption;
    }

    public byte getCheck() {
        return check;
    }

    public void setCheck(byte check) {
        this.check = check;
    }

    /**
     *  cmd + bytes2HexStr(time[6])
     * @return
     */
    public String getTraceId() {
        byte[] time = new byte[6];
        System.arraycopy(bodys, 0, time, 0, time.length);
        return String.valueOf(cmd) + StringUtil.byte2HexStr(time);
    }

    public CommonBody getCommonBody() {
        return commonBody;
    }

    public void setCommonBody(CommonBody commonBody) {
        this.commonBody = commonBody;
    }


    public byte[] toBtyes() {
        // ## 不计算
        int length = Protocol.HEAD_LENGTH - 2;
        if(bodys != null) {
            length += bodys.length;
        }
        byte[] bytes = new byte[length];
        ByteBuf out = Unpooled.buffer(length);
        out.writeByte(cmd);
        out.writeByte(res);
        out.writeBytes(vin);
        out.writeByte(encryption);
        out.writeShort(bodys.length);
        out.writeBytes(bodys);
        out.readBytes(bytes);
        out.clear();
        return bytes;
    }

    /**
     * 心跳包
     * @return
     */
    public static Transporter createHeartBeat() {
        Transporter t = new Transporter();
        t.setCmd(Protocol.CMD.HEARTBEAT.get());
        t.setRes(Protocol.CMD.NORMAL.get());
        t.setVin(Config.M.getVin().getBytes());
        t.setEncryption(Config.M.getEncrypted().getEncryption());
        t.setBodys(new byte[0]);
        t.setCheck(Config.M.getEncrypted().checkBCC(t.toBtyes()));
        return t;
    }

    /**
     * 登入包
     * @param body
     * @return
     */
    public static Transporter createLogin(CommonBody body) {
        return newInstance(Protocol.CMD.LOGIN.get(),
                Protocol.CMD.NORMAL.get(),
                Config.M.getVin().getBytes(),
                Config.M.getEncrypted().getEncryption(),
                Config.M.getEncrypted().encrypt(body.toBytes()));
    }

    /**
     * 实时信息
     * @param body
     * @return
     */
    public static Transporter createRealTimeInfo(CommonBody body) {
        return newInstance(Protocol.CMD.REALTIME_INFO.get(),
                Protocol.CMD.NORMAL.get(),
                Config.M.getVin().getBytes(),
                Config.M.getEncrypted().getEncryption(),
                Config.M.getEncrypted().encrypt(body.toBytes()));
    }


    public static Transporter newInstance(byte cmd, byte res, byte[] vin, byte encryption, byte[] bodys) {
        Transporter t = Transporter.newInstance(cmd, res, vin, encryption, bodys, (byte)0);
        t.setCheck(Config.M.getEncrypted().checkBCC(t.toBtyes()));
        return t;
    }

    public static Transporter newInstance(byte cmd, byte res, byte[] vin, byte encryption, byte[] bodys, byte check) {
        Transporter t = new Transporter();
        t.setCmd(cmd);
        t.setRes(res);
        t.setVin(vin);
        t.setEncryption(encryption);
        t.setBodys(bodys);
        t.setCheck(check);
        return t;
    }

    public static Transporter create(TbTransport tt) {
        String data = tt.getData();
        byte[] datas = StringUtil.hexStr2Bytes(data);
        ByteBuf out = Unpooled.buffer(datas.length);
        out.writeBytes(datas);

        byte[] vin = new byte[17];
        byte cmd = out.readByte();
        byte res = out.readByte();
        out.readBytes(vin);
        byte encryption = out.readByte();
        short length = out.readShort();
        byte[] bodys = new byte[length];
        out.readBytes(bodys);

        out.clear();
        return newInstance((byte)tt.getCmd(), res, vin, encryption, bodys);
    }
}
