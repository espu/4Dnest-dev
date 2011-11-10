package org.fourdnest.androidclient.services;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.fourdnest.androidclient.Egg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A service that manages Eggs waiting to be sent to a Nest.
 * Eggs can be marked for immediate sending or queued in waiting
 * for user confirmation or cancellation. The queue of Eggs waiting
 * for confirmation cannot be reordered, but Eggs can be manually sent from it
 * out of sequence.
 */
public class SendQueueService extends Service {
	private SendQueueWorkerThread thread;
	private ConcurrentLinkedQueue<Work> workQueue;
	private ConcurrentLinkedQueue<Egg> waitingForConfirmation;

	public SendQueueService() {
		this.workQueue = new ConcurrentLinkedQueue<Work>();
		this.waitingForConfirmation = new ConcurrentLinkedQueue<Egg>();
		this.thread = new SendQueueWorkerThread(this.workQueue);
	}	
	
	/**
	 * Starts the service.
	 */
	public void start() {
		this.thread.start();
	}

	/**
	 * Queues an Egg for sending.
	 * @param egg The Egg to be queued.
	 * @param autosend If true, the Egg will be sent immediately. If false,
	 * it will be placed in a separate queue waiting user confirmation.
	 */
	public void queueEgg(Egg egg, boolean autosend) {
		// FIXME: write to database, or is it already done at this point?
		this.workQueue.add(new QueuedEgg(egg, autosend));
	}
	/**
	 * Removes an Egg that awaits user confirmation.
	 * @param egg The Egg to be removed.
	 */
	public void removeQueuedEgg(Egg egg) {
		this.workQueue.add(new ConfirmEggInQueue(egg, false));
	}
	/**
	 * Sends one Egg that awaits user confirmation, out of sequence.
	 * @param egg The Egg to be sent
	 */
	public void sendQueuedEgg(Egg egg) {
		this.workQueue.add(new ConfirmEggInQueue(egg, true));
	}
	/**
	 * Sends all Eggs that await user confirmation.
	 */
	public void sendAllQueuedEggs() {
		//FIXME unimplemented
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
				// FIXME implement when Protocol is available
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
			SendQueueService.this.waitingForConfirmation.remove(this.egg);
			if(this.send) {
				// FIXME implement when Protocol is available
			}
		}
	}
	
	private class SendQueueWorkerThread extends WorkerThread<Work> {
		public SendQueueWorkerThread(
				ConcurrentLinkedQueue<Work> queue
		) {
			super("SendQueue", queue);
		}

		@Override
		protected void doPeriodically() {
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
