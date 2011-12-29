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
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
	private static final int SELECT_AUDIO = 2;
	private static final int SELECT_VIDEO = 3;

	private static final int RESULT_OK = -1; // apparently its -1... dunno
	
	

	private String fileURL = "";
	private String realFileURL = "";
	private String selectedFilePath;
	private String filemanagerstring;
	private ImageView thumbNailView;
	private RelativeLayout upperButtons;


	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_egg_view);
		this.getApplicationContext();
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null)
		{
		fileURL = extras.getString("pictureURL");
		}

		this.upperButtons = (RelativeLayout) this.findViewById(R.id.new_egg_upper_buttons);
		this.thumbNailView = (ImageView) this.findViewById(R.id.new_photo_egg_thumbnail_view);
		/*
		 * Adds a onClickListener to the image so we know when to open a thumbnail
		 */
		
        thumbNailView.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // in onCreate or any event where your want the user to
                // select a file
            	System.out.println("picture url is: xzy  " +realFileURL);
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	/*
            	 * LEET HACKS, needs file:// to the front or will crash !!!!!!!!!
            	 */
            	
            	if(currentMediaItem==mediaItemType.image){
                	i.setDataAndType(Uri.parse("file://"+realFileURL), "image/*");

            	}
            	else if(currentMediaItem==mediaItemType.audio){
            	i.setDataAndType(Uri.parse("file://"+realFileURL), "audio/*");
            	}
            	else if(currentMediaItem==mediaItemType.video){
                	i.setDataAndType(Uri.parse("file://"+realFileURL), "video/*");

            	}
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
				egg.setLocalFileURI(Uri.parse("file://"+realFileURL));
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
    	
       	((ImageButton) this.findViewById(R.id.select_audio))
    		.setOnClickListener(new OnClickListener() {
    			public void onClick(View arg0) {
    				// in onCreate or any event where your want the user to
    				// select a file
    				Intent intent = new Intent();
    				intent.setType("audio/*");
    				intent.setAction(Intent.ACTION_GET_CONTENT);
    				intent.addCategory(Intent.CATEGORY_OPENABLE);
    				startActivityForResult(
    						Intent.createChooser(intent, "Select Audio"),
    						SELECT_AUDIO);

    			}
    		});
       	
       	((ImageButton) this.findViewById(R.id.select_video))
    		.setOnClickListener(new OnClickListener() {
    			public void onClick(View arg0) {
    				// in onCreate or any event where your want the user to
    				// select a file
    				Intent intent = new Intent();
    				intent.setType("video/*");
    				intent.setAction(Intent.ACTION_GET_CONTENT);
    				intent.addCategory(Intent.CATEGORY_OPENABLE);
    				startActivityForResult(
    						Intent.createChooser(intent, "Select Video"),
    						SELECT_VIDEO);

    			}
    		});	
       	
	}


	/*
	 * Used to refresh the elements displayed when an media item is selected / unselected
	 */
	
	private void refreshElements(){
		
		/*
		 *  I used to have this work with a switch, but it didn't work for some strange reason
		 */
		
			if (this.currentMediaItem == mediaItemType.image){ //image has been selected, we hide the selection buttons and show the preview thumbnail
				upperButtons.setVisibility(View.GONE);
				thumbNailView.setVisibility(View.VISIBLE);
				ScrollView scrollView = (ScrollView) this.findViewById(R.id.new_egg_scroll_view);
				File imgFile = new  File(fileURL);
				if(imgFile.exists()){
					realFileURL = imgFile.getAbsolutePath();
				    Bitmap myBitmap = BitmapFactory.decodeFile(realFileURL);
				    thumbNailView.setImageBitmap(myBitmap);
				}
				scrollView.postInvalidate(); //should cause a redraw.... should!
			}
			else if(this.currentMediaItem == mediaItemType.none){
				thumbNailView.setVisibility(View.VISIBLE);	
				upperButtons.setVisibility(View.GONE);
			}
			else if (this.currentMediaItem == mediaItemType.audio){
			thumbNailView.setVisibility(View.VISIBLE);
			upperButtons.setVisibility(View.GONE);
			thumbNailView.setImageResource(R.drawable.note1);
			File audioFile = new  File(fileURL);
			realFileURL = audioFile.getAbsolutePath();
		}
			
			else if (this.currentMediaItem == mediaItemType.video){
			thumbNailView.setVisibility(View.VISIBLE);
			upperButtons.setVisibility(View.GONE);
			thumbNailView.setImageResource(R.drawable.roll1);
			File audioFile = new  File(fileURL);
			realFileURL = audioFile.getAbsolutePath();
		}
	
	}
	
	/*
	 * This method is used once image has been selected. 
	 */
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && (requestCode == SELECT_PICTURE || requestCode == SELECT_AUDIO || requestCode == SELECT_VIDEO)) {
			Uri selectedImageUri = data.getData();

			// OI FILE Manager
			filemanagerstring = selectedImageUri.getPath();

			// MEDIA GALLERY
			selectedFilePath = getPath(selectedImageUri);
			
			// NOW WE HAVE OUR WANTED STRING
			String filePath = "";
			if (selectedFilePath != null) {
				filePath = selectedFilePath;
				System.out
						.println("selectedImagePath is the right one for you!");
			} else {
				filePath = filemanagerstring;
				System.out
						.println("filemanagerstring is the right one for you!");
			}
			
			/*
			 * Getting the file url is the same for all  
			 */

			if(requestCode == SELECT_PICTURE){
			this.currentMediaItem = mediaItemType.image;
			}
			else if(requestCode == SELECT_AUDIO){
				this.currentMediaItem = mediaItemType.audio;
			}
			else if(requestCode == SELECT_VIDEO){
				this.currentMediaItem = mediaItemType.video;
			}
			this.fileURL = filePath;
			this.refreshElements();
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
	

