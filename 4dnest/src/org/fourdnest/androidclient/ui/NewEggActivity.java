package org.fourdnest.androidclient.ui;

import java.io.File;
import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.services.SendQueueService;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class NewEggActivity extends Activity{
	
	/*
	 * currentMediaItemType is used to track what media item is selected
	 * (if any). This decides what UI elements to show.
	 */
	
	private enum mediaItemType{
		none, image, video, audio, multiple //note that multiple is currently not used
	}
	private mediaItemType currentMediaItem = mediaItemType.none;
	private static final int SELECT_PICTURE = 1; //this is needed for selecting picture
	private static final int RESULT_OK = -1; // apparently its -1... dunno

	private String pictureURL = "";
	private String realPictureURL = "";
	private String selectedImagePath;
	private String filemanagerstring;



	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_egg_view);
		this.getApplicationContext();
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null)
		{
		pictureURL = extras.getString("pictureURL");
		}


		ImageView thumbNailView = (ImageView) this.findViewById(R.id.new_photo_egg_thumbnail_view);
		File imgFile = new  File(pictureURL);
		if(imgFile.exists()){
			realPictureURL = imgFile.getAbsolutePath();
		    Bitmap myBitmap = BitmapFactory.decodeFile(realPictureURL);
		    thumbNailView.setImageBitmap(myBitmap);

		}
		/*
		 * Adds a onClickListener to the image so we know when to open a thumbnail
		 */
		
        thumbNailView.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // in onCreate or any event where your want the user to
                // select a file
            	System.out.println("picture url is: xzy  " +realPictureURL);
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	/*
            	 * LEET HACKS, needs file:// to the front or will crash !!!!!!!!!
            	 */
            	i.setDataAndType(Uri.parse("file://"+realPictureURL), "image/jpeg");
            	startActivity(i);
            }
        });
	
        Button sendButton = (Button) findViewById(R.id.new_photo_egg_send_egg_button);
        sendButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				SendQueueService sendQueService = ((FourDNestApplication)getApplication()).getSendQueueService();
				
				//TODO: Proper implementation
				Egg egg = new Egg();
				egg.setAuthor("Gandalf_41");
				egg.setCaption(((EditText)findViewById(R.id.new_photo_egg_caption_view)).getText().toString());
				egg.setLocalFileURI(Uri.parse("file://"+realPictureURL));
				egg.setTags(new ArrayList<Tag>());
				
				sendQueService.queueEgg(egg, true);
			}
		});
        
		/*
		* This onClick listener is used to open up the image gallery
		* so user can select a new picture.
		 */
        
    	((ImageButton) this.findViewById(R.id.select_image))
		.setOnClickListener(new OnClickListener() {


			
			public void onClick(View arg0) {
				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);

			}
		});		
	}

	/*
	 * This method is used once image has been selected. 
	 */
	
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
			//Intent myIntent = new Intent(this.getApplicationContext(),
			//		NewEggActivity.class);
			//myIntent.putExtra("pictureURL", imagePath);
			//startActivityForResult(myIntent, 0);
		}
	}

	// UPDATED!
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };

		CursorLoader loader = new CursorLoader(this.getApplicationContext(), uri,
				projection, null, null, null);
		Cursor cursor = loader.loadInBackground();
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(columnIndex);
		} else {
			return null;
		}
	}
	
	
}
	


