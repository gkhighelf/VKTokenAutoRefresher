package org.gkhighelf.vktokenrefresher;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import org.gkhighelf.vktokenrefresher.receivers.AlarmFireReceiver;

/**
 * Created by gkhighelf on 17.11.14.
 */
public class VKTokenRefresherApplication extends Application {
    private static final String LOG_KEY = "VKTokenRefresherApplication";
    private static VKTokenRefresherApplication _instance = null;
    private boolean _started_manually = false;
    private static boolean _alarm_was_initialized = false;

    private static PendingIntent getAlarmReceiverPendingIntent(Context context) {
        Intent RefreshVkTokenintent = new Intent(context, AlarmFireReceiver.class);
        return PendingIntent.getBroadcast(context, 0, RefreshVkTokenintent, 0);
    }

    public static void InitAlarm() {
        Log.i(LOG_KEY, "InitAlarm");
        Context context = instance().getApplicationContext();
        long update_interval = Long.parseLong(instance().getPreferences().
                getString(SettingsActivity.KEY_TOKEN_UPDATE_INTERVAL, "360"))
                * 60 * 1000; // Defaults.UPDATE_INTERVAL * 60 * 1000;
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getAlarmReceiverPendingIntent(context);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, update_interval, update_interval, alarmIntent);
    }

//    public static void DeinitAlarm(Context context) {
//        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent alarmIntent = getAlarmReceiverPendingIntent(context);
//        if (alarmMgr!=null) {
//            alarmMgr.cancel(alarmIntent);
//        }
//    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VKTokenRefresherApplication._instance = this;
        InitAlarm();
    }

    public static VKTokenRefresherApplication instance() {
        return VKTokenRefresherApplication._instance;
    }

    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
    }

    public void setActivityStartedManually(boolean value) {
        _started_manually = value;
    }

    public boolean isActivityStartedManually() {
        return _started_manually;
    }
}
