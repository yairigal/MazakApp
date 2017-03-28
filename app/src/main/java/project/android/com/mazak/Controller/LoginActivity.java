package project.android.com.mazak.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;

import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText PasswordET;
    Button loginBtn;
    Activity ctx;

    LoginDatabase database;
    String Username, Password;
    Database db;
    boolean toStart = false;
    HashMap<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AssetManager mgr = getAssets();
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/lvnm.ttf");
        ((TextView)findViewById(R.id.mainText)).setTypeface(typeFace);


        ctx = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            toStart = bundle.getBoolean("new");

        usernameET = (EditText) findViewById(R.id.editText);
        PasswordET = (EditText) findViewById(R.id.editText2);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = usernameET.getText().toString();
                String pw = PasswordET.getText().toString();
                if (un.equals("") || pw.equals("")) {
                    Toast.makeText(getApplicationContext(), "No empty text!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Username = un;
                Password = pw;
                try {
                    db = Factory.getInstance(Username,Password,ctx);
                    db.clearDatabase();
                    database.clearLoginInformation();
                    database.saveLoginInformation(Username, Password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sentIntent(true);
            }
        });

        database = LoginDatabase.getInstance(ctx);
        if (database.dataIsSaved() && !toStart) {
            sentIntent(false);
        }
    }

    void sentIntent(boolean refresh) {
        Intent intent = new Intent(LoginActivity.this, NavDrawerActivity.class);
        intent.putExtra("refresh", refresh);
        startActivity(intent);
    }
}
