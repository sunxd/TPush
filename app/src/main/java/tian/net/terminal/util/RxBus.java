package tian.net.terminal.util;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.reactivex.subjects.*;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/13 下午7:04<br/>
 */

public class RxBus {

    private static volatile RxBus defaultInstance;

    private final Subject<Object> bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = PublishSubject.create().toSerialized();
    }
    // 单例RxBus
    public static RxBus getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance ;
    }
    // 发送一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable (Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
