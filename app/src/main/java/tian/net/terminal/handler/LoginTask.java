package tian.net.terminal.handler;

import android.os.Handler;
import android.util.LruCache;

import tian.net.push.PushManager;
import tian.net.terminal.common.Config;
import tian.net.terminal.common.Protocol;
import tian.net.terminal.common.Transporter;
import tian.net.terminal.util.DbUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 下午5:51<br/>
 */

public class LoginTask extends DataTask {

    public LoginTask(Handler handler, LruCache<String, Task> cache, Transporter transporter) {
        super(handler, cache, transporter);
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
            stop();
            PushManager.M.stopPush();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    PushManager.M.startPush();
                }
            }, Config.RELOGIN);
        }

    }
}
