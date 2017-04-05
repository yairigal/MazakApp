package project.android.com.mazak.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Yair on 2017-02-20.
 */
public class LoginDatabase {

    String username;
    String password;
    Context ctx;

    private static LoginDatabase ourInstance = null;

    public static LoginDatabase getInstance(Context ctx) {
        if (ourInstance == null)
            ourInstance = new LoginDatabase(ctx);
        return ourInstance;
    }

    private LoginDatabase(Context ctx) {
        this.ctx = ctx;
    }

    public void saveLoginInformation(String us,String pw) throws IOException {
        SharedPreferences sharedPref = ctx.getSharedPreferences("pw", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        username = us; password = pw;
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    public void clearLoginInformation() throws IOException {
        SharedPreferences sharedPref = ctx.getSharedPreferences("pw", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.commit();

        username = "";
        password = "";
    }

/*    private HashMap<String,String> getLoginDataFromDialog(final Delegate onDialogClosed) {
        final HashMap<String,String> data = new HashMap<>();

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.login_dialog);
        (dialog.findViewById(R.id.loginDialogBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) dialog.findViewById(R.id.usernameDialogET)).getText().toString();
                password = ((EditText) dialog.findViewById(R.id.passwordDialogET)).getText().toString();
                if (username.equals("") || password.equals(""))
                    Toast.makeText(context, "Please check your entries", Toast.LENGTH_LONG).show();
                else {
                    data.put("username",username);
                    data.put("password",password);
                    dialog.dismiss();
                    onDialogClosed.function(null);
                }
            }
        });
        dialog.show();
        return data;
    }*/

    public boolean dataIsSaved() {
        try {
            getLoginDataFromMemory();
        } catch (IOException e) {
            return false;
        }
        return !(username.equals("") || password.equals(""));
    }

    public HashMap<String,String> getLoginDataFromMemory() throws IOException {
        HashMap<String,String> map = new HashMap<>();
        SharedPreferences sharedPref = ctx.getSharedPreferences("pw", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "");
        password = sharedPref.getString("password", "");
        map.put("username",username);
        map.put("password",password);
        return map;
    }
}
