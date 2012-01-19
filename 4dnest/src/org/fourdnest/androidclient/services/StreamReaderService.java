package org.fourdnest.androidclient.services;

import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

public class StreamReaderService extends IntentService {
    
    /**Intent should have this category when stream should be read from the server */
    public static final String READ_STREAM = "READ_STREAM_CATEGORY";
    
    /**How long we initially wait before fetching the Stream*/
    public static final long FIRST_INTERVAL = 0;
    
    /**Location of thumbnails on the server */
    public static final String THUMBNAIL_PATH = "content/instance/";
    
    /** Thumbnails on the server are in jpg format*/
    private static final String THUMBNAIL_FILETYPE = "jpg";
    
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
        app = FourDNestApplication.getApplication();
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(app, StreamReaderService.class);
        intent.addCategory(READ_STREAM);
        am.setInexactRepeating( //Be power efficient: we don't need exact timing
                AlarmManager.ELAPSED_REALTIME,  // Don't wake up phone just for this
                FIRST_INTERVAL,                             
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,     // Update frequency
                PendingIntent.getService(
                        app,                    // The context
                        0,
                        intent,
                        0
                )
        );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasCategory(READ_STREAM)) {
            EggManager em = app.getStreamEggManager();
            List<Egg> eggList = app.getCurrentNest().getProtocol().getStream();
            for (int i = 0; i < eggList.size(); i++) {
                em.saveEgg(eggList.get(i));
            }
            //FIXME get thumbnails and save them to an disclosed location
        }
    }

}
