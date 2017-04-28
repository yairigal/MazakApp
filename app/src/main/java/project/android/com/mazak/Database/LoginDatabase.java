package project.android.com.mazak.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
        editor.putString("username", encrypt(username));
        editor.putString("password", encrypt(password));
        editor.commit();
    }

    private String encrypt(String toEnc) {
        byte[] UN = StringToByte(toEnc);
        return ByteToString(Base64.encode(UN, Base64.DEFAULT));
    }
    private String decrypt(String toDec) {
        byte[] UN = StringToByte(toDec);
        return ByteToString(Base64.decode(UN, Base64.DEFAULT));
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
        map.put("username", decrypt(username));
        map.put("password", decrypt(password));
        return map;
    }
    public static byte[] StringToByte(String str){
        byte[] toRet = new byte[str.length()];
        char[] arr = str.toCharArray();
        for(int i=0;i<arr.length;i++) {
            toRet[i] = (byte) arr[i];
        }
        return toRet;
    }
    public static String ByteToString(byte[] arg){
        char[] arr = new char[arg.length];
        for(int i = 0;i<arg.length;i++){
            arr[i] = (char) arg[i];
        }
        return String.valueOf(arr);
    }
}
