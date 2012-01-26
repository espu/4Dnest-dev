package org.fourdnest.androidclient.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.tools.LocationHelper;
import org.fourdnest.androidclient.ui.ListStreamActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class RouteTrackService extends Service implements LocationListener {
	
	/**
	 * Location cache for received locations
	 */
	private List<Location> locationCache;
	
	/**
	 * Location manager to access location info
	 */
	private LocationManager locationManager;

	/**
	 * Task bar notification
	 */
	private Notification notification;
	
	private String provider = "gps"; // Fixed provider
	
	private static final File FILE_BASE_PATH = Environment.getExternalStorageDirectory();
	private static final String FILE_DIRECTORY = "fourdnest";
	private static final String FILE_EXTENSION = ".json";
	
	private final static String TAG = RouteTrackService.class.getSimpleName();
	private final int LOCATION_MIN_DELAY = 1000; // ms
	private final float LOCATION_MIN_DISTANCE = 5; // m
	private final int LATEST_LOC_MAX_DELAY = 1000 * 60; // 60 sec 
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		
		this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(
        		this.provider,
        		LOCATION_MIN_DELAY,
        		LOCATION_MIN_DISTANCE,
        		this
        		);
        
        this.locationCache = new ArrayList<Location>();        
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStart");
		// If gps provider does not exist or is disabled, show error and die
		if(this.locationManager.getProvider(this.provider) == null ||
			!this.locationManager.getProviders(true).contains(this.provider)) {

			Toast.makeText(this.getApplicationContext(), getText(R.string.gps_notification_on), Toast.LENGTH_LONG);
			Log.d(TAG, "No location provider, stopping");
			
			stopSelf();
		}
		
		// Check last known location, if it's more recent than max delay specifies, add it as first point
		Location lastKnownLocation = this.locationManager.getLastKnownLocation(this.provider);
		if(lastKnownLocation != null && lastKnownLocation.getTime() < LATEST_LOC_MAX_DELAY) {
			this.locationCache.add(lastKnownLocation);
		}

		// Prepare notification message for status bar
		this.notification = new Notification(R.drawable.icon, getText(R.string.gps_notification_on), System.currentTimeMillis());

		// Prepare intent to start desired activity when notification is clicked
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ListStreamActivity.class), 0);        

        // Set status bar info
        notification.setLatestEventInfo(this, getText(R.string.gps_statusbar_title), getText(R.string.gps_statusbar_tracking_active), contentIntent);

        // Start service in foreground, checking for null to avoid Android testing bug
        if(getSystemService(ACTIVITY_SERVICE) != null) {
        	this.startForeground(R.string.gps_notification_on, notification);
        }
        
        // Run until explicitly stopped
        return START_STICKY;
    }
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		// Remove location updating
		this.locationManager.removeUpdates(this);
		
	    // Cancel the persistent notification
		if(getSystemService(ACTIVITY_SERVICE) != null) {
			this.stopForeground(true);
		}
		
		String message = "Tracking stopped. ";
		message += this.locationCache.size() + " Locations received.";
		
		// Trigger file write
		try {
			this.writeLocationCache(this.getOutputFile());
			Toast.makeText(this, getText(R.string.gps_file_save_success), Toast.LENGTH_LONG);
		} catch(Exception e) {
			Toast.makeText(this, getText(R.string.gps_file_save_failure) + ": " + e.getMessage(), Toast.LENGTH_LONG);
		}
		
		// Empty the cache
		this.locationCache = new ArrayList<Location>();
		
		// Tell the user we stopped    
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged: " + LocationHelper.locationToJSON(location).toString());
		Toast.makeText(this, "New location: " + location.getLongitude(), Toast.LENGTH_SHORT);
		
		// Add some sanity checks, for now just cache it
		this.locationCache.add(location);
	}

	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled: " + provider);
	}

	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled: " + provider);
		//this.provider = provider;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "onStatusChanged, provider: " + provider + ", status: " + status);
	}
	
	
	/**
	 * Writes locationCache to given file as JSON string
	 * @param outFile target file
	 * @return success
	 */
	private boolean writeLocationCache(File outFile) throws IOException, JSONException {
		JSONObject o = LocationHelper.listToJSON(this.locationCache);

		FileWriter fw = new FileWriter(outFile);
		fw.write(o.toString());
		fw.close();
		
		return true;
	}
	
	/**
	 * Gets output file handle
	 * @return Output file handle
	 * @throws IOException
	 */
	private File getOutputFile() throws IOException {
		// Get file name, check that it can be created. If not,
		// get a file name with added seq number and try a few times
		File outputFile = new File(FILE_BASE_PATH + File.separator + FILE_DIRECTORY, getFileName());
		
		int tryCounter = 0;
		while(!outputFile.createNewFile()) {
			tryCounter++;
			if(tryCounter > 9) {
				Log.e(TAG, "Unable to open file for writing. Gave up after 10 tries");
				return null;
			}
			outputFile = new File(FILE_BASE_PATH, getFileName(tryCounter));
		}
		
		return outputFile;		
	}
	
	/**
	 * Get file name for a file that is saved "now". Calls getFileName(0);
	 * @return string filename
	 */
	private String getFileName() {
		return this.getFileName(0);
	}
	/***
	 * Get file name for a file. Takes optional parameter that adds _duplicateNum after
	 * timestamp
	 * @param duplicateNum
	 * @return string filename
	 */
	private String getFileName(int duplicateNum) {
		String f = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
		if(duplicateNum > 0) f += "_" + duplicateNum;
		
		f += FILE_EXTENSION;
		return f;
	}
	
}
