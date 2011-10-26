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
		this.testService.scheduleWork();
		/* We need to sleep before running tests to make sure that the thread
		 * has time to see the work and run it. */
		boolean slept = false;
		try {
			while(!slept) {
				Thread.sleep(2000);
				slept = true;
			}
		} catch(InterruptedException ex) { }
	}

	@After
	public void tearDown() throws Exception {
		this.testService.stop();
	}

	@Test
	public void testPeriodicalsLtZero() {
		assertTrue(this.testService.getPeriodicals() > 0);
	}

	@Test
	public void testWorksEqThree() {
		assertEquals(this.testService.getWorks(), 3);
	}

}
