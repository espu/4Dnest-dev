package org.fourdnest.androidclient.ui;


import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import android.app.Activity;
import android.os.Bundle;

public class MapViewActivity extends Activity {
	
	public static final String EGG_ID = "EggID";
    /** Called when the activity is first created. */
    private MapController mapController;
    private MapView mapView;
    private FourDNestApplication application;
    private int eggID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	this.application = (FourDNestApplication) getApplication();
		
		Bundle startingExtras = getIntent().getExtras();
		this.eggID = (Integer) startingExtras
				.get(MapViewActivity.EGG_ID);
		
		setContentView(R.layout.egg_view);

		Egg egg = this.application.getStreamEggManager().getEgg(eggID);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(8);
        GeoPoint point2 = new GeoPoint(egg.getLatitude(), egg.getLongitude());
        mapController.setCenter(point2);
        

    }
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}   
