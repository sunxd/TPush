package tian.net.terminal.model.packet;

import org.reactivestreams.Subscription;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tian.net.terminal.common.Transporter;
import tian.net.terminal.util.RxBus;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/4 下午4:49<br/>
 */

public class RawPacket {

    TYPE type;
    Transporter transporter;

    public RawPacket(TYPE type) {
        this.type = type;
        Disposable subscription = RxBus.getDefault().toObservable(RawPacket.class).subscribe(new Consumer<RawPacket>() {
            @Override
            public void accept(@NonNull RawPacket rawPacket) throws Exception {

            }
        });
        subscription.dispose();
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE { CONNECTED, DISCONNECTED, MESSAGE}
}
