package org.fourdnest.androidclient.ui;


import org.fourdnest.androidclient.R;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import android.app.Activity;
import android.os.Bundle;

public class MapViewActivity extends Activity {
    /** Called when the activity is first created. */
    private MapController mapController;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(8);
        GeoPoint point2 = new GeoPoint(61000000, 25000000);
        mapController.setCenter(point2);

    }
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}   
