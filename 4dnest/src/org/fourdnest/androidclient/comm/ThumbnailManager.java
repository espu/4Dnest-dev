package org.fourdnest.androidclient.comm;

import org.fourdnest.androidclient.Egg;

import android.net.Uri;

/**
 * 
 * This interface provides the needed methods for a thumbnail manager
 *
 */
public interface ThumbnailManager {
	boolean getThumbnail(Egg egg);
	boolean deleteLocalThumbnail(Egg egg);
}
