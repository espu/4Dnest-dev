package org.fourdnest.androidclient.services;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.NestManager;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * A service that manages Eggs waiting to be sent to a Nest.
 * Eggs can be marked for immediate sending or queued in waiting
 * for user confirmation or cancellation. The queue of Eggs waiting
 * for confirmation cannot be reordered, but Eggs can be manually sent from it
 * out of sequence.
 */
public class SendQueueService extends Service {
	
	/** Tag string used to indicate source in logging */	
	public static final String TAG = SendQueueService.class.getSimpleName();

	public static final String SEND_EGG = "SEND_EGG_CATEGORY";
	
	public static final String BUNDLE_EGG_CAPTION = "BUNDLE_EGG_CAPTION";
	public static final String BUNDLE_EGG_LOCALFILEURI = "BUNDLE_EGG_LOCALFILEURI";
	
	private FourDNestApplication app;
	private SendQueueWorkerThread thread;
	private ConcurrentLinkedQueue<Work> workQueue;
	private ConcurrentLinkedQueue<Egg> waitingForConfirmation;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.app = (FourDNestApplication) getApplication();
		
		this.workQueue = new ConcurrentLinkedQueue<Work>();
		this.waitingForConfirmation = new ConcurrentLinkedQueue<Egg>();
		
		this.thread = new SendQueueWorkerThread(this.workQueue);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand called");
		
		
		if(intent.hasCategory(SEND_EGG)) {
			Egg egg = new Egg();
			
			egg.setCaption(intent.getStringExtra(BUNDLE_EGG_CAPTION));
			egg.setLocalFileURI((Uri)intent.getSerializableExtra(BUNDLE_EGG_LOCALFILEURI));
			
			this.queueEgg(egg, true);
			
		}
		
		if(!this.thread.isAlive()) {
			this.thread.start();
		}

		
		return START_FLAG_REDELIVERY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
		
	
	/**
	 * Called before Service is killed. 
	 */
	@Override
	public void onDestroy() {
		this.thread.dispose();
		super.onDestroy();
	}
	
	/**
	 * Queues an Egg for sending. Handles Egg meta data and saves it to database.
	 * @param egg The Egg to be queued. May not be null.
	 * @param autosend If true, the Egg will be sent immediately. If false,
	 * it will be placed in a separate queue waiting user confirmation.
	 */
	public void queueEgg(Egg egg, boolean autosend) {
		assert(egg != null);
		
		FourDNestApplication app = (FourDNestApplication) this.getApplication(); 
		
		// Set meta data
		Nest currentNest = app.getCurrentNest();
		egg.setNestId(currentNest.getId());
		egg.setAuthor(currentNest.getUserName());
		
		// Save
		egg = app.getEggManager().saveEgg(egg);
		
		// Add to queue
		this.workQueue.add(new QueuedEgg(egg, autosend));
	}
	/**
	 * Removes an Egg that awaits user confirmation.
	 * @param egg The Egg to be removed. May not be null.
	 */
	public void removeQueuedEgg(Egg egg) {
		assert(egg != null);
		this.workQueue.add(new ConfirmEggInQueue(egg, false));
	}
	/**
	 * Sends one Egg that awaits user confirmation, out of sequence.
	 * @param egg The Egg to be sent. May not be null.
	 */
	public void sendQueuedEgg(Egg egg) {
		assert(egg != null);
		this.workQueue.add(new ConfirmEggInQueue(egg, true));
	}
	/**
	 * Sends all Eggs that await user confirmation.
	 */
	public void sendAllQueuedEggs() {
		Egg egg;
		// drain the waitingForConfirmation queue
		while((egg = SendQueueService.this.waitingForConfirmation.poll()) != null) {
			// put it back in the send queue, with autosend on
			SendQueueService.this.queueEgg(egg, true);
		}	
	}
	
	private class QueuedEgg implements Work {
		private Egg egg;
		private boolean autosend;
		public QueuedEgg(Egg egg, boolean autosend) {
			this.egg = egg;
			this.autosend = autosend;
		}
		public void doWork() {
			if(this.autosend) {
				Nest nest = ((FourDNestApplication)SendQueueService.this.getApplication()).getNestManager().getNest(
					this.egg.getNestId()
				);
				if(nest != null) {
					nest.getProtocol().sendEgg(egg);
				} else {
					Log.d(TAG, "Tried to send Egg to nonexisting nest " + this.egg.getNestId());
				}
			} else {
				SendQueueService.this.waitingForConfirmation.add(this.egg);
			}
		}
	}
	
	private class ConfirmEggInQueue implements Work {
		private Egg egg;
		private boolean send;
		public ConfirmEggInQueue(Egg egg, boolean send) {
			this.egg = egg;
			this.send = send;
		}
		public void doWork() {
			if(
					SendQueueService.this.waitingForConfirmation.remove(this.egg) &&
					this.send
			) {
				// put it back in the send queue, with autosend on
				SendQueueService.this.queueEgg(this.egg, true);
			}
		}
	}
	
	private static class SendQueueWorkerThread extends WorkerThread<Work> {
		public SendQueueWorkerThread(
				ConcurrentLinkedQueue<Work> queue
		) {
			super("SendQueue", queue);
		}

		@Override
		protected void doPeriodically() {
		}

	}

	/**
	 * @param delay The time in milliseconds to sleep between polls of the work queue
	 */
	public void setDelay(long delay) {
		this.thread.setDelay(delay);
	}

	
	

}
