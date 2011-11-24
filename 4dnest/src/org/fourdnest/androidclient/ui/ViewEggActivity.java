package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ViewEggActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.egg_view);
		
		FrameLayout mediaView = (FrameLayout) findViewById(R.id.media_view);
		
		LayoutInflater inflater = LayoutInflater.from(mediaView.getContext());
		
		//TODO: Inflate based on egg type
		View view = inflater.inflate(R.layout.egg_element_large, mediaView, false);

		mediaView.addView(view);

	}

}
