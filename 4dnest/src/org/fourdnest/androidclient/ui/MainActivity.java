package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private ViewPager awesomePager;
	private FragAdapter fragadapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		awesomePager = (ViewPager) findViewById(R.id.awesomepager);
		fragadapter = new FragAdapter(getSupportFragmentManager(), this.getApplicationContext());
		awesomePager.setAdapter(fragadapter);
		awesomePager.setCurrentItem(1);
		this.getApplicationContext();


	}
}