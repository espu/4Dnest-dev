package org.fourdnest.androidclient.services;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class WorkerThread<W> extends Thread {
	protected ConcurrentLinkedQueue<W> queue;
	protected boolean running;
	protected long delay = 1000; //FIXME
	
	public WorkerThread(String threadName, ConcurrentLinkedQueue<W> queue) {
		super(threadName);
		this.queue = queue;
		this.running = true;
	}
	
	public void run() {
		W work;
		while(running) {
			try{
				this.doPeriodically();
				while((work = this.queue.poll()) != null) {
					this.doWork(work);
				}
				sleep(delay);
			} catch(InterruptedException ie) { }
		}
	}

	public synchronized void dispose() {
		running = false;
        notify();
    }
	
	/**
	 * This method is executed on each loop of the thread.
	 * Override this with code that should be performed periodically.
	 * Timing is not reliable: if you need a minimum delay between executions,
	 * you need to keep your own timestamp. Maximum delay between executions
	 * cannot be guaranteed.
	 * 
	 * Default implementation does nothing.
	 */
	protected void doPeriodically() { }
	/**
	 * This method is executed whenever work has been assigned to this thread
	 * through the queue. There is an unspecified delay between assigning the
	 * work and its execution.
	 * @param work The work to be performed.
	 */
	protected abstract void doWork(W work);
}
