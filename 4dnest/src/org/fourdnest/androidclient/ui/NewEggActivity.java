package org.fourdnest.androidclient.ui;

import java.io.File;
import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.services.SendQueueService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
	protected static final int CAMERA_PIC_REQUEST = 4;
	protected static final int CAMERA_VIDEO_REQUEST = 5;

	private static final int RESULT_OK = -1; // apparently its -1... dunno
	
	/*
	 *  ID values for dialogues
	 */
	static final int DIALOG_ASK_AUDIO = 0;
	static final int DIALOG_ASK_IMAGE = 1;
	static final int DIALOG_ASK_VIDEO = 2;
	//static final int DIALOG_GAMEOVER_ID = 1;
	

	private String fileURL = "";
	private String realFileURL = "";
	private String selectedFilePath;
	private String filemanagerstring;
	private ImageView thumbNailView;
	private RelativeLayout upperButtons;
	private Uri capturedImageURI;
	private TaggingTool taggingTool;

	
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
				//TODO: Proper implementation
				Egg egg = new Egg();
				egg.setAuthor("Saruman_The_White_42");
				egg.setCaption(((EditText)findViewById(R.id.new_photo_egg_caption_view)).getText().toString());
				egg.setLocalFileURI(Uri.parse("file://"+realFileURL));
				egg.setTags(NewEggActivity.this.taggingTool.getCheckedTags());
				SendQueueService.sendEgg(getApplication(), egg);
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
				showDialog(DIALOG_ASK_IMAGE);
				/*Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			*/
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
    				showDialog(DIALOG_ASK_VIDEO);
    			}
    		});
       	
       	LinearLayout inputsLinearLayout = (LinearLayout) this.findViewById(R.id.new_egg_inputs_linearlayout);
       	this.taggingTool = new TaggingTool(this, inputsLinearLayout);
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
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    switch(id) {
	    case DIALOG_ASK_IMAGE:
	    	final CharSequence[] items = {"Open Camera", "Open Picture Gallery"};
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setTitle("Select Source");
	    	builder.setItems(items, new DialogInterface.OnClickListener() {
	    	    public void onClick(DialogInterface dialog, int item) {
	    	    	if(item==1){ //notice the id order is reversed (for no particular reason)
	    	    		Intent intent = new Intent();
	    	    		intent.setType("image/*");
	    	    		intent.setAction(Intent.ACTION_GET_CONTENT);
	    	    		intent.addCategory(Intent.CATEGORY_OPENABLE);
	    	    		startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							SELECT_PICTURE);
	    	    	}
	    	    	else if(item==0){
	    	    		//define the file-name to save photo taken by Camera activity
	    	    		String fileName = "dpic.jpg";
	    	    		//create parameters for Intent with filename
	    	    		ContentValues values = new ContentValues();
	    	    		values.put(MediaStore.Images.Media.TITLE, fileName);
	    	    		values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured for 4D Nest");
	    	    		/*
	    	    		 * We are going to save the Uri to the image before actually taking the picture.
	    	    		 * This was the way used in the example, so far I haven't been able to find a better
	    	    		 * way (but there has to be one, this cant be good)
	    	    		 */
	    	    		capturedImageURI = getContentResolver().insert(
	    	    		        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    	    		//create new Intent
	    	    		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
	    	    		intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI); //tell the intent where to store the file
	    	    		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	    	    		startActivityForResult(intent, CAMERA_PIC_REQUEST);
	    	    	}
	    	    }
	    	});
	    	dialog = builder.create();
	    	break;
	    
	    case DIALOG_ASK_VIDEO:
	    	final CharSequence[] videoItems = {"Open Camera", "Open Video Gallery"};
	    	AlertDialog.Builder videoBuilder = new AlertDialog.Builder(this);
	    	videoBuilder.setTitle("Select Source");
	    	videoBuilder.setItems(videoItems, new DialogInterface.OnClickListener() {
	    	    public void onClick(DialogInterface dialog, int item) {
	    	    	if(item==0){
	    	    		//String fileName = "dpic.jpg";
	    	    		//create parameters for Intent with filename
	    	    		ContentValues values = new ContentValues();
	    	    		//values.put(MediaStore.Images.Media.TITLE, fileName);
	    	    		values.put(MediaStore.Images.Media.DESCRIPTION,"Video captured for 4D Nest");
	    	    		/*
	    	    		 * We are going to save the Uri to the image before actually taking the picture.
	    	    		 * This was the way used in the example, so far I haven't been able to find a better
	    	    		 * way (but there has to be one, this cant be good)
	    	    		 */
	    	    		/*capturedImageURI = getContentResolver().insert(
	    	    		        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); */
	    	    		//create new Intent
	    	    		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE );
	    	    		//intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI); //tell the intent where to store the file
	    	    		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	    	    		startActivityForResult(intent, CAMERA_VIDEO_REQUEST);
	    	    	}
	    	    	if(item==1){
	    	    		Intent intent = new Intent();
	    	    		intent.setType("video/*");
	    	    		intent.setAction(Intent.ACTION_GET_CONTENT);
	    	    		intent.addCategory(Intent.CATEGORY_OPENABLE);
	    	    		startActivityForResult(
    						Intent.createChooser(intent, "Select Video"),
    						SELECT_VIDEO);
	    	    	}
	    	    }
	    	});
	    	dialog = videoBuilder.create();
	    	break;

	    	    	
	    	    
	    
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	

	/*
	 * This method is used once media item has been selected or captured. 
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
		else if(requestCode==CAMERA_PIC_REQUEST){
			if (resultCode == RESULT_OK) {
				
				/*
				 * Its essentially the same code again. I think I should move this over to some nice
				 * cosy private help method some where.
				 */
				
				filemanagerstring = capturedImageURI.getPath();

				// MEDIA GALLERY
				selectedFilePath = getPath(capturedImageURI);
				
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
				this.fileURL = filePath;
				this.currentMediaItem = mediaItemType.image;
				this.refreshElements();
				
				
				
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
			}
			else {
				Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
			}
		}
		
		else if(requestCode==CAMERA_VIDEO_REQUEST){
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
			this.fileURL = filePath;
			this.currentMediaItem = mediaItemType.video;
			this.refreshElements();
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
			//TODO create help for new egg
			return true;
		case R.id.menu_create_discard:
			//TODO discard implementation
			return true;
		}
		return false;
	}
	
	
}
	


