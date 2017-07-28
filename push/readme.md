## 介绍
#### 该库基于Netty实现，主要功能为建立并保持与服务端的双向通信。

## 用途
#### 主要应用于那些需要与服务端基于长连接通信的应用，已实现了包括心跳，断线重连接等功能，解放你的大脑，让你更加专注于业务。

## 配置文件
#### 该组件基本assets目录下的config.json工作：
```json
{
  "host": "192.168.1.73",		//服务器地址
  "port": "8080",				//端口

  "heartbeat": {
    "step": "60",				//心跳间隔
    "mode": "alarm",			//目前支持netty/alarm
    "ping": "1",				//心跳指令，Test用
    "pone": "-1"				//心跳响应指令，Test用
  },

  "times": [
    2, 4, 8, 16, 32, 64, 128, 256, 512, 1024
  ]
	//重连间隔
}

```

###### 关于心跳的mode:
1. netty: 基于Netty的心跳，系统休眠时会断开连接
2. alarm: 基于Alarm的心跳，系统休眠时会唤醒系统以保持心跳

###### 关于times:
在连接断开后，服务会根据times的值，分别间隔times[i]进行重连

#### AndroidManifest.xml中注册服务
```xml
<service android:name="tian.net.push.android.PushService">
    <intent-filter>
        <action android:name="tian.net.push.PUSH_SERVICE"/>
    </intent-filter>
</service>
```


##使用

```kotlin
//初始化，一般放到Application中
PushManager.M.setCustomeHandlerProvider{ arrayOf(StringDecoder(), StringEncoder())}
                .setHeartbeatEntity(heartbeat)
                .init(this)
                
//启动                
PushManager.M.startPush()


//接收消息, 动态注册receiver来监听消息
val lbm = LocalBroadcastManager.getInstance(this)
val iftr  = IntentFilter()
iftr.addAction(PushManager.RECEIVER_ACTION)
iftr.addAction(PushManager.CONNECTED_ACTION)
iftr.addAction(PushManager.DISCONNECTED_ACTION)
lbm.registerReceiver(MessageReceiver(), iftr)

//Receiver
class MessageReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Timber.d("收到消息: " + action)
        when(action) {
            PushManager.RECEIVER_ACTION -> {
            	//收到服务端消息, MessageWrap有成员变量o, o即为收到的消息，其中泛型T为o的实际类型。
                var messageWrap = intent?
                	.getSerializableExtra(PushManager.MESSAGE) as MessageWrap<T>
                	Timber.d("消息：" + messageWrap.o.toString())
                }
            PushManager.CONNECTED_ACTION -> { //连接建立 }
            PushManager.DISCONNECTED_ACTION -> { //连接断开 }
        }
    }
    
}

//当连接建立后，调用send(Object o)发送消息
PushManager.M.send("hello server");

//备注：
1. setCustomeHandlerProvider(CustomHandlerProvider provider)
	//provider
	public interface CustomHandlerProvider {
	
	    ChannelHandler[] handlers();
	}
	
	该接口返回继承了ChannelHandler的Handler数组, 其中主要包括通信协议的encoder和decoder, 方便用户自定义协定


2. setHeartbeatEntity(Object heartbeat) 若心跳包也是自定义的话，需要通过该方法进行设置.
	若不设置，心跳将默认发送config.json中的ping字段的指令。

```

