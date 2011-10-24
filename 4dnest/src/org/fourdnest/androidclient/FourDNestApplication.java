package org.fourdnest.androidclient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;


public class FourDNestApplication extends Application
	implements OnSharedPreferenceChangeListener {
	private static final String TAG = FourDNestApplication.class.getSimpleName();
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() { //
	  super.onCreate();
	  this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	  this.prefs.registerOnSharedPreferenceChangeListener(this);
	  Log.i(TAG, "onCreated");
	}

	public synchronized void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		
	}

	public synchronized String getCurrentNestId() {
		return this.prefs.getString("currentNestId", "");
	}
	public synchronized void setCurrentNestId(String newNestId) {
		//FIXME check that newNestId is valid?
		SharedPreferences.Editor prefEditor = this.prefs.edit();
		prefEditor.putString("currentNestId", newNestId);
	}
}
