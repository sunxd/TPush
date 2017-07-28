package tian.net.push.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

/**
 * <br/>
 * 孙晓达<br/>
 * sunxd14@gmail.com<br/>
 * 2017/6/14 下午4:24<br/>
 */

public class Utils {

    /**
     * 字节流转换成字符串
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 判断有无网络
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    // 开启定时服务
    public static void startBroadcast(Context context, int seconds, String action) {
        // 获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //最小间隔9分钟
        //long minDelay = 1 * 60 * 1000;
        long delay = seconds * 1000;
        /*if(delay < minDelay) {
            delay = minDelay + 1000;
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pendingIntent);
            /*manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent);
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + delay, pendingIntent);*/
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pendingIntent);
            /*manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
            manager.setExact(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent);
            manager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + delay, pendingIntent);*/
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pendingIntent);
            /*manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent);
            manager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + delay, pendingIntent);*/
        }
        Timber.d("Alarm set...");
    }

    // 停止定时服务
    public static void stopBroadcast(Context context, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // 取消
        manager.cancel(pendingIntent);
        Timber.d("Alarm cancel...");
    }
}
