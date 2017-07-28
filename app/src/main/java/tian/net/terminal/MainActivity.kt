package tian.net.terminal

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.widget.EditText
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

import tian.net.push.PushManager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*val heartbeat = Protocol()
        heartbeat.cmd = 1
        heartbeat.echo = -1
        heartbeat.body = "heartbeat"
        heartbeat.bodyLength = heartbeat.body!!.toByteArray().size*/


        /*PushManager.M.setCustomeHandlerProvider{ arrayOf(TransporterDecoder(), TransporterEncoder())}
                .setHeartbeatEntity(heartbeat)
                .init(this)
        PushManager.M.startPush()*/
        /*PushManager.M.setCustomeHandlerProvider{ arrayOf(StringDecoder(), StringEncoder())}
                .setHeartbeatEntity(heartbeat)
                .init(this)
        PushManager.M.startPush()*/



        val lbm = LocalBroadcastManager.getInstance(this)
        val iftr  = IntentFilter()
        iftr.addAction(PushManager.RECEIVER_ACTION)
        iftr.addAction(PushManager.CONNECTED_ACTION)
        iftr.addAction(PushManager.DISCONNECTED_ACTION)
        lbm.registerReceiver(MessageReceiver(), iftr)




        findViewById(R.id.btnSbmt).setOnClickListener {
            /*val txt = (findViewById(R.id.edtPushTxt) as EditText).text.toString()
            val msg = Protocol()
            msg.cmd = 2
            msg.echo = -2
            msg.body = txt
            msg.bodyLength = heartbeat.body!!.toByteArray().size
            PushManager.M.send(msg)*/
        }


        startService(Intent(this, TerminalService::class.java))
        PushManager.M.startPush()
    }


    override fun onDestroy() {
        super.onDestroy()
        PushManager.M.stopPush()
        stopService(Intent(this, TerminalService::class.java))
    }

}
