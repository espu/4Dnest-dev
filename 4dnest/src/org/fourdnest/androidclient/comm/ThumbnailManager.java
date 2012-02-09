package org.fourdnest.androidclient.comm;

import java.io.File;

import org.fourdnest.androidclient.Egg;

import android.net.Uri;
import android.os.Environment;

/**
 * 
 * 
 *
 */
public class ThumbnailManager {

    private static String THUMBNAIL_LOCATION = "/fourdnest/thumbnails/";

    /** Location of thumbnails on the server */
    public static final String THUMBNAIL_PATH = "content/instance/";

    /** Thumbnails on the server are in jpg format */
    private static final String THUMBNAIL_FILETYPE = ".jpg";

    /**
     * Check if given file exists
     * 
     * @param egg
     *            for which the check is done
     * @return boolean
     */
    public static boolean thumbNailExists(Egg egg) {
        String path = getThumbnailUriString(egg);
        if ((new File(path)).exists()) {
            return true;
        }else {
            return false;   
        }
    }
    
    /**
     * Returns predefined thumbnailUri for given egg as a string based on localID and a hash from unique property
     * @param egg 
     * @return String of egg's thumbnail's Uri
     */
    public static String getThumbnailUriString(Egg egg) {
        if (egg.getRemoteFileURI() != null) {
            return Environment.getExternalStorageDirectory()
                    + THUMBNAIL_LOCATION
                    + egg.getId()
                    + CommUtils
                            .md5FromString(egg.getRemoteFileURI().toString())
                    + THUMBNAIL_FILETYPE;
        }else {
            // egg has only textual content
            return Environment.getExternalStorageDirectory()
                    + THUMBNAIL_LOCATION
                    + egg.getId()
                    + CommUtils
                            .md5FromString(egg.getCaption())
                    + THUMBNAIL_FILETYPE;
        }

    }
}
