package project.android.com.mazak.Model.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import project.android.com.mazak.BuildConfig;
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

    /**
     * checks if the login data is saved and if its the user second time if not , back to the login page.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String time = "";
        db = LoginDatabase.getInstance(this);
        try {
            time = Factory.getInstance(this).getUpdateTime(InternalDatabase.gradesKey);
        } catch (Exception ignored) {
        }
        if (!db.dataIsSaved()) {
            Intent Login = new Intent(LoginService.this, LoginActivity.class);
            Login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Login);
        } else { // data is saved.
            int dataVersion=0;
            try {
                SharedPreferences sharedPref = getSharedPreferences("version", Context.MODE_PRIVATE);
                dataVersion = sharedPref.getInt("version", 0);
            } catch (Exception ignored) {
            } finally {
                if (dataVersion < BuildConfig.VERSION_CODE) {
                    Factory.getInstance(this).clearDatabase();
                }
                SharedPreferences sharedPref = getSharedPreferences("version", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("version",BuildConfig.VERSION_CODE);
                editor.apply();
            }

        }
        this.stopSelf();
        return START_NOT_STICKY;
    }
}
