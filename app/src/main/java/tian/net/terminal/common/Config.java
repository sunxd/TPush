package tian.net.terminal.common;

import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

import es.dmoral.prefs.Prefs;
import tian.net.terminal.encrypt.Encrypted;
import tian.net.terminal.util.DateUtil;
import tian.net.terminal.util.StringUtil;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/4 上午9:59<br/>
 */

public class Config {

    //流水号
    private static final String SERIAL_DATA = "SERIAL_DATA";

    //七天有效期
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";

    //缓存数据包
    public static final String  cache_file = "/extsd/tian/net/terminal/";
    public static final long  cache_size = Long.MAX_VALUE;
    public static final int  cache_count = Integer.MAX_VALUE;


    //发送信息相关
    public static final int RETRY_STEP = 60 * 1000;  //重发间隔
    public static final int RETRY_TIMES = 3;         //重发次数
    public static final int RELOGIN = 60 * 1000 * 2; //60分后重新登入

    public static final int QUERY_LIMIT = 60;        //补发数据一次查询个数

    public static final int SEND_RATE = 20 * 1000;   //每20秒发送一次数据


    public static Config M = getInstance();

    private Context context;

    Encrypted encrypted;

    //TODO
    String vin;
    String iccId;

    String invalidDate;


    private static final AtomicInteger SERIAL_ID = new AtomicInteger();


    private static Config getInstance() {
        if(M == null) {
            synchronized (Config.class) {
                if(M == null) {
                    M = new Config();
                }
            }
        }
        return M;
    }

    public void init(Context context) {
        this.context = context;
        encrypted = Encrypted.getInstance(EncryptionType.NORMAL);

        //TODO
        vin = "LSVFA49J232037048";
        iccId = "89860079090449311287";

        /**
         *    SERIAL_DATA = 2017-07-04
         *    2017-07-04  =  serialId: Int
         */
        String serialData = Prefs.with(context).read(SERIAL_DATA);
        String date = DateUtil.getDate();
        if(date.equals(serialData)) {
            int tempId = Prefs.with(context).readInt(serialData, 0);
            SERIAL_ID.set(tempId);
        }else {
            Prefs.with(context).write(SERIAL_DATA, date);
            Prefs.with(context).writeInt(date, 0);
            SERIAL_ID.set(0);
        }

        /**
         *  计算数据失效时间，默认存7天
         *  170706, 170705, ..., 170330,   size = 7
         */
        String dates = Prefs.with(context).read(EFFECTIVE_DATE, " , , , , , , ");
        String[] datesArray = dates.split(",");
        int crtDate = StringUtil.bytesDate2Int(DateUtil.getByteDateTime());
        if(String.valueOf(crtDate).equals(datesArray[0])) {
            invalidDate = datesArray[6];
        }else {
            String temps = crtDate + "," + dates.substring(0, dates.lastIndexOf(","));
            Prefs.with(context).write(EFFECTIVE_DATE, temps);
            invalidDate = temps.split(",")[6];
        }
    }

    public short getAndIncrementSerialId() {
        int id = SERIAL_ID.incrementAndGet();
        Prefs.with(context).writeInt(Prefs.with(context).read(SERIAL_DATA), id);
        return (short)id;
    }

    public short getSerialId() {
        return (short)(SERIAL_ID.get());
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getIccId() {
        return iccId;
    }

    public void setIccId(String iccId) {
        this.iccId = iccId;
    }

    public Encrypted getEncrypted() {
        return encrypted;
    }

    public String getVin() {
        return vin;
    }

    public enum EncryptionType{ NORMAL, RSA, AES, EXP, INVALID}

    public String getInvalidDate() {
        return invalidDate;
    }

    public String getPatchDate() {
        if(StringUtil.isEmpty(invalidDate)) {
            String dates = Prefs.with(context).read(EFFECTIVE_DATE);
            return getInvalidDate(dates.split(","));
        }
        //理论上该值不会被使用
        return "990000";
    }

    private String getInvalidDate(String[] dates) {
        for(int i=dates.length -1; i>=0; i--) {
            if(!StringUtil.isEmpty(dates[i])) {
                return dates[i];
            }
        }
        return dates[0];
    }
}
