package org.fourdnest.androidclient.comm;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.comm.CommUtils;
import org.fourdnest.androidclient.tools.LocationHelper;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class OsmStaticMapGetter implements StaticMapGetter {
	private static String TAG = "StaticMapGetter";
	private static String BASEURI = "http://pafciu17.dev.openstreetmap.org/?module=map";
	private static String JPG_TYPE = "imgType=jpg";

	

	public boolean getStaticMap(Egg egg) {
		String internalUriString = ThumbnailManager.getThumbnailUriString(egg, "");
		try {
			List<String> list = getLocationListFromEgg(egg);
		} catch (Exception e) {
			Log.d(TAG, "Failed to produce location list from location file");
			e.printStackTrace();
			return false;
		}
		String uriString = BASEURI;
		uriString = setBoundBox(uriString, 28, 63, 23, 59);
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
	
	private String addPath(String uriString, List<String> list) {
		String attribute = "&paths=";
		String separator = ",";
		uriString = uriString + attribute;
		for (String location : list) {
			uriString = uriString + location + separator; // there can be a , in the end, too
		}
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
	
	private List<String> getLocationListFromEgg(Egg egg) throws Exception {
		List<String> locList = new ArrayList<String>();
		FileInputStream fstream = new FileInputStream(egg.getLocalFileURI().toString());
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while ((line = buffRead.readLine()) != null) {
				JSONObject temp = new JSONObject(line);
				String lat = temp.optString(LocationHelper.JSON_LATITUDE);
				String lon = temp.optString(LocationHelper.JSON_LONGITUDE);
				locList.add(lat + "," + lon);			
			}
		} catch (JSONException e) {
			Log.d(TAG, "Could not convert location file line to json object");
			e.printStackTrace();
		} finally {
			buffRead.close();
			in.close();
			fstream.close();
		}
		return locList;
	}


}
