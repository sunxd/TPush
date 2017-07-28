package tian.net.push;

import java.io.Serializable;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/22 上午10:56<br/>
 */

public class MessageWrap<T> implements Serializable{

    T o;

    public T getO() {
        return o;
    }

    public void setO(T o) {
        this.o = o;
    }
}
