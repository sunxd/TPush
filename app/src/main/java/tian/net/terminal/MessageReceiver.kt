package tian.net.terminal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import tian.net.push.MessageWrap
import tian.net.push.PushManager
import tian.net.terminal.common.Transporter
import tian.net.terminal.model.packet.RawPacket
import tian.net.terminal.util.RxBus
import timber.log.Timber

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/16 上午11:40<br/>
 */
class MessageReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Timber.d("收到消息: " + action)
        when(action) {
            PushManager.RECEIVER_ACTION -> {
                val pp = RawPacket(RawPacket.TYPE.MESSAGE)
                var messageWrap = intent?.getSerializableExtra(PushManager.MESSAGE) as MessageWrap<Transporter>
                pp.transporter = messageWrap.o
                RxBus.getDefault().post(pp)
            }
            PushManager.CONNECTED_ACTION -> { RxBus.getDefault().post(RawPacket(RawPacket.TYPE.CONNECTED)) }
            PushManager.DISCONNECTED_ACTION -> { RxBus.getDefault().post(RawPacket(RawPacket.TYPE.DISCONNECTED)) }
        }
    }


    fun getIntentFilter(): IntentFilter{
        return IntentFilter()
    }
}