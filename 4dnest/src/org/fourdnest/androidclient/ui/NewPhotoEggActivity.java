package org.fourdnest.androidclient.ui;

import java.io.File;

import org.fourdnest.androidclient.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class NewPhotoEggActivity extends Activity{
	private String pictureURL = "";
	
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

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    
		    thumbNailView.setImageBitmap(myBitmap);

		}

	}
	

}
