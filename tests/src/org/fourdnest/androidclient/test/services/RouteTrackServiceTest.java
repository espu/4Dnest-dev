package org.fourdnest.androidclient.test.services;


import org.fourdnest.androidclient.services.RouteTrackService;
import org.fourdnest.androidclient.services.SendQueueService;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class RouteTrackServiceTest extends ServiceTestCase<RouteTrackService> {

	public RouteTrackServiceTest(Class<RouteTrackService> serviceClass) {
		super(serviceClass);
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

}
