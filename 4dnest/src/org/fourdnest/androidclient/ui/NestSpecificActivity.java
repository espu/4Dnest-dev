package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Activity that places a button on the top of the screen. The button displays
 * the name of the current nest. Pressing the button sends the user back to
 * ListStreamActivity.
 */
public abstract class NestSpecificActivity extends Activity {

	protected ViewGroup contentView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nest_specific_view);
		
		inflateNestView();

		findViewById(R.id.nest_button).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(v.getContext(),
								ListStreamActivity.class);
						v.getContext().startActivity(intent);
					}
				});

		this.contentView = (FrameLayout) findViewById(R.id.content_view);

		LayoutInflater inflater = LayoutInflater.from(contentView.getContext());
		View view = inflater.inflate(getLayoutId(), contentView, false);
		contentView.addView(getContentLayout(view));
	}

	/**
	 * Initializes the main content view. The main content view is the entire
	 * area under the Nest display.
	 * 
	 * @param view
	 *            The ViewGroup where you want to display your functional
	 *            content.
	 */
	public abstract View getContentLayout(View view);

	/**
	 * Returns the id of the layout definition that is used to fill the content
	 * layout. Acquired with R.layout.your_layout_id.
	 * 
	 * @return An integer representing the id of the specified layout.
	 */
	public abstract int getLayoutId();

	/**
	 * Inflates the nest_view in nest_specific_view with the appropriate view,
	 * depending on kiosk settings.
	 */
	private void inflateNestView() {
		FrameLayout nestView = (FrameLayout) findViewById(R.id.nest_view);
		LayoutInflater inflater = LayoutInflater.from(nestView.getContext());
		
		if (((FourDNestApplication)getApplication()).getKioskModeEnabled()) {
			inflater.inflate(R.layout.nest_view_kiosk_disabled, nestView);
		} else {
			inflater.inflate(R.layout.nest_view_kiosk_disabled, nestView);
		}
//		nestView.addView(child);
	}

}