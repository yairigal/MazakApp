package project.android.com.mazak.Controller.Login;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

import project.android.com.mazak.Controller.Constants;
import project.android.com.mazak.Controller.GradesView.FatherTab;
import project.android.com.mazak.Controller.NavDrawerActivity;
import project.android.com.mazak.Database.Database;
import project.android.com.mazak.Database.Factory;
import project.android.com.mazak.Database.InternalDatabase;
import project.android.com.mazak.Database.LoginDatabase;
import project.android.com.mazak.Model.Entities.LoginException;
import project.android.com.mazak.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText PasswordET;
    Button loginBtn;
    Activity ctx;
    static String errormsg = "Error";

    LoginDatabase database;
    Database db;
    final Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFont();

        clearDatabases();

        ctx = this;
        usernameET = (EditText) findViewById(R.id.editText);
        PasswordET = (EditText) findViewById(R.id.editText2);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(onClickLoginBtn());
    }

    /**
     * sets the login font.
     */
    private void setFont() {
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/lvnm.ttf");
        ((TextView)findViewById(R.id.mainText)).setTypeface(typeFace);
    }

    /**
     * This function is called when the login button is clicked.
     * @return
     */
    private View.OnClickListener onClickLoginBtn(){
        return new View.OnClickListener() {
            /**
             * gets the username and password,
             * checks that are both no empty.
             * gets the update time.
             * and tries to login.
             * @param v
             */
            @Override
            public void onClick(View v) {
                final boolean[] auth = {false};
                final String un = usernameET.getText().toString().trim();
                final String pw = PasswordET.getText().toString().trim();

                if (un.equals("") || pw.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.missing_fields), Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = db.getUpdateTime(InternalDatabase.gradesKey);
                Login(auth, un, pw);

/*                Username = un;
                Password = pw;
                try {
                    db = Factory.getInstance(Username,Password,ctx);
                    db.clearDatabase();
                    database.clearLoginInformation();
                    database.saveLoginInformation(Username, Password);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }

            /**
             * check the username and password with the server
             * if its authenticated you save it in the device and moves to the next activity.
             * else you delete the entire login database and throws an exception.
             * @param auth
             * @param un
             * @param pw
             */
            private void Login(final boolean[] auth, final String un, final String pw) {
                new AsyncTask<Void,Void,Void>(){
                    ProgressDialog dialog = new ProgressDialog(act);

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog.setTitle("Logging in...");
                        dialog.setMessage("Authenticating credentials");
                        dialog.setCancelable(false);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        auth[0] = authenticate(un,pw,getApplicationContext());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        dialog.dismiss();
                        if (auth[0]) {
                            try {
                                database.saveLoginInformation(un,pw);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sentIntent(true);
                        }
                        else {
                            try {
                                database.clearLoginInformation();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                        }

                    }
                }.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR );
            }
        };
    }

    /**
     * deletes the login database and the data database.
     */
    private void clearDatabases() {
        try {
            database = LoginDatabase.getInstance(getApplicationContext());
            db = Factory.getInstance(ctx);
            database.clearLoginInformation();
            db.clearDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sends the data to the next activity and moves to there.
     * @param refresh
     */
    void sentIntent(boolean refresh) {
        Intent intent = new Intent(LoginActivity.this, NavDrawerActivity.class);
        intent.putExtra("refresh", refresh);
        startActivity(intent);
    }

    /**
     * Checks if the username and password are correct with the server.
     * first it saves the username and password , then access the grades if it succeeded then the username and password are correct
     * else they are not correct and an error is printed.
     * @param username
     * @param password
     * @param ctx
     * @return
     */
    public static boolean authenticate(String username , String password, Context ctx){
        try {
            if(!FatherTab.isNetworkAvailable(ctx))
                throw new NetworkErrorException();

            LoginDatabase.getInstance(ctx).saveLoginInformation(username,password);
            Factory.getInstance(ctx).tryLogin();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            errormsg = Constants.checkErrorTypeAndMessage(ex, ctx);
            return false;
        }
    }



    public void onBotClicked(View view) {
        Intent telegram = new Intent(Intent.ACTION_VIEW);
        telegram.setData(Uri.parse(getString(R.string.bot_link)));
        startActivity(telegram);
    }
}
