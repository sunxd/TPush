package tian.net.terminal.handler;

import android.os.Handler;
import android.util.LruCache;

import tian.net.push.PushManager;
import tian.net.terminal.common.Config;
import tian.net.terminal.common.Protocol;
import tian.net.terminal.common.Transporter;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.util.DbUtil;
import tian.net.terminal.util.StringUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 下午5:51<br/>
 */

public class DataTask implements Runnable, Task {

    Handler handler;
    Transporter transporter;
    int times;
    TbTransport tbTransport;
    LruCache<String, Task> cache;

    public DataTask(Handler handler, LruCache<String, Task> cache, Transporter transporter) {
        this.handler = handler;
        this.transporter = transporter;
        this.cache = cache;
        cache.put(transporter.getTraceId(), this);

        tbTransport = new TbTransport();
        tbTransport.setCmd(transporter.getCmd());
        tbTransport.setData(StringUtil.byte2HexStr(transporter.toBtyes()));
        tbTransport.setDateTime(StringUtil.bytesDate2Str(transporter.getBodys()));
        tbTransport.setDate(StringUtil.bytesDate2Int(transporter.getBodys()));
    }

    @Override
    public void start() {
        DbUtil.insert(tbTransport);
        handler.post(this);
    }

    @Override
    public void success() {
        stop();
        tbTransport.setState(Protocol.RES.SUCCESS.get());
        DbUtil.update(tbTransport);
    }


    @Override
    public void stop() {
        cache.remove(transporter.getTraceId());
        handler.removeCallbacks(this);
    }

    @Override
    public String getId() {
        return transporter.getTraceId();
    }

    @Override
    public void run() {
        if(times < Config.RETRY_TIMES) {
            times ++;
            tbTransport.setState(Protocol.RES.ERROR.get());
            tbTransport.setTimes(times);
            DbUtil.update(tbTransport);
            PushManager.M.send(transporter);
            handler.postDelayed(this, Config.RETRY_STEP);
        }else {
            //发送三次仍失败
            stop();
        }
    }
}
