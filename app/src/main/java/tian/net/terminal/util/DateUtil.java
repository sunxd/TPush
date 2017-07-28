package tian.net.terminal.util;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.Operator;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tian.net.terminal.common.Config;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.model.db.TbTransport_Table;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/4 下午2:38<br/>
 */

public class DateUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");

    public static String getDate() {
        return dateFormat.format(new Date());
    }

    public static byte[] getByteDateTime() {
        byte[] dt = new byte[6];
        String[] dateTime = dateTimeFormat.format(new Date()).split("-");
        for(int i=0; i<dt.length; i++) {
            dt[i] = (byte)(StringUtil.parseInt(dateTime[i]));
        }
        return dt;
    }


}

