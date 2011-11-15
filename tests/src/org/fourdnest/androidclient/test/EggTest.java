package org.fourdnest.androidclient.test;


import java.util.ArrayList;
import java.util.Date;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

public class EggTest extends AndroidTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testEggEquals() {
		
		Date now = new Date();
		
		Egg e1 = new Egg();
		Egg e2 = new Egg();
		
		assertTrue(e1.equals(e2));
		
		ArrayList<Tag> tagList1 = new ArrayList<Tag>();
		tagList1.add(new Tag("Cool"));
		tagList1.add(new Tag("Awesome"));
		
		ArrayList<Tag> tagList2 = new ArrayList<Tag>();
		tagList2.add(new Tag("Cool"));
		tagList2.add(new Tag("Awesome"));
		
		e1 = new Egg(1, 1234, null, null, "Great egg", tagList1, now);
		e2 = new Egg(2, 1234, null, null, "Great egg", tagList2, now);
		
		assertTrue(!e1.equals(e2));
		
		
		e2 = new Egg(1, 1234, null, null, "Great egg", tagList2, now);
		assertTrue(e1.equals(e2));
		
		tagList2 = new ArrayList<Tag>();
		tagList2.add(new Tag("Awesome"));
		tagList2.add(new Tag("Cool"));
		
		e2 = new Egg(1, 1234, null, null, "Great egg", tagList2, now);
		assertTrue(!(e1.equals(e2)));
		
		assertTrue( !(e1.equals(new Date())) );
		
	}

}
