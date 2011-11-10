package org.fourdnest.androidclient.test;

import java.util.ArrayList;

import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.NestManager;
import android.test.AndroidTestCase;

public class NestManagerTest extends AndroidTestCase {
	
	NestManager nestManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.nestManager = new NestManager(this.getContext());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		this.nestManager.close();
	}

	public void testListNests() {
		
		Nest nest1 = new Nest(1, "Home Nest", "Nest hosted in my home server", "127.0.0.1", "MestaDB");
		Nest nest2 = new Nest(2, "Another nest", "Some random nest", "10.0.0.1", "Facebook");
		
		this.nestManager.saveNest(nest1);
		this.nestManager.saveNest(nest2);
		
		ArrayList<Nest> nests = this.nestManager.listNests();
		
		// 2 nests should be saves
		assertEquals(2, nests.size());
				
		// Nest1 should be returned as #2, result set is  ordered by name
		assertTrue(nest1.equals(nests.get(1)));
		assertTrue(nest2.equals(nests.get(0)));
		
	}

	public void testSaveAndGetNest() {
		
		Nest nest = new Nest(1, "Home Nest", "Nest hosted in my home server", "127.0.0.1", "MestaDB");
		
		this.nestManager.saveNest(nest);
		
		Nest resultNest = this.nestManager.getNest(1);
		// Check that same Nest is returned
		assertTrue(nest.equals(resultNest));
		
		// Check that some other id does not return equal nest
		assertFalse(nest.equals(this.nestManager.getNest(2)));
	}
	
	

}
