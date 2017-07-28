package tian.net.terminal.handler;

import android.util.LruCache;

import java.util.List;

import tian.net.terminal.common.Config;
import tian.net.terminal.common.Protocol;
import tian.net.terminal.common.Transporter;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.util.DateUtil;
import tian.net.terminal.util.DbUtil;
import tian.net.terminal.util.StringUtil;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/7 下午3:20<br/>
 */

public class PatchThread extends Thread {


    int invalidDate;
    List<TbTransport> list;
    LruCache<String, Task> cache;
    boolean flag;

    public PatchThread(int invalidDate, LruCache<String, Task> cache) {
        this.invalidDate = invalidDate;
        this.cache = cache;
        flag = true;
    }

    @Override
    public void run() {
        while (flag) {
            if(list == null || list.isEmpty()) {
                list = getList();
                if(list == null || list.isEmpty()) {
                    Timber.d("没有待补发数据: " + toString());
                    break;
                }
            }
            TbTransport tt = list.remove(0);
            tt.setCmd(Protocol.CMD.PATCH_INFO.get()); //更新命令为补发 标识
            Transporter tspt = Transporter.create(tt);
            PatchTask patchTask = new PatchTask(tt, tspt, cache);
            cache.put(tspt.getTraceId(), patchTask);
            patchTask.run();

            doSleep(Config.SEND_RATE / 3);
        }
    }

    public void doStop() {
        flag = false;
        stop();
    }

    private int getCrtDate() {
        return StringUtil.bytesDate2Int(DateUtil.getByteDateTime());
    }

    private  List<TbTransport> getList() {
        return DbUtil.getTbTransport(invalidDate, getCrtDate());
    }

    private void doSleep(int s) {
        try {
            //补发频率为正常发送频率的3倍
            sleep(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String str = "日期 = " + invalidDate + "  缓存 = " + cache.size();
        return str;
    }
}
