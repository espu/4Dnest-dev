package org.fourdnest.androidclient.comm;

import android.net.Uri;

public interface StaticMapGetter {
	boolean getStaticMap(Uri localUri, Uri routeFileUri);
}
