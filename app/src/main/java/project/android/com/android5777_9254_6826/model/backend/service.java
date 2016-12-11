package project.android.com.android5777_9254_6826.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class service extends Service{
    Backend db;
    private final int timeToSleep = 10000;

    public service() {
        db = FactoryDatabase.getDatabase();
        toStop = false;
        try {
            checkForChange();
        } catch (InterruptedException e) {
            e.printStackTrace();
            stopCheck();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    boolean toStop = false;
    private void checkForChange() throws InterruptedException {
        while(!toStop){
            if(db.ifNewAttractionAdded() || db.ifNewBusinessAdded()) {
                //TODO something if new business added.
            }
            Thread.sleep(timeToSleep);
        }
    }
    private void stopCheck(){
        toStop = true;
    }
}
