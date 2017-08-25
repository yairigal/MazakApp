package project.android.com.mazak.Model.Entities;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

/**
 * Created by Yair on 2017-06-18.
 * This class wraps AsyncTask for easier Access
 */

public class BackgroundTask {
    AsyncTask<Void, Void, Void> task;

    public BackgroundTask(final Delegate before, final Delegate in, final Delegate after) {
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

    public BackgroundTask(final Delegate in) {
        task = new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {
                in.function(null);
                return null;
            }

        };
    }

    public BackgroundTask(final Delegate before, final Delegate in){
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
        };
    }

    public void start() {
        //AsyncTaskCompat.executeParallel(task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

 /*   Thread backgroundThread,afterThread;
    Delegate before,in,after;
    public BackgroundTask(Delegate before, final Delegate in , final Delegate after){
        this.before = before;
        this.in = in;
        this.after = after;
        afterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                after.function(null);
            }
        });
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                in.function(null);
                afterThread.run();
            }
        });
    }

    public BackgroundTask(final Delegate in){
        this.before = new Delegate() {
            @Override
            public void function(Object obj) {

            }
        };
        this.in = in;
        this.after = new Delegate() {
            @Override
            public void function(Object obj) {

            }
        };
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                in.function(null);
            }
        });
    }

    public BackgroundTask(Delegate before, final Delegate in){
        this.before = before;
        this.in = in;
        this.after = new Delegate() {
            @Override
            public void function(Object obj) {

            }
        };
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                in.function(null);
            }
        });
    }


    public void start(){
        before.function(null);
        backgroundThread.start();
    }*/
}
