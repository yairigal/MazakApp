package project.android.com.mazak.Model.Entities;

import android.os.AsyncTask;

/**
 * Created by Yair on 2017-06-18.
 * This class wraps AsyncTask for easier Access
 */

public class BackgroundTask {
    AsyncTask<Void,Void,Void> task;
    public BackgroundTask(final Delegate before, final Delegate in , final Delegate after){
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                before.function(null);
            }

            @Override
            protected Void doInBackground(Void... params) {
                in.function(null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                after.function(null);
            }
        };
    }

    public BackgroundTask(final Delegate in){
        task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {
                in.function(null);
                return null;
            }

        };
    }

    public void start(){
        task.execute();
    }
}
