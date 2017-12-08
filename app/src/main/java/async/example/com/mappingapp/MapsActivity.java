package async.example.com.mappingapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private KmlLayer allowedArea;
    private Marker customMarker = null;
    private PolygonRelationChecker rc;

    final private int INTERSECTION_BOUNDS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {

            allowedArea = new KmlLayer(map, R.raw.allowed_area, getApplicationContext());
            allowedArea.addLayerToMap();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // centering camera on polygon
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(rc.getMidCoordinate(), 12));

        rc = new PolygonRelationChecker(allowedArea, INTERSECTION_BOUNDS);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if (customMarker != null) {
                    customMarker.remove();
                }

                customMarker = map.addMarker(new MarkerOptions().position(latLng));

                try {

                    if (rc.isWithin(latLng)) {

                        customMarker.setSnippet("Marker is within\npolygon boundaries");
                        customMarker.showInfoWindow();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
