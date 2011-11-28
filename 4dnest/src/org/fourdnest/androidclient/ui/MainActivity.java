package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ViewPager awesomePager = (ViewPager) findViewById(R.id.awesomepager);
		FragAdapter fragadapter = new FragAdapter(getSupportFragmentManager(), this.getApplicationContext());
		awesomePager.setAdapter(fragadapter);
		awesomePager.setCurrentItem(1);
		this.getApplicationContext();
	}
}