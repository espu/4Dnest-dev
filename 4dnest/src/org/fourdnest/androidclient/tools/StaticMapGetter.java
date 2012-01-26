package org.fourdnest.androidclient.tools;

import android.net.Uri;

public interface StaticMapGetter {
	boolean getStaticMap(Uri localUri, Uri routeFileUri);
}
