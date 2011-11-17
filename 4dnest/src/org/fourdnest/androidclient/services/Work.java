package org.fourdnest.androidclient.services;

/**
 * A Work objects represents one unit of work that can be passed on for later
 * execution, to a service with a WorkerThread.
 * 
 * The Work object should contain (references to)
 * all the data and logic it needs to perform the work itself. 
 */
public interface Work {
	/**
	 * This method is called when the WorkerThread has selected this piece of
	 * Work to be performed next. The Work object will be discarded after this
	 * method returns.
	 */
	public void doWork();
}
