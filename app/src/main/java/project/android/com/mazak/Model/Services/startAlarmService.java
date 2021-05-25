package project.android.com.mazak.Model.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by Yair on 2017-03-11.
 */

public class startAlarmService extends Service {

    Alarm alarm = new Alarm();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        alarm.setAlarm(this);
        return super.startService(service);
    }
}
