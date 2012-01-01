package org.fourdnest.androidclient.services;

import java.util.List;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.ui.ListStreamActivity;

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
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class RouteTrackService extends Service implements LocationListener {
	
	private LocationManager locationManager;
	private String provider = "gps"; // Fixed provider
	private int NOTE_ON = R.string.gps_track_on;
	private int NOTE_OFF = R.string.gps_track_off;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		
        
        long minTime = 1000; //ms
        float minDist = 5; //m        
        this.locationManager.requestLocationUpdates(this.provider, minTime, minDist, this);
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		// If gps provider does not exist or is disabled, show error and die
		if(this.locationManager.getProvider(this.provider) == null ||
			!this.locationManager.getProviders(true).contains(this.provider)) {
			Toast.makeText(this, getText(R.string.gps_track_gps_disabled), Toast.LENGTH_LONG);
			stopSelf();
		}
		
		
		// Prepare notification message for status bar
		Notification notification = new Notification(R.drawable.icon, getText(this.NOTE_ON), System.currentTimeMillis());

		// Prepare intent to start desired activity when notification is clicked
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ListStreamActivity.class), 0);        

        // Set status bar info
        notification.setLatestEventInfo(this, getText(R.string.gps_track_statusbar_title), getText(R.string.gps_track_statusbar_text), contentIntent);

        // Start service in foreground
        this.startForeground(this.NOTE_ON, notification);
        
        // Run until explicitly stopped
        return START_STICKY;
    }
	
	@Override
	public void onDestroy() {
		
		// Remove location updating
		this.locationManager.removeUpdates(this);
		
	    // Cancel the persistent notification.
		this.stopForeground(true);
	
		// Tell the user we stopped.
	    Toast.makeText(this, this.NOTE_OFF, Toast.LENGTH_SHORT).show();
	}

	public void onLocationChanged(Location location) {
		Toast.makeText(this, "New location: " + location.getLongitude(), Toast.LENGTH_SHORT);
	}

	public void onProviderDisabled(String provider) {		
		Toast.makeText(this, "onProviderDisabled: " + provider, Toast.LENGTH_SHORT);
	}

	public void onProviderEnabled(String provider) {
		this.provider = provider;
		Toast.makeText(this, "onProviderEnabled: " + provider, Toast.LENGTH_SHORT);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(this, "onStatusChanged: " + provider, Toast.LENGTH_SHORT);		
	}
	

}
