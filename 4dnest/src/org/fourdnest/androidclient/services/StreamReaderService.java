package org.fourdnest.androidclient.services;

import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

public class StreamReaderService extends IntentService {
    
    public StreamReaderService() {
        super(StreamReaderService.class.getName());
    }

    public StreamReaderService(String name) {
        super(name);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        //Add the Alarm Manager
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FourDNestApplication app = FourDNestApplication.getApplication();
        EggManager em = app.getStreamEggManager();
        
    }

}
