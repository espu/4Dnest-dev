package org.fourdnest.androidclient.services;

import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.ui.ListStreamActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class RouteTrackService extends Service implements LocationListener {
	
	
	public static boolean isTracking = false;
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
	
	private final String TAG = RouteTrackService.class.getSimpleName();
	private final int LOCATION_MIN_DELAY = 1000; // ms
	private final float LOCATION_MIN_DISTANCE = 5; // m
	
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
		
		// Prepare notification message for status bar
		this.notification = new Notification(R.drawable.icon, getText(R.string.gps_notification_on), System.currentTimeMillis());

		// Prepare intent to start desired activity when notification is clicked
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ListStreamActivity.class), 0);        

        // Set status bar info
        notification.setLatestEventInfo(this, getText(R.string.gps_statusbar_title), getText(R.string.gps_statusbar_tracking_active), contentIntent);

        // Start service in foreground
        this.startForeground(R.string.gps_notification_on, notification);
        
        // Run until explicitly stopped
        return START_STICKY;
    }
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		// Remove location updating
		this.locationManager.removeUpdates(this);
		
	    // Cancel the persistent notification.
		this.stopForeground(true);
		
		String message = "Tracking stopped. ";
		message += this.locationCache.size() + " Locations received.";
		
		// Empty the cache
		this.locationCache = new ArrayList<Location>();
		
		// Tell the user we stopped.
	    //Toast.makeText(this, this.NOTE_OFF, Toast.LENGTH_SHORT).show();	    
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		
		RouteTrackService.isTracking = false;
	}

	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		Toast.makeText(this, "New location: " + location.getLongitude(), Toast.LENGTH_SHORT);
		
		// Add some sanity checks, for now just cache it
		this.locationCache.add(location);
	}

	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled");
		Toast.makeText(this, "onProviderDisabled: " + provider, Toast.LENGTH_SHORT);
	}

	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled");
		this.provider = provider;
		Toast.makeText(this, "onProviderEnabled: " + provider, Toast.LENGTH_SHORT);		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "onStatusChanged");
		Toast.makeText(this, "onStatusChanged: " + provider, Toast.LENGTH_SHORT);
	}
	
	/**
	 * Converts a Location object to JSON object with relevant information
	 * @param loc
	 * @return JSON representation of Location
	 * @throws JSONException
	 */
	private JSONObject locationToJSON(Location loc) throws JSONException {
    	JSONObject json = new JSONObject();
    	
		json.put("accuracy", loc.getAccuracy());
		json.put("altitude", loc.getAltitude());
		json.put("bearing", loc.getBearing());
		json.put("latitude", loc.getLatitude());
		json.put("longitude", loc.getLongitude());
		json.put("speed", loc.getSpeed());
		json.put("time", loc.getTime());
		
		return json;    	
    }
	

}
