package org.fourdnest.androidclient;

import java.io.File;

import android.net.Uri;

/**
 * 
 *
 */
public class ThumbnailManager {
	private static String BASEURI = "/sdcard/.fourdnest/thumbnails/";
	
	public ThumbnailManager() {
		
	}
	
	private boolean thumbNailExists(String path) {
		if ((new File(path)).exists()) {
			return true;
		}else {
			return false;	
		}
	}
	private String getThumbnailUri(Egg egg) {
		return BASEURI + egg.getExternalId() + ".jpg";
	}
	
	public Uri getThubnail(Egg egg) {
		String path = getThumbnailUri(egg);
		if (!thumbNailExists(path)) {
			if (egg.getMimeType() == Egg.fileType.ROUTE) {
				
			}else {
				
			}
		}
		return Uri.parse(path);
	}
}
