package org.fourdnest.androidclient.test.services;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.NestManager;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.ProtocolFactory;
import org.fourdnest.androidclient.services.SendQueueService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Tests the send queue service.
 * @author gronsti
 */
public class SendQueueServiceTest extends AndroidTestCase {
	private NestManager testNestManager;
	private SendQueueService service;
	
	private Egg massEgg;
	private Egg trueEgg;
	private Egg falseEgg;

	@Before
	public void setUp() throws Exception {
		this.testNestManager = new DummyNestManager(this.getContext());
		this.service = new SendQueueService(this.testNestManager);
		this.massEgg = new Egg(
				0,
				1,
				"Matti",
				null,
				null,
				"Mass Egg",
				new ArrayList<Tag>(),
				System.currentTimeMillis()
			);
		this.trueEgg = new Egg(
				1,
				1,
				"Matti",
				null,
				null,
				"True Egg",
				new ArrayList<Tag>(),
				System.currentTimeMillis()
			);
		this.falseEgg = new Egg(
				2,
				1,
				"Matti",
				null,
				null,
				"False Egg",
				new ArrayList<Tag>(),
				System.currentTimeMillis()
			);
	}

	@After
	public void tearDown() throws Exception {
		this.service.stop();
	}
	
	@Test
	public void testFastScheduling() {
		long allowed = 1000;	// Using 1s, real maximum is 10s
		long timestamp = System.currentTimeMillis();
		// Schedule 1000 pieces of work
		for(int i=0; i<1000; i++) {
			this.service.queueEgg(this.massEgg,true);
		}
		long duration = System.currentTimeMillis() - timestamp;
		System.out.printf("Scheduling duration: %d\n", duration);
		assertTrue(duration <= allowed);
	}

	/** We need to sleep before running tests to make sure that the thread
	 * has time to see the work and run it. */
	private void guaranteeSleep(long delay) {
		boolean slept = false;
		try {
			while(!slept) {
				Thread.sleep(delay);
				slept = true;
			}
		} catch(InterruptedException ex) { }
	}
	
	private class DummyNestManager extends NestManager {

		public DummyNestManager(Context context) {
			super(context);
		}
		
		// We only need this method to return our dummy Nest
		@Override
		public Nest getNest(int id) {
			return new DummyNest();
		}
	}
	
	private class DummyNest extends Nest {
		public DummyNest() {
			this.protocol = TestProtocol.getInstance();
		}
	}
}
