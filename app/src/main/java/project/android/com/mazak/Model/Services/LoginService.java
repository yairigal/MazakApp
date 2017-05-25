package project.android.com.mazak.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import project.android.com.mazak.Controller.Login.LoginActivity;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Database.LoginDatabase;

public class LoginService extends Service {
    LoginDatabase db;
    public LoginService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = LoginDatabase.getInstance(this);
        String time = Factory.getInstance(this).getUpdateTime(InternalDatabase.gradesKey);
        if(!db.dataIsSaved()||time == null || time.equals("")){
            Intent Login = new Intent(LoginService.this, LoginActivity.class);
            Login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Login);
        }
        this.stopSelf();
        return START_NOT_STICKY;
    }
}
