package org.fourdnest.androidclient.test;

import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.EggManager;
import org.fourdnest.androidclient.Tag;

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
		long now = System.currentTimeMillis();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Egg e = new Egg(
			0,
			1,
			null,
			null,
			"TestCaption",
			tags,
			now
		);
		
		this.eggManager.saveEgg(e);
		
		Egg fetchedEgg = this.eggManager.getEgg(0);
		assertTrue(e.equals(fetchedEgg));
		
		
	}

	public void testListEggs() {
		long now = System.currentTimeMillis();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		
		Egg e1 = new Egg(
			0,
			1,
			null,
			null,
			"CaptionForEgg",
			tags,
			now
		);
		this.eggManager.saveEgg(e1);
		
		Egg e2 = new Egg(
			1,
			2,
			null,
			null,
			"CaptionToo!",
			tags,
			now
		);
		this.eggManager.saveEgg(e2);
		
		Egg e3 = new Egg(
			1337,
			10,
			null,
			null,
			null,
			tags,
			now
		);
		this.eggManager.saveEgg(e3);
		
		
		List<Egg> eggs = this.eggManager.listEggs();
		
		assertTrue(eggs.get(0).equals(e1));
		assertTrue(eggs.get(1).equals(e2));
		assertTrue(eggs.get(2).equals(e3));
		
	}
	

}
