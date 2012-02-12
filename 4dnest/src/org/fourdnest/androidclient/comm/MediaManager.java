package org.fourdnest.androidclient.comm;

import java.io.File;

import org.fourdnest.androidclient.Egg;

import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;


public class MediaManager {
    private static String MEDIA_LOCATION = "/fourdnest/media/";
    
    /**
     * Check if given file exists
     * 
     * @param egg
     *            for which the check is done
     * @return boolean
     */
    public static boolean thumbNailExists(Egg egg) {
        String path = getMediaUriString(egg);
        if ((new File(path)).exists()) {
            return true;
        }else {
            return false;   
        }
    }
    
    public static String getMediaUriString(Egg egg) {
    	Uri remoteUri = egg.getRemoteFileURI();
        if (remoteUri == null) { // this shouldn't happen
            return Environment.getExternalStorageDirectory()
            		+ MEDIA_LOCATION + "fail";
		} else {
			String remoteUriString = remoteUri.toString();
			return Environment.getExternalStorageDirectory()
					+ MEDIA_LOCATION
					+ egg.getExternalId()
					+ MimeTypeMap.getFileExtensionFromUrl(remoteUriString);
		}

    }

}
