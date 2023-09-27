package project.android.com.mazak.Controller;

import android.accounts.NetworkErrorException;
import android.content.Context;

import java.net.UnknownHostException;

import project.android.com.mazak.Model.Entities.LoginException;
import project.android.com.mazak.R;

public class Constants {

    /**
     * checks the error type and returns the corresponds message
     * @param e1
     * @return
     */
    public static String checkErrorTypeAndMessage(Exception e1, Context ctx) {
        String errorMsg;
        if (e1 instanceof UnknownHostException) {
            errorMsg = ctx.getString(R.string.levnet_down);
        }
        else if (e1 instanceof NullPointerException) {
            errorMsg = ctx.getString(R.string.wrong_user_pass);
        }
        else if (e1 instanceof NetworkErrorException) {
            errorMsg = ctx.getString(R.string.check_network);
        }
        else if (e1 instanceof LoginException) {
            errorMsg = ctx.getString(R.string.wrong_user_pass);
        }
        else {
            errorMsg = ctx.getString(R.string.db_error);
        }
        return errorMsg;
    }


    public static CharSequence getChartType(int position, Context ctx) {
        if(position == 0) {
            return ctx.getString(R.string.bar_chart);
        }
        else {
            return ctx.getString(R.string.pie_chart);
        }
    }
}
