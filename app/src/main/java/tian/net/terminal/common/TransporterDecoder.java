package tian.net.terminal.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import tian.net.terminal.util.StringUtil;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/3 下午4:28<br/>
 */

public class TransporterDecoder extends ReplayingDecoder<TransporterDecoder.State> {

    private final Protocol protocol = new Protocol();
    private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

    public TransporterDecoder() {
        super(State.MAGIC);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case MAGIC:
                byte[] magic = new byte[2];
                in.readBytes(magic);
                checkMagic(magic);
                checkpoint(State.CMD);
            case CMD:
                protocol.setCmd(in.readByte());
                checkpoint(State.RES);
            case RES:
                protocol.setRes(in.readByte());
                checkpoint(State.VIN);
            case VIN:
                byte[] vin = new byte[17];
                in.readBytes(vin);
                protocol.setVin(vin);
                checkpoint(State.ENCRYPTION);
            case ENCRYPTION:
                protocol.setEncryption(in.readByte());
                checkpoint(State.BODYLENGTH);
            case BODYLENGTH:
                protocol.setBodyLength(in.readShort());
                checkpoint(State.BODY);
            case BODY:
                int bodyLength = checkBodyLength(protocol.getBodyLength());
                byte[] bytes = new byte[bodyLength];
                in.readBytes(bytes);
                Transporter transporter = Transporter.newInstance(protocol.getCmd(), protocol.getRes(), protocol.getVin(),
                        protocol.getEncryption(), bytes, in.readByte());
                out.add(transporter);
                Timber.d("Decode = " + StringUtil.byte2HexStr(transporter.toBtyes()));
                break;
            //case CHECK:

            default:
                break;
        }
        checkpoint(State.MAGIC);
    }

    private int checkBodyLength(int bodyLength) throws Exception {
        if (bodyLength > MAX_BODY_SIZE) {
            throw new Exception("body of request is bigger than limit value "+ MAX_BODY_SIZE);
        }
        return bodyLength;
    }

    private void checkMagic(byte[] magic) throws Exception {
        if (Protocol.MAGIC.length != magic.length
                || Protocol.MAGIC[0] != magic[0]
                || Protocol.MAGIC[1] != magic[1]) {
            throw new Exception("magic value is not equal " + magic);
        }
    }


    enum State {
        MAGIC, CMD, RES, VIN, ENCRYPTION, BODYLENGTH, BODY, CHECK
    }
}
