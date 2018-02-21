package project.android.com.mazak.Model.Entities;

import project.android.com.mazak.Database.LoginDatabase;

/**
 * Created by Yair Yigal on 2018-02-20.
 */

public class LoginException extends Exception {
    public LoginException(String msg){
        super(msg);
    }
}
