package org.fourdnest.androidclient.test;


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
		
		Nest nest1 = new Nest(1, "Home Nest", "Nest hosted in my home server", "127.0.0.1", "MestaDB");
		Nest nest2 = new Nest(1, "Home Nest", "Nest hosted in my home server", "127.0.0.1", "MestaDB");
		Nest nest3 = new Nest(2, "Another nest", "Some random nest", "10.0.0.1", "Facebook");
		
		assertTrue(nest1.equals(nest2));
		assertFalse(nest1.equals(nest3));
		
	}

}
