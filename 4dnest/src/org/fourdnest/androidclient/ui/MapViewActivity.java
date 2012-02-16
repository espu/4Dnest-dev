package org.fourdnest.androidclient.ui;


import java.util.ArrayList;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.R;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

public class MapViewActivity extends Activity {
	
	public static final String EGG_ID = "EggID";
	private static final int DEFAULT_ZOOM = 15;
    /** Called when the activity is first created. */
    private MapController mapController;
    private MapView mapView;
    private FourDNestApplication application;
    private int eggID;
    private ItemizedOverlay<OverlayItem> overlay;
    private ResourceProxy resourceProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	this.application = (FourDNestApplication) getApplication();
		
		Bundle startingExtras = getIntent().getExtras();
		this.eggID = (Integer) startingExtras
				.get(MapViewActivity.EGG_ID);
		
		setContentView(R.layout.egg_view);

		Egg egg = this.application.getStreamEggManager().getEgg(eggID);
    	
        super.onCreate(savedInstanceState);
        
        resourceProxy = new DefaultResourceProxyImpl(application);
                
        setContentView(R.layout.map_view);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(DEFAULT_ZOOM);
        GeoPoint point2 = new GeoPoint(egg.getLatitude(), egg.getLongitude());
        mapController.setCenter(point2);
        
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("", "", point2));
        
        this.overlay = new ItemizedIconOverlay<OverlayItem>(items,
        		new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

					public boolean onItemLongPress(int arg0, OverlayItem arg1) {
						// TODO Auto-generated method stub
						return true;
					}

					public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {
						// TODO Auto-generated method stub
						return true;
					}
				}, resourceProxy);
        
        this.mapView.getOverlays().add(this.overlay);
        mapView.invalidate();

    }
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}   
