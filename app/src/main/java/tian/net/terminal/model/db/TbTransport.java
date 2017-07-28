package tian.net.terminal.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.UUID;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 下午6:40<br/>
 */
@Table(database = AppDatabase.class)
public class TbTransport extends BaseModel{

    @PrimaryKey
    UUID id = UUID.randomUUID();

    @Column
    int cmd;

    /**
     * bytes2hexstr
     */
    @Column
    String data;

    @Column
    int state;

    @Column
    int times;

    @Column
    String dateTime;

    /**
     * 用来标记时间，
     * 超过7天的数据会直接删除
     */
    @Column
    int date;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TbTransport{" +
                "id=" + id +
                ", cmd=" + cmd +
                ", data='" + data + '\'' +
                ", state=" + state +
                ", times=" + times +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
