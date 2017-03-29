package project.android.com.mazak.Model.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                ||
                intent.getAction().equals("action.APP_KILLED")) {
            context.startService(new Intent(context,startAlarmService.class));
        }
    }
}
