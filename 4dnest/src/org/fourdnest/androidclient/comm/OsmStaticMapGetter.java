package org.fourdnest.androidclient.comm;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.comm.CommUtils;

import android.net.Uri;

public class OsmStaticMapGetter implements StaticMapGetter {
	private static String BASEURI = "http://pafciu17.dev.openstreetmap.org/?module=map&bbox=20,67,30,63&width=800";
	private static String TESTURI = "/sdcard/testfile";
	
	/**
	 * comments
	 */
	public boolean getStaticMap(Egg egg) {
		boolean val = CommUtils.getNetFile(Uri.parse(BASEURI), Uri.parse(TESTURI));
		return val;
	}


}
