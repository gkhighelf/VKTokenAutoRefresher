package org.gkhighelf.vktokenrefresher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.gkhighelf.vktokenrefresher.VKTokenRefresherApplication;

/**
 * Created by gkhighelf on 14.11.14.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            VKTokenRefresherApplication.InitAlarm();
        }
    }
}