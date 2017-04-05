package project.android.com.mazak.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import project.android.com.mazak.Controller.Login.LoginActivity;
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
        if(!db.dataIsSaved()){
            Intent Login = new Intent(LoginService.this, LoginActivity.class);
            Login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Login);
        }
        return START_STICKY;
    }
}
