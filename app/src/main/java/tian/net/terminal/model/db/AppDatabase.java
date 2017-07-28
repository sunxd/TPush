package tian.net.terminal.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/7/6 下午2:30<br/>
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase"; // we will add the .db extension

    public static final int VERSION = 1;
}
