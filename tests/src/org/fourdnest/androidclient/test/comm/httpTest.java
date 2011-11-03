package org.fourdnest.androidclient.test.comm;


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
		String post = protocol.postTest();
		assertTrue(post.length() != 0);
		Log.v("httppost", post);
	}

}
