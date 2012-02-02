package org.fourdnest.androidclient.comm;

import org.fourdnest.androidclient.Egg;

import android.net.Uri;

public interface ThumbnailManager {
	boolean getThumbnail(Egg egg);
	boolean deleteLocalThumbnail(Egg egg);
}
