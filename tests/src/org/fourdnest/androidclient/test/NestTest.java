package org.fourdnest.androidclient.test;


import java.net.URISyntaxException;
import java.net.URI;
import org.fourdnest.androidclient.Nest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

public class NestTest extends AndroidTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testEqualsNest() {
		try {
			Nest nest1 = new Nest(1, "Home Nest", "Nest hosted in my home server", new URI("http://127.0.0.1"), 1, "testuser", "secretkey");
			Nest nest2 = new Nest(1, "Home Nest", "Nest hosted in my home server", new URI("http://127.0.0.1"), 1, "testuser", "secretkey");
			Nest nest3 = new Nest(2, "Another nest", "Some random nest", new URI("http://10.0.0.1"), 2, "testuser", "secretkey");
			
			assertTrue(nest1.equals(nest2));
			assertFalse(nest1.equals(nest3));
	
		} catch(URISyntaxException e) {
			fail("URISyntaxException");
		}
	}

}
