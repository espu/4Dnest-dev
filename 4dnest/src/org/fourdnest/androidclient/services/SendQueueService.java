package org.fourdnest.androidclient.services;
import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.comm.ProtocolResult;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * A service for sending Egg to it's specified Nest in the background.
 * Jobs are sent via Intent (use sendEgg convenience method) and queued
 * for sending automatically. 
 */
public class SendQueueService extends IntentService {
	
	/** Tag string used to indicate source in logging */	
	public static final String TAG = SendQueueService.class.getSimpleName();
	
	/** Intent should have this category when Egg is meant to be extracted from extras */
	public static final String SEND_EGG = "SEND_EGG_CATEGORY";
	/** Key for Egg id in Intent extras */
	public static final String BUNDLE_EGG_ID = "BUNDLE_EGG_ID";
	
	/** Internal Handler for displaying Toast after job completes */
	private Handler handler;
	
	/**
	 * Constructor, simply calls super. Never used explicitly in user code.
	 */
	public SendQueueService() {
		super(SendQueueService.class.getName());
	}
	/**
	 * Constructor, simply calls super. Never used explicitly in user code.
	 * @param name
	 */
	public SendQueueService(String name) {
		super(name);
	}
	
	/**
	 * Override for default onCreate method. Creates a Handler for Toasts
	 * and calls super()
	 */
	@Override
	public void onCreate() {
		this.handler = new Handler();
		super.onCreate();
	}
	
	/**
	 * Static method to be called with an Egg that needs to be put
	 * to the send queue. Handles all the ugly details.
	 * 
	 * @param context Current application context
	 * @param egg Egg to be put to queue
	 */
	public static void sendEgg(Context context, Egg egg) {
		FourDNestApplication app = (FourDNestApplication)context; 
		
		Nest currentNest = app.getCurrentNest();		
		if(currentNest == null) {
			Toast.makeText(app, "Active nest not set, item not queued", Toast.LENGTH_SHORT);
		}
		
		egg.setAuthor(currentNest.getUserName());
		egg.setNestId(currentNest.getId());
		Egg savedEgg = app.getEggManager().saveEgg(egg);
		
		Intent intent = new Intent(context, SendQueueService.class);
		intent.addCategory(SendQueueService.SEND_EGG);
		intent.putExtra(SendQueueService.BUNDLE_EGG_ID, savedEgg.getId());

		context.startService(intent);
		Toast.makeText(context, context.getString(R.string.egg_queued), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Default Intent handler for IntentService. All Intents get sent here.
	 * Extracts Egg id from Intent extras, finds an Egg with that id and
	 * sends it to the Nest specified in the Egg. 
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent called");
		
		// If Intent it categorized as egg-sending intent, act accordingly 
		if(intent.hasCategory(SEND_EGG)) {			
			FourDNestApplication app = (FourDNestApplication) this.getApplication();
			
			// Get Egg id from Intent, fetch the Egg from db
			int eggId = intent.getIntExtra(BUNDLE_EGG_ID, -1);
			Egg egg = app.getEggManager().getEgg(eggId);
			
			if(egg == null) {
				Log.d(TAG, "Egg with id " + eggId + " not found");
			} else {
				ProtocolResult res = app.getNestManager().getNest(egg.getNestId()).getProtocol().sendEgg(egg);
				if(res.getStatusCode() == res.RESOURCE_UPLOADED) {
					this.handler.post(new ToastDisplay(app, getString(R.string.egg_send_complete), Toast.LENGTH_SHORT));
					Log.d(TAG, "Send completed");
				} else {
					String message;
					
					switch (res.getStatusCode()) {
					case ProtocolResult.AUTHORIZATION_FAILED:
						message = "Authorization failed";
						break;
					case ProtocolResult.SENDING_FAILED:
						message = "Sending failed";
						break;
					case ProtocolResult.SERVER_INTERNAL_ERROR:
						message = "Server internal error";
						break;
					case ProtocolResult.UNKNOWN_REASON:
						message = "Unknown failure";
						break;
					default:
						message = "Unknown result";
						break;
					}
					this.handler.post(new ToastDisplay(app, "Send failed: " + message, Toast.LENGTH_SHORT));
					Log.d(TAG, "Send failed: " + res.getStatusCode());
				}
				
				
			}
		}
		
		
	}
	
	/**
	 * Private class to display Toasts in main thread
	 */
	private class ToastDisplay implements Runnable {
		String message;
		Context context;
		int duration;
		
		/**
		 * Runnable ToastDisplayer, added to a Handler to be run at a later time or
		 * in a different thread
		 * @param context Target context to display Toast in
		 * @param message Message content
		 * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
		 */
		public ToastDisplay(Context context, String message, int duration) {
			this.context = context;
			this.message = message;
			this.duration = duration;
		}
		
		/**
		 * Called when Runnable is executed. displays Toast as specified when object was created
		 */
		public void run() {
			Toast.makeText(this.context, this.message, this.duration).show();
		}
	
	}
}
