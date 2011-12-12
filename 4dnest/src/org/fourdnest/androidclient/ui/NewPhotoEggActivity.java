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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewPhotoEggActivity extends Activity{
	private static final String TAG = "NewPhotoEggActivity";
	private String pictureURL = "";
	private String realPictureURL = "";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_photo_egg_view);
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
            	Log.d(TAG, "picture url is: xzy  " +realPictureURL);
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
				//SendQueueService sendQueService = ((FourDNestApplication)getApplication()).getSendQueueService();
				
				//TODO: Proper implementation
				Egg egg = new Egg();
				egg.setCaption(((EditText)findViewById(R.id.new_photo_egg_caption_view)).getText().toString());
				egg.setLocalFileURI(Uri.parse("file://"+realPictureURL));
				egg.setTags(new ArrayList<Tag>());
				
				SendQueueService.sendEgg(getApplication(), egg);
				finish();
			}
		});
        
        

	}
	

}
