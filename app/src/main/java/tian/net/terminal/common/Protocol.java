package tian.net.terminal.common;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午2:48<br/>
 *
 * ************************************************************************************************
 *                                          Protocol
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *       2   │   1   │    1   │    17     │      1      │      2       │                  │   1
 *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
 *           │       │        │           │             │              │                  │
 *  │  MAGIC   Cmd       Res      Vin       Encryption     Body length     Body Content     Check │
 *           │       │        │           │             │              │                  │
 *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * 消息头24个字节定长
 * = 2   // MAGIC = ##
 * + 1   // 消息标志位, 用来表示消息类型
 * + 1   // 应答标识
 * + 17  // 车辆VIN 17 字节
 * + 1   // 加密类型
 * + 2   // 消息体body长度
 * ........
 *
 * + 1   // 校验位，倒数第一个字节
 *
 * ************************************************************************************************
 *
 */

public class Protocol {

    /** 协议头长度 */
    public static final int HEAD_LENGTH = 24;

    /** Magic */
    public static final byte[] MAGIC = "##".getBytes();


    private byte cmd;
    private byte res;
    private byte[] vin;
    private byte encryption;
    private short bodyLength;
    private byte check;


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

    public short getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(short bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte getCheck() {
        return check;
    }

    public void setCheck(byte check) {
        this.check = check;
    }


    /**
     * 命令
     */
    public enum CMD {

        NORMAL((byte)0x01),
        LOGIN((byte)0x01), REALTIME_INFO((byte)0x02), PATCH_INFO((byte)0x03), LOGOUT((byte)0x04),
        HEARTBEAT((byte)0x07),
        VERFIY_TIME((byte)0x08),
        QUERY((byte)0x80), SETTING((byte)0x81), CONTROL((byte)0x82);

        private byte cmd;

        CMD(byte cmd) {
            this.cmd = cmd;
        }

        public byte get() {
            return cmd;
        }
    }

    /**
     * 应答标识
     */
    public enum RES {

        SUCCESS((byte)0x1), ERROR((byte)0x2), VIN_DUPLICATE((byte)0x1), REQUEST((byte)0x4);

        private byte value;

        RES(byte v) { value = v; }

        public byte get() { return value; }
    }
}
