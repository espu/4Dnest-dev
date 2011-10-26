package org.fourdnest.androidclient.services;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The WorkerThread is a system for passing units of work to a separate thread
 * in Services, in order to ensure smooth running of UI thread. Communication
 * with the WorkerThread happens through a @see ConcurrentLinkedQueue, which
 * is passed to the constructor.
 * @param <W> WorkerThread and its ConcurrentLinkedQueue are parameterized to
 * ensure that only work that is understood by the Service can be scheduled.
 */
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

	/**
	 * Instructs the Thread to stop looping and exit. 
	 */
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
