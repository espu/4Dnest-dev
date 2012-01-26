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
		super.setUp();
	
	}
	
	public void testStartable() {
		Intent startIntent = new Intent();
		startIntent.setClass(getContext(), RouteTrackService.class);
		startService(startIntent);
		
		assertTrue(true);
	}
	
	public void testBindable() {
		Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SendQueueService.class);
        IBinder service = bindService(startIntent);
        
        assertNull(service);
	}
	
	public void testJSONification() {
		RouteTrackService s = new RouteTrackService();
		
		Location loc1 = this.getTestLocation();
		
		JSONObject json = s.locationToJSON(loc1);
		assertTrue(json != null);
		
		Location loc2 = s.locationFromJSON(json);
		assertTrue(s.locationsEqual(loc1,  loc2));
	}
	
	private Location getTestLocation() {
		Location loc = new Location("gps");
		loc.setAltitude(100);
		loc.setAccuracy((float) 0.1);
		loc.setBearing(5);
		loc.setLatitude(60);
		loc.setLongitude(25);
		loc.setSpeed((float) 5);
		loc.setTime(System.currentTimeMillis());
		
		return loc;
	}

}
