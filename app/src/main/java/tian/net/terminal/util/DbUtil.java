package tian.net.terminal.util;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import tian.net.terminal.common.Config;
import tian.net.terminal.common.Protocol;
import tian.net.terminal.model.db.TbTransport;
import tian.net.terminal.model.db.TbTransport_Table;
import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/5 下午6:33<br/>
 */

public class DbUtil {


    public static void insert(TbTransport tbTransport) {
        tbTransport.insert();
        Timber.d("Insert = " + tbTransport.toString());
    }

    public static void update(TbTransport tbTransport) {
        tbTransport.update();
        Timber.w("Update = " + tbTransport.toString());
    }

    /**
     * 查询补发数据
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<TbTransport> getTbTransport(int startTime, int endTime) {
        // 如果是同一天，则开始时间减1
        if(startTime == endTime) startTime = startTime -1;
        List<TbTransport> list = SQLite.select()
                .from(TbTransport.class)
                .where(TbTransport_Table.state.eq((int)Protocol.RES.ERROR.get()))
                //  cmd = 0x2, 0x3
                .and(TbTransport_Table.cmd.eq((int)Protocol.CMD.REALTIME_INFO.get()))
                .or(TbTransport_Table.cmd.eq((int)Protocol.CMD.PATCH_INFO.get()))
                //日期
                .and(TbTransport_Table.date.greaterThan(startTime))
                .and(TbTransport_Table.date.lessThanOrEq(endTime))
                .limit(Config.QUERY_LIMIT)
                .offset(0)
                .queryList();
        return list;
    }

    /**
     * 删除失效数据
     * @param validDate
     */
    public static void deleteInvalidData(int validDate) {
        SQLite.delete()
                .from(TbTransport.class)
                .where(TbTransport_Table.date.lessThan(validDate));
    }
}
