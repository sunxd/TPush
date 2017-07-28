package tian.net.terminal

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager
import tian.net.push.PushManager

import tian.net.terminal.common.Config
import tian.net.terminal.common.Transporter
import tian.net.terminal.common.TransporterDecoder
import tian.net.terminal.common.TransporterEncoder
import tian.net.terminal.util.CacheUtils
import timber.log.Timber
import java.io.File

/**
 * <br></br>
 * 孙晓达<br></br>
 * sunxd14@gmail.com<br></br>
 * 2017/7/4 上午10:27<br></br>
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //日志
        Timber.plant(Timber.DebugTree())

        //数据库
        FlowManager.init(this)

        //配置
        Config.M.init(this)

        //数据包缓存
        //CacheUtils.init(File(Config.cache_file), Config.cache_size, Config.cache_count)

        //通信
        PushManager.M.setCustomeHandlerProvider{ arrayOf(TransporterDecoder(), TransporterEncoder())}
                .setHeartbeatEntity(Transporter.createHeartBeat())
                .init(this)


    }
}
