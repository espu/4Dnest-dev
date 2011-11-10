package org.fourdnest.androidclient.test.comm;


import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;
import android.util.Log;

public class httpTest extends AndroidTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testHttpGet() throws Exception {
		
		FourDNestProtocol protocol = new FourDNestProtocol();
		String get = protocol.getTest();
		assertTrue(get.length() != 0);
		Log.v("httpget", get);
	}
	
	@Test
	public void testHttpPost() throws Exception {
		FourDNestProtocol protocol = new FourDNestProtocol();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Egg egg = new Egg("/sdcard/kuva.jpg", "Trolol", tags);
		Nest nest = new Nest(007, "testNest", "testNest", "http://test42.4dnest.org/fourdnest/api/", "protocol");
		String post = protocol.sendEgg(egg, nest);
		assertTrue(post.split(" ")[0].equalsIgnoreCase("201"));
		Log.v("httppost", post);
	}

}
