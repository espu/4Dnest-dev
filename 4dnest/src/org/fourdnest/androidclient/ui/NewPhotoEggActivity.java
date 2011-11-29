package org.fourdnest.androidclient.ui;

import java.io.File;

import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

public class NewPhotoEggActivity extends Activity{
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
		 * Adds a onclicklistener to the image so we know when to open a thumbnail
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
	

	}
	

}
