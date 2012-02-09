package org.fourdnest.androidclient.comm;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.comm.CommUtils;

import android.net.Uri;
import android.util.Log;

public class OsmStaticMapGetter implements StaticMapGetter {
	private static String BASEURI = "http://pafciu17.dev.openstreetmap.org/?module=map";
	private static String JPG_TYPE = "imgType=jpg";

	

	public boolean getStaticMap(Egg egg) {
		String internalUriString = ThumbnailManager.getThumbnailUriString(egg);
		String uriString = BASEURI;
		uriString = setBoundBox(uriString, 15, 65, 20, 63);
		uriString = setWidth(uriString, 400);
		//uriString = setHeight(uriString, 100);
		uriString = uriString + JPG_TYPE;
		Log.d("thumbs", uriString);
		boolean val = CommUtils.getNetFile(Uri.parse(uriString), Uri.parse(internalUriString));
		Log.d("map_val", String.valueOf(val));
		return val;
	}
	private String setBoundBox(String uriString ,float leftBoundLongitude, float topBoundLatitude, float rightBoundLongitude, float lowBoundLatitude) {
		String attribute = "&bbox=";
		attribute = attribute + String.format("%.4f", leftBoundLongitude);
		attribute = attribute + "," + String.format("%.4f", topBoundLatitude);
		attribute = attribute + "," + String.format("%.4f", rightBoundLongitude);
		attribute = attribute + "," + String.format("%.4f", lowBoundLatitude);
		return uriString + attribute;
	}
	
	private String addPath(String uriString, Egg egg) {
		return uriString;
	}
	
	private String addPoint(String uriString, Egg egg) {
		return uriString;
	}
	
	private String setCenterPoint(String uriString, float longitude, float latitude) {
		return uriString + "&center=" + String.format("%.4f", longitude) + "," +  String.format("%.4f", longitude);
	}
	
	private String setWidth(String uriString, int width) {
		return uriString + "&width=" + width;
	}
	
	private String setHeight(String uriString, int height) {
		return uriString + "&height=" + height;
	}
	
	private String setZoom(String uriString, int zoomLevel) {
		return uriString;
	}


}
