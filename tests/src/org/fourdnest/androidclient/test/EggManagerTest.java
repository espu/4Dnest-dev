package org.fourdnest.androidclient.test;

import java.util.ArrayList;
import java.util.Date;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import android.test.AndroidTestCase;

public class EggManagerTest extends AndroidTestCase {
	
	EggManager eggManager;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.eggManager = new EggManager(this.getContext());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		this.eggManager.close();
	}
	
	public void testSaveAndGetEgg() {
		Date now = new Date();
		Egg e = new Egg(
			0,
			1,
			null,
			null,
			"TestCaption",
			null,			
			now
		);
		
		this.eggManager.saveEgg(e);
		
		Egg fetchedEgg = this.eggManager.getEgg(0);
		assertTrue(e.equals(fetchedEgg));
		
		
	}

	public void testListEggs() {
		fail("Not implemented");
		
	}
	

}
