package tian.net.terminal.handler;


import android.util.LruCache;

import tian.net.push.PushManager;
import tian.net.terminal.common.Protocol;
import tian.net.terminal.common.Transporter;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.util.DbUtil;
import tian.net.terminal.util.StringUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/7 上午11:21<br/>
 */

public class PatchTask implements Runnable, Task {


    TbTransport tbTransport;
    Transporter transporter;
    LruCache<String, Task> cache;


    public PatchTask(TbTransport tt, Transporter tspt, LruCache<String, Task> cache) {
        tbTransport = tt;
        transporter = tspt;
        this.cache = cache;
    }


    @Override
    public void run() {
        //更新数据库信息
        tbTransport.setTimes(tbTransport.getTimes() + 1);
        tbTransport.setData(StringUtil.byte2HexStr(transporter.toBtyes()));
        DbUtil.update(tbTransport);
        //发送数据
        PushManager.M.send(transporter);
    }

    @Override
    public void start() {
    }

    @Override
    public void success() {
        cache.remove(transporter.getTraceId());
        tbTransport.setState(Protocol.RES.SUCCESS.get());
        DbUtil.update(tbTransport);
    }

    @Override
    public void stop() {
    }

    @Override
    public String getId() {
        return null;
    }
}
