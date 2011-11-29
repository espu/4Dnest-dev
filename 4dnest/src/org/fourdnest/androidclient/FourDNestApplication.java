package org.fourdnest.androidclient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Manages shared resources, including the preferences and singletons like
 * services and databases.
 * The application object is accessible from any part of the program which 
 * has access to the application context, through
 * FourDNestApplication application = (FourDNestApplication) getApplication() 
 */
public class FourDNestApplication extends Application
	implements OnSharedPreferenceChangeListener {
	private static final String TAG = FourDNestApplication.class.getSimpleName();
	
	private SharedPreferences prefs;
	private NestManager nestManager;
	
	@Override
	public void onCreate() { //
	  super.onCreate();
	  this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	  this.prefs.registerOnSharedPreferenceChangeListener(this);
	  Log.i(TAG, "onCreated");
	}

	/**
	 * Gets the NestManager singleton.
	 * The NestManager object synchronizes itself, so the caller is free to store
	 * the object.  
	 * @return
	 */
	public NestManager getNestManager() {
		if(this.nestManager == null) {
			this.nestManager = new NestManager(this);
		}
		return this.nestManager;
	}

	public synchronized void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences, String key) {
		this.getApplicationContext();
		// TODO Auto-generated method stub
	}

	/**
	 * Gets the id of the currently active Nest.
	 * @return The id of the currently active Nest. 
	 */
	public synchronized String getCurrentNestId() {
		return this.prefs.getString("currentNestId", "");
	}
	/**
	 * Sets the currently active Nest. The setting is persisted between
	 * restarts of the application.
	 * @param newNestId Id of the new active Nest. Must be a valid Nest id.
	 */
	public synchronized void setCurrentNestId(String newNestId) {
		//FIXME check that newNestId is valid?
		SharedPreferences.Editor prefEditor = this.prefs.edit();
		prefEditor.putString("currentNestId", newNestId);
	}
}
