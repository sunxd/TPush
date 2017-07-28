package tian.net.terminal.handler;


/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 下午5:33<br/>
 */

public interface Task{

    void start();

    void success();

    void stop();

    String getId();
}
