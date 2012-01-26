package org.fourdnest.androidclient.services;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.ui.ListStreamActivity;
import org.json.JSONArray;
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
	
	private static final File FILE_BASE_PATH = Environment.getExternalStorageDirectory();
	private static final String FILE_DIRECTORY = "fourdnest";
	private static final String FILE_EXTENSION = ".json";
	
	private final static String TAG = RouteTrackService.class.getSimpleName();
	private final int LOCATION_MIN_DELAY = 1000; // ms
	private final float LOCATION_MIN_DISTANCE = 5; // m
	private final int LATEST_LOC_MAX_DELAY = 1000 * 60; // 60 sec 
	
	public static final String JSON_ACCURACY = "accuracy";
	public static final String JSON_ALTITUDE = "altitude";
	public static final String JSON_BEARING = "bearing";
	public static final String JSON_LATITUDE = "latitude";
	public static final String JSON_LONGITUDE = "longitude";
	public static final String JSON_SPEED = "speed";
	public static final String JSON_TIME = "time";
	public static final String JSON_LOCATIONARRAY = "locations";

	/**
	 * JSONification keywords
	 */
	private final String JSON_LOCATION_PROVIDER = "json";
	private final String JSON_WRAPPER = "locations";
	private final int JSON_INDENT_FACTOR = 1;
	

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
		if(lastKnownLocation.getTime() < LATEST_LOC_MAX_DELAY) {
			this.locationCache.add(lastKnownLocation);
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
		
		// Trigger file write
		this.writeLocationsToFile();
		
		// Empty the cache
		this.locationCache = new ArrayList<Location>();
		
		// Tell the user we stopped.
	    //Toast.makeText(this, this.NOTE_OFF, Toast.LENGTH_SHORT).show();	    
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		
		RouteTrackService.isTracking = false;
	}

	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged: " + RouteTrackService.locationToJSON(location).toString());
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
	private boolean writeLocationCache(FileWriter outFile) {
		if(this.locationCache.size() < 1) return false;
		
		JSONObject o = this.locationCacheToJSON();

		if(o == null) return false;
		
		try {
			outFile.write(o.toString(JSON_INDENT_FACTOR));
		} catch(JSONException jsone) {
			Log.e(TAG, "Failed stringifying JSON: " + jsone.getMessage());
			return false;
		} catch(IOException ioe) {
			Log.e(TAG, "Failed writing file: " + ioe.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Serializes local locationCache to a JSON object
	 * @return JSONObject representation of location cache or null on failure
	 */
	private JSONObject locationCacheToJSON() {
		try {
			// Create location array
			JSONArray arr = new JSONArray();
			for(Location loc : this.locationCache) {
				JSONObject json = RouteTrackService.locationToJSON(loc);
				if(json != null) {
					arr.put(json);
				}
			}
					
			JSONObject wrapper = new JSONObject();
			wrapper.put(JSON_WRAPPER, arr);
			
			return wrapper;
		} catch(JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}
	
	
	
	/**
	 * Compares two locations for equality. They are equal if
	 * accuracy, altitude, bearing, latitude, longitude, speed and time
	 * are equal
	 * 
	 * @param loc1
	 * @param loc2
	 * @return the two are equal
	 */
	public boolean locationsEqual(Location loc1, Location loc2) {
		if(loc1 == null || loc2 == null) return false;
		
		if(!(loc1.getAccuracy() == loc2.getAccuracy())) return false;
		if(!(loc1.getAltitude() == loc2.getAltitude())) return false;
		if(!(loc1.getBearing() == loc2.getBearing())) return false;
		if(!(loc1.getLatitude() == loc2.getLatitude())) return false;
		if(!(loc1.getLongitude() == loc2.getLongitude())) return false;
		if(!(loc1.getSpeed() == loc2.getSpeed())) return false;
		if(!(loc1.getTime() == loc2.getTime())) return false;
		 
		return true;
	}
	
	/**
	 * Converts a Location object to JSON object with relevant information
	 * @param loc Location ot serialize
	 * @return JSON representation of Location or null on failure
	 */
	public static JSONObject locationToJSON(Location loc) {
		try {
	    	JSONObject json = new JSONObject();
	    	
			json.put(JSON_ACCURACY, loc.getAccuracy());
			json.put(JSON_ALTITUDE, loc.getAltitude());
			json.put(JSON_BEARING, loc.getBearing());
			json.put(JSON_LATITUDE, loc.getLatitude());
			json.put(JSON_LONGITUDE, loc.getLongitude());
			json.put(JSON_SPEED, loc.getSpeed());
			json.put(JSON_TIME, loc.getTime());
			
			return json;
		} catch(JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
    }
	
	/*
	 * @param json object containing relevant location information
	 * @return Location object
	 * @throws JSONException
	 */
	public static Location locationFromJSON(JSONObject json) {
		Location loc = new Location("gps");
				
		try {
			if(!json.isNull(JSON_ACCURACY)) {
				loc.setAccuracy(Float.parseFloat(json.getString(JSON_ACCURACY)));
			}
			if(!json.isNull(JSON_ALTITUDE)) {
				loc.setAltitude(Double.parseDouble(json.getString(JSON_ALTITUDE)));
			}
			if(!json.isNull(JSON_BEARING)) {
				loc.setBearing(Float.parseFloat(json.getString(JSON_BEARING)));
			}
			if(!json.isNull(JSON_LATITUDE)) {
				loc.setLatitude(Double.parseDouble(json.getString(JSON_LATITUDE)));
			}
			if(!json.isNull(JSON_LONGITUDE)) {
				loc.setLongitude(Double.parseDouble(json.getString(JSON_LONGITUDE)));
			}
			if(!json.isNull(JSON_SPEED)) {
				loc.setSpeed(Float.parseFloat(json.getString(JSON_SPEED)));
			}
			if(!json.isNull(JSON_TIME)) {
				loc.setTime(json.getLong(JSON_TIME));
			}
		} catch(JSONException exc) {
			Log.e(TAG, "JSONException extracting Location from JSON: " + exc.getMessage());
		}
		
		return loc; 
	}
	
	private void writeLocationsToFile() {
		if(this.locationCache.size() < 1) return;
		
		FileOutputStream output = null;
		try {
			File outputDir = new File(FILE_BASE_PATH, FILE_DIRECTORY);
			outputDir.mkdirs();
	
			File outputFile = new File(outputDir, DateFormat.format("yyyy-MM-dd_HH:mm:ss", new Date()) + FILE_EXTENSION);
			//File outputFile = new File("/sdcard/test.json");
			
			int tryCounter = 0;
			while(!outputFile.createNewFile()) {
				tryCounter++;
				outputFile = new File(FILE_BASE_PATH, DateFormat.format("yyyy-MM-dd_HH:mm:ss", new Date()) + "_" + tryCounter + FILE_EXTENSION);
				if(tryCounter > 9) {
					Log.e(TAG, "Unable to open file for writing. Gave up after 10 tries");
					return;
				}
			}
			
        	output = new FileOutputStream(outputFile);
        	
        	// Create JSON Object out of all objects in given list
	        JSONArray arr = new JSONArray();
	        for(Location loc : this.locationCache) {
	        	JSONObject o = RouteTrackService.locationToJSON(loc);
	        	arr.put(o);
	        }
        
	        JSONObject container = new JSONObject();
	        container.put(JSON_LOCATIONARRAY, arr);
        
	        // Output to file
	        output.write(container.toString().getBytes());	        
        }
        catch(JSONException jsonE) {
        	Log.e(TAG, jsonE.getMessage());
        }
        catch(Exception e) {
        	Log.e(TAG, e.getMessage());
        } finally {
        	// Close the output
        	if(output != null) {
        		try {
        			output.close();
        		} catch(IOException e) {
        			// File closing fails -> probably already closed
        		}
        	}
        }
	}

}
