package org.fourdnest.androidclient.comm;

import java.io.File;

import org.fourdnest.androidclient.Egg;

import android.net.Uri;

/**
 * 
 *
 */
public class FourDNestThumbnailManager implements ThumbnailManager {
	private static String BASEURI = "/sdcard/.fourdnest/thumbnails/";
	
	public FourDNestThumbnailManager() {
		
	}
	
	private boolean thumbNailExists(String path) {
		if ((new File(path)).exists()) {
			return true;
		}else {
			return false;	
		}
	}
	private String getThumbnailUriString(Egg egg) {
		return BASEURI + egg.getExternalId() + ".jpg";
	}
	
	public Uri getThumbnail(Egg egg) {
		String path = getThumbnailUriString(egg);
		Uri thumbnailUri = Uri.parse(path);
		boolean res = false;
		if (!thumbNailExists(path)) {
			if (egg.getMimeType() == Egg.fileType.ROUTE) {
				StaticMapGetter mapGetter = new OsmStaticMapGetter();
				res = mapGetter.getStaticMap(egg);
			}else {
				// IMPLEMENT OTHER FILES
				res = false;
			}
		}
		if (res) {
			return thumbnailUri;
		}else {
			return null;
		}
	}
	
	public boolean deleteLocalThumbnail(Egg egg) {
		return false;
	}
}
