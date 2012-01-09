package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class StreamPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.stream_prefs);
	}
}
