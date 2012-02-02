package org.fourdnest.androidclient.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.comm.FourDNestThumbnailManager;
import org.fourdnest.androidclient.comm.ThumbnailManager;

import android.app.AlarmManager;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class StreamReaderService extends IntentService {
	
	public static final String TAG = "STREAMREADERSERVICE";
    
    /**Intent should have this category when stream should be read from the server */
    public static final String READ_STREAM = "READ_STREAM_CATEGORY";
    
    /**How long we initially wait before fetching the Stream*/
    public static final long FIRST_INTERVAL = 0;
    
 
    
    public static final String THUMBNAIL_SAVE_LOCATION = "thumbnails";
    

    

    
    private static final long TWO_MINUTE = 120000;
    
    private FourDNestApplication app;
    
    public StreamReaderService() {
        super(StreamReaderService.class.getName());
    }

    public StreamReaderService(String name) {
        super(name);
    }
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	Log.d(TAG, "Intentcreated");
        
        app = FourDNestApplication.getApplication();
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(app, StreamReaderService.class);
        intent.addCategory(READ_STREAM);
        am.setInexactRepeating( //Be power efficient: we don't need exact timing
                AlarmManager.ELAPSED_REALTIME,  // Don't wake up phone just for this
                FIRST_INTERVAL,                             
                TWO_MINUTE,     // Update frequency
                PendingIntent.getService(
                        app,                    // The context
                        0,
                        intent,
                        0
                )
        );
    }
    
    @Override
    public void onDestroy() {
    	Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.d(TAG, "Handling intent");
        if (intent.hasCategory(READ_STREAM)) {
            EggManager em = app.getStreamEggManager();
            List<Egg> eggList = app.getCurrentNest().getProtocol().getStream();
            Log.d(TAG, "Egglist size: " + eggList.size());
            em.deleteAllEggs();
            ThumbnailManager thumbnailManager = new FourDNestThumbnailManager();
            for (int i = 0; i < eggList.size(); i++) {
            	Egg egg = eggList.get(i);
                em.saveEgg(egg);
                thumbnailManager.getThumbnail(egg);
                
            }
            Log.d(TAG, "Saved eggs");
            List<Egg> eggs = em.listEggs();
            Log.d("EGGAMOUNT2", String.valueOf(eggs.size()));
        }
    }

}
