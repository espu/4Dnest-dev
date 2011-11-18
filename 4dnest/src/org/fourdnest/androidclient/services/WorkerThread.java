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
public abstract class WorkerThread<W extends Work> extends Thread {
	/** Default delay in milliseconds */
	public static final long DEFAULT_DELAY = 1000;
	
	/** A thread-safe queue for storing the Work objects */
	protected ConcurrentLinkedQueue<W> queue;
	/** Set this to false to cause the thread to exit */
	protected boolean running;
	/** The time in milliseconds to sleep between polls of the queue */ 
	protected long delay = DEFAULT_DELAY;
	
	/**
	 * @param threadName A name passed on to Thread
	 * @param queue The queue used to schedule work to this WorkerThread.
	 * Adding a Work object into the queue schedules it for execution.
	 * The caller should therefore keep a reference to the queue in order to
	 * communicate with the WorkerThread.
	 */
	public WorkerThread(String threadName, ConcurrentLinkedQueue<W> queue) {
		super(threadName);
		this.queue = queue;
		this.running = true;
	}
	
	/**
	 * @param delay The time in milliseconds to sleep between polls of the queue
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	@Override
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
        notifyAll();
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
	 * 
	 * Default implementation calls doWork() on the work object. This will
	 * probably be enough for most needs.
	 * @param work The work to be performed.
	 */
	protected void doWork(W work) {
		work.doWork();
	}
}
