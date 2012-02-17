package org.fourdnest.androidclient.services;

import java.io.File;

import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.ThumbnailManager;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class CacheCleaningService extends IntentService {
	
	public static final String CLEAN_THUMBNAILS = "cleanThumbnails";
	private static final String THUMBNAIL_LOCATION = "/fourdnest/thumbnails/";
	
	public CacheCleaningService() {
		super(CacheCleaningService.class.getName());
	}

	public CacheCleaningService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		FourDNestApplication app = FourDNestApplication.getApplication();
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent in = new Intent(app, CacheCleaningService.class);
		in.addCategory(CLEAN_THUMBNAILS);
		am.setInexactRepeating( // Be power efficient: we don't need exact  timing
				AlarmManager.ELAPSED_REALTIME, // Don't wake up phone just for this
				0, // Time of first	 execution
				AlarmManager.INTERVAL_DAY, // Clean the thumbnails once a day.
				PendingIntent.getService(app, // The context
						0, in, 0));
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.hasCategory(CLEAN_THUMBNAILS)) {
			String thumbnailDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + THUMBNAIL_LOCATION;
			Log.d("THUMBNAILPATH", thumbnailDir);
		}
		
	}

}
