package org.fourdnest.androidclient.ui;

import org.fourdnest.androidclient.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CreateScreenActivity extends NestSpecificActivity {

	// YOU CAN EDIT THIS TO WHATEVER YOU WANT
	private static final int SELECT_PICTURE = 1;
	private static final int RESULT_OK = -1; // apparently its -1... dunno

	private String selectedImagePath;
	// ADDED
	private String filemanagerstring;

	// UPDATED
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
			Uri selectedImageUri = data.getData();

			// OI FILE Manager
			filemanagerstring = selectedImageUri.getPath();

			// MEDIA GALLERY
			selectedImagePath = getPath(selectedImageUri);
			
			// NOW WE HAVE OUR WANTED STRING
			String imagePath = "";
			if (selectedImagePath != null) {
				imagePath = selectedImagePath;
				System.out
						.println("selectedImagePath is the right one for you!");
			} else {
				imagePath = filemanagerstring;
				System.out
						.println("filemanagerstring is the right one for you!");
			}
			Intent myIntent = new Intent(this.getApplicationContext(),
					NewEggActivity.class);
			myIntent.putExtra("pictureURL", imagePath);
			startActivityForResult(myIntent, 0);
		}
	}

	// UPDATED!
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };

		CursorLoader loader = new CursorLoader(this.getApplicationContext(), uri,
				projection, null, null, null);
		Cursor cursor = loader.loadInBackground();
		String result = null;
		
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(columnIndex);
		}
		cursor.close();
		
		return result;
	}

	@Override
	public View getContentLayout(View view) {
		((Button) view.findViewById(R.id.openGalleryButton))
		.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("video/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);

			}
		});
		
		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.create_view;
	}
	/**
	 * Creates the options menu on the press of the Menu button.
	 * 
	 * @param menu The menu to inflate
	 * @return Boolean indicating success of creating the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_menu, menu);
		return true;
	}
	
	/**
	 * Specifies the action to perform when a menu item is pressed.
	 * 
	 * @param item The MenuItem that was pressed
	 * @return Boolean indicating success of identifying the item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create_pref:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.menu_create_help:
			return true;
		case R.id.menu_create_discard:
			return true;
		}
		return false;
	}
}
