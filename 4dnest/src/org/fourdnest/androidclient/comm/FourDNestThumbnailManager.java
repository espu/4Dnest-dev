package org.fourdnest.androidclient.comm;

import java.io.File;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;

import android.os.Environment;
import android.util.Log;

/**
 * 
 * This class gives functionality to maintain egg thumbnails between 4dNest server and android client.
 *
 */
public class FourDNestThumbnailManager implements ThumbnailManager {
	
	private static String TAG = "4DNestThumbnailManager";
	
	private static String THUMBNAIL_LOCATION = "/fourdnest/thumbnails/";

	   /**Location of thumbnails on the server */
    public static final String THUMBNAIL_PATH = "content/instance/";
    
    /** Thumbnails on the server are in jpg format*/
    private static final String THUMBNAIL_FILETYPE = ".jpg";
    
    private static final String THUMBNAIL_DEFAULT_SIZE = "-400x400";
    
    private FourDNestApplication app;
	
	public FourDNestThumbnailManager() {
		this.app = FourDNestApplication.getApplication();
	}
	
	/**
	 * Check if given file exists
	 * @param path String of path location
	 * @return boolean
	 */
	private boolean thumbNailExists(String path) {
		if ((new File(path)).exists()) {
			return true;
		}else {
			return false;	
		}
	}
	
	/**
	 * Returns predefined thumbnailUri for given egg as a string
	 * @param egg 
	 * @return String of egg's thumbnail's Uri
	 */
	public static String getThumbnailUriString(Egg egg) {
		return Environment.getExternalStorageDirectory() + THUMBNAIL_LOCATION + egg.getExternalId() + THUMBNAIL_FILETYPE;
	}
	
	/**
	 * Can be called to make sure thumbnail is in memory card, thumbnail is downloaded from 4dnest server or
	 * OSM static maps api when applicable.
	 * 
	 * @param Egg whose thumbnail is in question
	 * 
	 * @return boolean whether thumbnail can be found in predefined location
	 */
	public boolean getThumbnail(Egg egg) {
		String path = getThumbnailUriString(egg);
		boolean res = false;
		if (!thumbNailExists(path)) {
			if (egg.getMimeType() == Egg.fileType.ROUTE) {
				StaticMapGetter mapGetter = new OsmStaticMapGetter();
				res = mapGetter.getStaticMap(egg);
			}else {
				String externalUriString = app.getCurrentNest().getBaseURI()
                        + THUMBNAIL_PATH + egg.getExternalId()
                        + THUMBNAIL_DEFAULT_SIZE + THUMBNAIL_FILETYPE;
                String thumbnail_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + THUMBNAIL_LOCATION;
                if (!new File(thumbnail_dir).exists()) {
                	new File(thumbnail_dir).mkdirs();
                }
                Log.d("SAVELOC", path);
                if (app.getCurrentNest().getProtocol().getMediaFile(externalUriString, path)) {
                    Log.d(TAG, "Thumbnail written succesfully");
                    res = true;
                }else {
                    Log.d(TAG, "Thumbnail failed to write");
                    res = false;
                }
			}
		}
		return res;
	}
	
	public boolean deleteLocalThumbnail(Egg egg) {
		// TODO Implement if time 
		return false;
	}
}
