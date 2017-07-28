package tian.net.terminal

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.LruCache
import io.reactivex.disposables.Disposable
import tian.net.terminal.common.Config
import tian.net.terminal.common.Protocol
import tian.net.terminal.common.Transporter
import tian.net.terminal.handler.DataTask
import tian.net.terminal.handler.LoginTask
import tian.net.terminal.handler.PatchThread
import tian.net.terminal.handler.Task
import tian.net.terminal.model.Login
import tian.net.terminal.model.RealTimeInfo

import tian.net.terminal.model.packet.RawPacket
import tian.net.terminal.util.DbUtil
import tian.net.terminal.util.RxBus
import tian.net.terminal.util.StringUtil
import timber.log.Timber


/**
 * <br></br>
 * 孙晓达<br></br>
 * sunxd14@gmail.com<br></br>
 * 2017/7/4 下午4:25<br></br>
 */

class TerminalService : Service() {


    val handler: Handler = Handler()
    val lbm = LocalBroadcastManager.getInstance(this)
    val receiver = MessageReceiver()

    var disposable: Disposable? = null


    val lruCache :LruCache<String, Task> = LruCache(180)


    var patchThread: PatchThread? = null
    val patchCache :LruCache<String, Task> = LruCache(Config.QUERY_LIMIT)

    override fun onCreate() {
        super.onCreate()

        lbm.registerReceiver(receiver, receiver.getIntentFilter())
        disposable = RxBus.getDefault().toObservable(RawPacket::class.java)
                .subscribe { rp -> handle(rp) }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        lbm.unregisterReceiver(receiver)
        disposable?.dispose()
        handler.removeCallbacksAndMessages(null)
        patchThread?.doStop()
    }



    /**
     * 补发数据
     */
    fun patchData() {
        val invalidDate = Config.M.invalidDate
        //删除过期数据
        if (!StringUtil.isEmpty(invalidDate)) {
            DbUtil.deleteInvalidData(invalidDate.toInt())
        }
        //启动补发线程
        patchThread = PatchThread(Config.M.patchDate.toInt(), patchCache)
        patchThread?.start()
    }

    /**
     * 时实数据
     */
    fun realTimeData() {
        //TODO 数据需要采集
        var byteArray = byteArrayOf(1)
        DataTask(handler, lruCache,
                Transporter.createRealTimeInfo(RealTimeInfo.create(byteArray))).start()
    }


    /**
     * 处理数据
     */
    fun handle(rp :RawPacket) {
        when(rp.type) {
            RawPacket.TYPE.CONNECTED -> { handleConnected(rp) }
            RawPacket.TYPE.MESSAGE -> { handleMeesage(rp) }
            RawPacket.TYPE.DISCONNECTED -> { handleDisconnected(rp) }
        }
    }

    /**
     * 连接成功
     */
    fun handleConnected(rawPacket :RawPacket) {
        handler.removeCallbacksAndMessages(null)
        //发送登入信息

        LoginTask(handler, lruCache,
                Transporter.createLogin(
                        Login.create(1.toByte(), 1.toByte(), byteArrayOf(1)))).start()
    }

    /**
     * 收到数据
     */
    fun handleMeesage(rawPacket :RawPacket) {
        val transporter = rawPacket.transporter
        if(transporter.cmd == Protocol.CMD.HEARTBEAT.get()) {
            Timber.d("收到服务器心跳反馈")
        }else {
            when(transporter.res) {
                Protocol.RES.SUCCESS.get() -> {
                    if(transporter.cmd == Protocol.CMD.LOGIN.get()) {
                        patchData()
                    }else if(transporter.cmd == Protocol.CMD.PATCH_INFO.get()) {
                        patchCache.get(transporter.traceId).success()
                    }
                    lruCache.get(transporter.traceId).success()
                }
                Protocol.RES.ERROR.get() -> {}
                Protocol.RES.VIN_DUPLICATE.get() -> {}
                Protocol.RES.REQUEST.get() -> {
                    //服务器发送的命令

                }
            }
        }
    }

    /**
     * 连接断开
     */
    fun handleDisconnected(rawPacket :RawPacket) {

    }

}
