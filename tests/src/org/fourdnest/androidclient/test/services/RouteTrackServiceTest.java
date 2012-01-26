package org.fourdnest.androidclient.test.services;


import org.fourdnest.androidclient.services.RouteTrackService;
import org.fourdnest.androidclient.services.SendQueueService;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class RouteTrackServiceTest extends ServiceTestCase<RouteTrackService> {

	public RouteTrackServiceTest() {
		super(RouteTrackService.class);
	}
	
	@Override
	public void setUp() throws Exception {
		//super.setUp();	
	}
	
	public void testStartable() {
		Intent startIntent = new Intent();
		startIntent.setClass(getContext(), RouteTrackService.class);
		startService(startIntent);
		
		assertTrue(true);
	}
	
	public void testBindable() {
		Intent startIntent = new Intent();
        startIntent.setClass(getContext(), RouteTrackService.class);
        IBinder service = bindService(startIntent);
        
        assertNull(service);
	}
	
	public void testLocationJSON() {
	
		try {
			
			Location loc1 = new Location("gps");
			loc1.setAccuracy(1);
			loc1.setAltitude(22);
			loc1.setBearing(0);
			loc1.setLatitude(60);
			loc1.setLongitude(25);
			loc1.setSpeed(3);
			loc1.setTime(System.currentTimeMillis());
			
			// Convert to JSON
			JSONObject o1 = RouteTrackService.locationToJSON(loc1);
			
			// Convert back to Location
			Location loc2 = RouteTrackService.locationFromJSON(o1);
			
			// Convert new location to JSON again, compare string representations
			JSONObject o2 = RouteTrackService.locationToJSON(loc2);
			
			assertTrue(o1.toString().equals(o2.toString()));	
		
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

}
