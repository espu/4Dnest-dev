package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity for displaying the current Nest at the top of the screen.
 */
public class NestSpecificActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
}
