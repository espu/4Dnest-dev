package org.fourdnest.androidclient.services;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.fourdnest.androidclient.Egg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SendQueueService extends Service {
	private SendQueueWorkerThread thread;
	private ConcurrentLinkedQueue<QueuedEgg> workQueue;
	private ConcurrentLinkedQueue<QueuedEgg> waitingForConfirmation;

	public SendQueueService() {
		this.workQueue = new ConcurrentLinkedQueue<QueuedEgg>();
		this.waitingForConfirmation = new ConcurrentLinkedQueue<QueuedEgg>();
		this.thread = new SendQueueWorkerThread(this.workQueue);
		this.thread.start();
	}

	public void queueEgg(Egg egg, boolean autosend) {
		this.workQueue.add(new QueuedEgg(egg, autosend));
	}
	
	private class QueuedEgg {
		@SuppressWarnings("unused")
		private Egg egg;
		private boolean autosend;
		public QueuedEgg(Egg egg, boolean autosend) {
			this.egg = egg;
			this.autosend = autosend;
		}
	}
	
	private class SendQueueWorkerThread extends WorkerThread<QueuedEgg> {
		public SendQueueWorkerThread(
				ConcurrentLinkedQueue<QueuedEgg> queue
		) {
			super("SendQueue", queue);
		}

		@Override
		protected void doPeriodically() {
		}

		@Override
		protected void doWork(QueuedEgg work) {
			if(work.autosend) {
				// FIXME implement when Protocol is available
			} else {
				SendQueueService.this.waitingForConfirmation.add(work);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
