package org.fourdnest.androidclient.comm;

import java.io.File;

import org.fourdnest.androidclient.Egg;

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
	 * @param size
	 *            thumbnail size
	 * @return boolean
	 */

	public static boolean thumbNailExists(Egg egg, String size) {
		String path = getThumbnailUriString(egg, size);
		return new File(path).exists();
	}

	/**
	 * Returns predefined thumbnailUri for given egg as a string based on
	 * localID and a hash from unique property
	 * 
	 * @param egg
	 * @param size
	 *            thumbnail size
	 * @return String of egg's thumbnail's Uri
	 */
	public static String getThumbnailUriString(Egg egg, String size) {
		if (egg.getRemoteFileURI() != null) {
			return Environment.getExternalStorageDirectory()
					+ THUMBNAIL_LOCATION
					+ egg.getId()
					+ CommUtils
							.md5FromString(egg.getRemoteFileURI().toString())
					+ size + THUMBNAIL_FILETYPE;
		} else if (egg.getMimeType() == Egg.fileType.ROUTE) {
			return Environment.getExternalStorageDirectory()
					+ THUMBNAIL_LOCATION + egg.getId()
					+ CommUtils.md5FromFile(egg.getLocalFileURI().toString()) + THUMBNAIL_FILETYPE;
		} else {
			// egg has only textual content (shouldn't get this far) or is local
			return Environment.getExternalStorageDirectory()
					+ THUMBNAIL_LOCATION + egg.getId()
					+ CommUtils.md5FromString(egg.getCaption())
					+ THUMBNAIL_FILETYPE;
		}

	}
}
