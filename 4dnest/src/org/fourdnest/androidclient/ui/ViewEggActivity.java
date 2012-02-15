package org.fourdnest.androidclient.ui;

import java.text.DateFormat;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.ThumbnailManager;
import org.fourdnest.androidclient.comm.FourDNestProtocol;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
	private ImageView thumbnail;

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

		timestamp.setText(DateFormat.getDateTimeInstance().format(egg.getCreationDate()));
		message.setText(egg.getCaption());
		
		if (egg.getMimeType() != Egg.fileType.TEXT) {
			this.thumbnail = (ImageView) findViewById(R.id.file_thumbnail);
			new ThumbnailTask().execute(egg);
		}
		List<Tag> tagList = egg.getTags();
		if (tagList.size() > 0) { // if there are no tags, leave default message
			// (no tags)
			tags.setText(EggAdapter.tagListToString(tagList));
		}
				
		final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapViewActivity.class);
                intent.putExtra(MapViewActivity.EGG_ID, ViewEggActivity.this.eggID);
                v.getContext().startActivity(intent);
            }
        });
		
		//TODO: Inflate based on egg type

//		FrameLayout mediaView = (FrameLayout) findViewById(R.id.media_view);
//		LayoutInflater inflater = LayoutInflater.from(mediaView.getContext());
//		View view = inflater.inflate(R.layout.egg_element_large, mediaView, false);
//		mediaView.addView(view);

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
			startActivity(this.application.helpBrowserIntent());
			return true;
		/*
		// FIXME Actually these would be better as on-screen buttons instead of menu buttons
		case R.id.menu_view_edit:
			//FIXME unimplemented, button hidden. Uncomment from xml when implemented
			return true;
		case R.id.menu_view_delete:
			//FIXME unimplemented, button hidden. Uncomment from xml when implemented
			return true;
		*/
		}
		return false;
	}

	/**
	 * Asynchronous task for background retrieval of larger thumbnail
	 */
	private class ThumbnailTask extends AsyncTask<Egg, Void, Void> {
		protected Void doInBackground(Egg... eggs) {
			Egg egg = eggs[0]; 
			application.getCurrentNest().getProtocol()
			.getThumbnail(egg, FourDNestProtocol.THUMBNAIL_SIZE_LARGE);
			final Uri thumbUri = Uri.parse(ThumbnailManager.getThumbnailUriString(
					egg,
					FourDNestProtocol.THUMBNAIL_SIZE_LARGE
			));
			
			// Android allows only the UI thread to touch views
			runOnUiThread(new Runnable() {
			     public void run() {
			    	 thumbnail.setImageURI(thumbUri);
			    }
			});

			return null;	// because Void is an Android placeholder, not true void
		}

		protected void onProgressUpdate() {
		}

		protected void onPostExecute() {
		}
	 }

}
