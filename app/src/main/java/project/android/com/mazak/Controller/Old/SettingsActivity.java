package project.android.com.mazak.Controller.Old;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import project.android.com.mazak.Controller.Login.LoginActivity;
import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.R;

public class SettingsActivity extends AppCompatActivity {

    private static String username = "";
    private static String password = "";

    LoginDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = LoginDatabase.getInstance(this);
        setContentView(R.layout.activity_settings);
        CardView card = (CardView) findViewById(R.id.loginCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("new",true);
                intent.putExtras(b);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
                startActivity(intent);
            }
        });
        if(getIntentInfomration())
            Toast.makeText(this, "Please Insert your username and password", Toast.LENGTH_LONG).show();
        //getSP();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,NavDrawerActivity.class));
        finish();
    }

    private boolean getIntentInfomration() {
        try {
            return getIntent().getBooleanExtra("toast", false);
        } catch (Exception ex) {
            return false;
        }

    }
}
