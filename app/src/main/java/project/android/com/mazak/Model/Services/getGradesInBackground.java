package project.android.com.mazak.Model.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Entities.getOptions;
import project.android.com.mazak.R;


public class getGradesInBackground extends Service {
    Database db;
    LoginDatabase lgnDB;

    public getGradesInBackground() {
    }

    private void tryGetDatabases() {
        try {
            lgnDB = LoginDatabase.getInstance(this);
            db = Factory.getInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        irurCheckup();
        return super.onStartCommand(intent, flags, startId);
    }

    private void irurCheckup() {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    Log.d("Alarm","Alarm wakeup");
                    boolean flag;
                    try {
                        tryGetDatabases();
/*                        flag = db.ifNewIrurArrived();
                        if (flag) // new irurs arrived
                            sendIrurNotification();*/
                        db.getGrades(getOptions.fromWeb);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //startService(new Intent(getApplicationContext(),startAlarmService.class));
                }
            }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
    }

    private void sendIrurNotification() {

        vibrate();


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.mold_icon)
                        .setContentTitle("Appeals Arrived")
                        .setContentText("Appeals Status has Changed");
        int NOTIFICATION_ID = 1;
        builder.setAutoCancel(true);
        Intent targetIntent = new Intent(this, NavDrawerActivity.class);
        targetIntent.putExtra("fragment","appeals");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0,200,100,200},-1);
    }
}
