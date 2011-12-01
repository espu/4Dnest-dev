package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Activity for displaying the current Nest at the top of the screen.
 */
public abstract class NestSpecificActivity extends Activity {

	protected ViewGroup contentView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		this.contentView = (FrameLayout)findViewById(R.id.content_view);
		
		LayoutInflater inflater = LayoutInflater.from(contentView.getContext());
		View view = inflater.inflate(getLayoutId(), contentView, false);
		contentView.addView(getContentLayout(view));
	}
	
	/**
	 * Sets the main content view. The main content view is the entire area under the Nest display.
	 * @param view The ViewGroup where you want to display your functional content.
	 */
	public abstract View getContentLayout(View view);
	public abstract int getLayoutId();
}
