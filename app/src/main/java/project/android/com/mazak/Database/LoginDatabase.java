package project.android.com.mazak.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.IOException;
import java.util.HashMap;

import project.android.com.mazak.R;

/**
 * Created by Yair on 2017-02-20.
 */
public class LoginDatabase {

    String username;
    String password;
    Context ctx;

    String pw = "pw";

    private static LoginDatabase ourInstance = null;

    public static LoginDatabase getInstance(Context ctx) {
        if (ourInstance == null) {
            ourInstance = new LoginDatabase(ctx);
        }
        return ourInstance;
    }

    private LoginDatabase(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * saves the username and password in the device memory.
     *
     * @param us
     * @param pw
     * @throws IOException
     */
    public void saveLoginInformation(String us, String pw) throws IOException {
        SharedPreferences sharedPref = ctx.getSharedPreferences(pw, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        username = us;
        password = pw;

        editor.putString(ctx.getString(R.string.username_key), encrypt(username));
        editor.putString(ctx.getString(R.string.password_key), encrypt(password));
        editor.apply();
    }

    /**
     * encrpyes the data with base64.encode function.
     *
     * @param toEnc
     * @return
     */
    private String encrypt(String toEnc) {
        byte[] UN = StringToByte(toEnc);
        return ByteToString(Base64.encode(UN, Base64.DEFAULT));
    }

    /**
     * decrypts the data .
     *
     * @param toDec
     * @return
     */
    private String decrypt(String toDec) {
        byte[] UN = StringToByte(toDec);
        return ByteToString(Base64.decode(UN, Base64.DEFAULT));
    }

    /**
     * deletes the login information from the device memory.
     *
     * @throws IOException
     */
    public void clearLoginInformation() throws IOException {
        SharedPreferences sharedPref = ctx.getSharedPreferences(pw, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(ctx.getString(R.string.username_key));
        editor.remove(ctx.getString(R.string.password_key));
        editor.apply();

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
                    data.put(ctx.getString(R.string.password_key),password);
                    dialog.dismiss();
                    onDialogClosed.function(null);
                }
            }
        });
        dialog.show();
        return data;
    }*/

    /**
     * checks if the login information is saved or not.
     *
     * @return
     */
    public boolean dataIsSaved() {
        try {
            getLoginDataFromMemory();
        } catch (IOException e) {
            return false;
        }
        return !(username.equals("") || password.equals(""));
    }

    /**
     * returns the login information from the device memory.
     *
     * @return
     * @throws IOException
     */
    public HashMap<String, String> getLoginDataFromMemory() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        SharedPreferences sharedPref = ctx.getSharedPreferences(pw, Context.MODE_PRIVATE);
        username = sharedPref.getString(ctx.getString(R.string.username_key), "");
        password = sharedPref.getString(ctx.getString(R.string.password_key), "");
        map.put(ctx.getString(R.string.username_key), decrypt(username));
        map.put(ctx.getString(R.string.password_key), decrypt(password));
        return map;
    }
    //help functions

    /**
     * turns the string to a byte array
     *
     * @param str
     * @return
     */
    public static byte[] StringToByte(String str) {
        byte[] toRet = new byte[str.length()];
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            toRet[i] = (byte) arr[i];
        }
        return toRet;
    }

    /**
     * turns the byte array to a string
     *
     * @param arg
     * @return
     */
    public static String ByteToString(byte[] arg) {
        char[] arr = new char[arg.length];
        for (int i = 0; i < arg.length; i++) {
            arr[i] = (char) arg[i];
        }
        return String.valueOf(arr);
    }
}
