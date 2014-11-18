package org.gkhighelf.vktokenrefresher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.gkhighelf.vktokenrefresher.RefreshVkTokenActivity;
import org.gkhighelf.vktokenrefresher.SettingsActivity;
import org.gkhighelf.vktokenrefresher.VKTokenRefresherApplication;

/**
 * Created by gkhighelf on 17.11.14.
 */
public class AlarmFireReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if(VKTokenRefresherApplication.instance().getPreferences().getBoolean(SettingsActivity.KEY_TOKEN_UPDATE_ENABLED, false)) {
                Intent newIntent = new Intent(context, RefreshVkTokenActivity.class);
                newIntent.putExtra("started_by_alarm_manager", true);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
