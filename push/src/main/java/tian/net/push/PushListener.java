package tian.net.push;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/15 下午5:50<br/>
 */

public interface PushListener {

    void receiver(Object message);

    void onConnected();

    void onDisConnected();
}
