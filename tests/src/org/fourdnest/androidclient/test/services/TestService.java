package org.fourdnest.androidclient.test.services;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.fourdnest.androidclient.services.WorkerThread;

public class TestService {
	private int periodicals;
	private int works;
	private TestWorkerThread thread;
	private ConcurrentLinkedQueue<TestWork> queue;
	
	public TestService() {
		this.periodicals = 0;
		this.works = 0;
		this.queue = new ConcurrentLinkedQueue<TestWork>();
		this.thread = new TestWorkerThread("TestWorkerThread", this.queue);
		this.thread.setDelay(100);
		this.thread.start();
	}

	public void stop() {
		this.thread.dispose();
	}

	
	public void scheduleWork(String tag) {
		this.queue.add(new TestWork(tag));
	}
	
	public int getPeriodicals() {
		return this.periodicals;
	}
	public int getWorks() {
		return this.works;
	}
	
	private class TestWork {
		private String name;
		public TestWork(String name) {
			this.name = name;
		}
		public void doWork() {
			try {
				// simulating work
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
			TestService.this.works++;
			System.out.printf("Did work %s\n", name);
		}
	}
	
	private class TestWorkerThread extends WorkerThread<TestWork> {

		public TestWorkerThread(String threadName, ConcurrentLinkedQueue<TestWork> queue) {
			super(threadName, queue);
		}

		@Override
		protected void doPeriodically() {
			TestService.this.periodicals++;
			System.out.println("Did periodical");
		}

		@Override
		protected void doWork(TestWork work) {
			work.doWork();
		}
	}
}
