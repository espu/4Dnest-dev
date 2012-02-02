package org.fourdnest.androidclient.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.FourDNestApplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class StreamReaderService extends IntentService {
	
	public static final String TAG = "STREAMREADERSERVICE";
    
    /**Intent should have this category when stream should be read from the server */
    public static final String READ_STREAM = "READ_STREAM_CATEGORY";
    public static final String STREAM_FREQ = "STREAM_FREQUENCY_CATEGORY";
    public static final String NEW_FREQUENCY = "NEW_FRQUENCY";
    public static final String STREAM_SIZE = "STREAM_SIZE_CATEGORY";
    public static final String NEW_STREAM_SIZE = "NEW_STREAM_SIZE";
    
    /**How long we initially wait before fetching the Stream*/
    public static final long FIRST_INTERVAL = 0;
    
    /**Location of thumbnails on the server */
    public static final String THUMBNAIL_PATH = "content/instance/";
    
    public static final String THUMBNAIL_SAVE_LOCATION = "thumbnails";
    
    /** Thumbnails on the server are in jpg format*/
    private static final String THUMBNAIL_FILETYPE = "jpg";
    
    private static final String THUMBNAIL_DEFAULT_SIZE = "-100x100.";
    
    /** Default Frequency in seconds, as a String so it can be used in the getPreference method*/
    private static final String DEFAULT_FREQUENCY = "600";
    
    /** Default Frequency in seconds, as a String so it can be used in the getPreference method*/
    private static final String DEFAULT_SIZE = "10";
    
    private FourDNestApplication app;
    private int size = Integer.parseInt(DEFAULT_SIZE);
    
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(app);
        int frequency = Integer.parseInt(prefs.getString("stream_frequency",
               String.valueOf(DEFAULT_FREQUENCY))) * 60000;
        size = Integer.parseInt(prefs.getString("stream_size", String
                .valueOf(DEFAULT_SIZE)));
        Log.d(TAG, "Value of freq after read: " + frequency);
        Log.d(TAG, "Value of size after read: " + size);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(app, StreamReaderService.class);
        intent.addCategory(READ_STREAM);
        am.setInexactRepeating( //Be power efficient: we don't need exact timing
                AlarmManager.ELAPSED_REALTIME,  // Don't wake up phone just for this
                FIRST_INTERVAL,      //Don't run on start                            
                frequency,     // Update frequency
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
            Log.d(TAG, "Got READ_STREAM category");
            EggManager em = app.getStreamEggManager();
            em.deleteAllEggs();
            List<Egg> eggList = app.getCurrentNest().getProtocol().getStream(size);
            Log.d(TAG, "Egglist size: " + eggList.size());
            for (int i = 0; i < eggList.size(); i++) {
                em.saveEgg(eggList.get(i));
                String thumbnailUri = app.getCurrentNest().getBaseURI()
                        + THUMBNAIL_PATH + eggList.get(i).getExternalId()
                        + THUMBNAIL_DEFAULT_SIZE + THUMBNAIL_FILETYPE;
                String thumbnail_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + THUMBNAIL_SAVE_LOCATION;
                if (!new File(thumbnail_dir).exists()) {
                	new File(thumbnail_dir).mkdirs();
                }
                String saveLocation = thumbnail_dir + "/" + eggList.get(i).getExternalId() + ".jpg";
               // Log.d("SAVELOC", saveLocation);
                if (app.getCurrentNest().getProtocol().getMediaFile(thumbnailUri, saveLocation)) {
                 //   Log.d(TAG, "Egg written succesfully");
                }else {
                 //   Log.d(TAG, "Egg failed to write");
                }
            }
            Log.d(TAG, "Saved eggs");
            List<Egg> eggs = em.listEggs();
            Log.d("EGGAMOUNT2", String.valueOf(eggs.size()));
        }
        if (intent.hasCategory(STREAM_FREQ)) {
        	Log.d(TAG, "Got updated frequency");
        }
    }

}
