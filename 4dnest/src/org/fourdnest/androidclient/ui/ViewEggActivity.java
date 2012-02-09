package org.fourdnest.androidclient.ui;

import java.util.Date;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.FourDNestProtocol;
import org.fourdnest.androidclient.comm.ThumbnailManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewEggActivity extends NestSpecificActivity {

	private int eggID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.application = (FourDNestApplication) getApplication();
		
		Bundle startingExtras = getIntent().getExtras();
		this.eggID = (Integer) startingExtras
				.get(EggItemOnClickListener.INTENT_EGG_ID);

		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.egg_view);

		Egg egg = this.application.getStreamEggManager().getEgg(eggID);

		TextView timestamp = (TextView) findViewById(R.id.timestamp);
		TextView message = (TextView) findViewById(R.id.message);
		TextView tags = (TextView) findViewById(R.id.tags);
		ImageView thumbnail= (ImageView) findViewById(R.id.file_thumbnail);

		timestamp.setText(new Date(egg.getLastUpload()).toString());
		message.setText(egg.getCaption());
		thumbnail.setImageURI(Uri.parse(ThumbnailManager.getThumbnailUriString(egg, FourDNestProtocol.THUMBNAIL_SIZE_LARGE)));
		if (!egg.getTags().isEmpty()) {
			String tagList = "";
			for (Tag current : egg.getTags()) {
				tagList += current.getName() + " ";
			}
			tags.setText(tagList);
		}
		
		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MapViewActivity.class);
				v.getContext().startActivity(intent);
			}
		});

		// TODO: Inflate based on egg type

		// FrameLayout mediaView = (FrameLayout) findViewById(R.id.media_view);
		// LayoutInflater inflater =
		// LayoutInflater.from(mediaView.getContext());
		// View view = inflater.inflate(R.layout.egg_element_large, mediaView,
		// false);
		// mediaView.addView(view);

		super.onCreate(savedInstanceState);
	}

	/**
	 * Creates the options menu on the press of the Menu button.
	 * 
	 * @param menu
	 *            The menu to inflate
	 * @return Boolean indicating success of creating the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_menu, menu);
		return true;
	}

	/**
	 * Specifies the action to perform when a menu item is pressed.
	 * 
	 * @param item
	 *            The MenuItem that was pressed
	 * @return Boolean indicating success of identifying the item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_pref:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.menu_view_help:
			return true;
		case R.id.menu_view_edit:
			return true;
		case R.id.menu_view_delete:
			return true;
		}
		return false;
	}

}
