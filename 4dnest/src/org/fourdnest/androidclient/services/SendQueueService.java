package org.fourdnest.androidclient.services;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.NestManager;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
	
	public static final String BUNDLE_EGG_ID = "BUNDLE_EGG_ID";
	
	private FourDNestApplication app;
	private SendQueueWorkerThread thread;
	private ConcurrentLinkedQueue<Work> workQueue;
	private ConcurrentLinkedQueue<Egg> waitingForConfirmation;
	
	/**
	 * Static method to be called with an Egg that needs to be put
	 * to the send queue.
	 * 
	 * @param context Current application context
	 * @param egg Egg to be put to queue
	 */
	public static void sendEgg(Context context, Egg egg) {
		Intent intent = new Intent(context, SendQueueService.class);
		
		FourDNestApplication app = (FourDNestApplication) context;
		Egg savedEgg = app.getEggManager().saveEgg(egg);
		
		intent.addCategory(SendQueueService.SEND_EGG);
		intent.putExtra(SendQueueService.BUNDLE_EGG_ID, egg.getId());
		
		context.startService(intent);		
	}
	
	/**
	 * Overrides basic Service onCreate method. Sets up necessary background
	 * items.
	 * 
	 * Should never be called directly.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.app = (FourDNestApplication) getApplication();
		
		this.workQueue = new ConcurrentLinkedQueue<Work>();
		this.waitingForConfirmation = new ConcurrentLinkedQueue<Egg>();
		
		this.thread = new SendQueueWorkerThread(this.workQueue);
	}
	
	/**
	 * Overrides basic Service onStartComand method. Reacts to received intents.
	 * Core functionality is reacting to received Egg send requests.
	 * 
	 * Should never be called directly. 
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand called");
		
		// If Intent it categorized as egg-sending intent, act accordingly 
		if(intent.hasCategory(SEND_EGG)) {
			
			// Get Egg id from Intent, fetch the Egg from db
			int eggId = intent.getIntExtra(BUNDLE_EGG_ID, -1);
			Egg egg = this.app.getEggManager().getEgg(eggId);
			
			
			if(egg != null) {
				// Add meta data and re-save
				Nest currentNest = app.getCurrentNest();
				egg.setNestId(currentNest.getId());
				egg.setAuthor(currentNest.getUserName());			
				this.app.getEggManager().saveEgg(egg);
			
				this.queueEgg(egg, true);
			} else {
				Log.e(TAG, "Tried sending an unsaved egg with id " + eggId);
			}
			
		}
		
		if(!this.thread.isAlive()) {
			this.thread.start();
		}
		
		// Apparently critical return value. Not sure why exactly. :p 
		return START_FLAG_REDELIVERY;
	}
	

	/**
	 * Dummy override for default onBind. Returns null to signal
	 * that this service cannot be bound.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
		
	
	/**
	 * Overrides basic Service onDestroy method. Disposes of background
	 * items safely.
	 * 
	 * Should never be called directly. 
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
	private void queueEgg(Egg egg, boolean autosend) {
		assert(egg != null);
		this.workQueue.add(new QueuedEgg(egg, autosend));
	}
	/**
	 * Removes an Egg that awaits user confirmation.
	 * @param egg The Egg to be removed. May not be null.
	 */
	private void removeQueuedEgg(Egg egg) {
		assert(egg != null);
		this.workQueue.add(new ConfirmEggInQueue(egg, false));
	}
	/**
	 * Sends one Egg that awaits user confirmation, out of sequence.
	 * @param egg The Egg to be sent. May not be null.
	 */
	private void sendQueuedEgg(Egg egg) {
		assert(egg != null);
		this.workQueue.add(new ConfirmEggInQueue(egg, true));
	}
	/**
	 * Sends all Eggs that await user confirmation.
	 */
	private void sendAllQueuedEggs() {
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
