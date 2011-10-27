package org.fourdnest.androidclient.test.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the work queue of the WorkerThread, using the dummy service
 * @see TestService.
 * @author gronsti
 */
public class WorkQueueTest {
	private TestService testService;

	@Before
	public void setUp() throws Exception {
		this.testService = new TestService();
	}

	@After
	public void tearDown() throws Exception {
		this.testService.stop();
	}

	@Test
	public void testPeriodicalsLtZero() {
		guaranteeSleep(500);
		assertTrue(this.testService.getPeriodicals() > 0);
	}

	@Test
	public void testWorksEqThree() {
		this.testService.scheduleWork("First");
		this.testService.scheduleWork("Second");
		this.testService.scheduleWork("Third");
		guaranteeSleep(500);
		assertEquals(this.testService.getWorks(), 3);
	}
	
	@Test
	public void testFastScheduling() {
		long allowed = 1000;	// Using 1s, real maximum is 10s
		long timestamp = System.currentTimeMillis();
		// Schedule 1000 pieces of work
		for(int i=0; i<1000; i++) {
			this.testService.scheduleWork("FastScheduling");
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
}
