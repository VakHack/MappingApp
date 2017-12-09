package async.example.com.mappingapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker customMarker = null;
    private PolygonsHandler ph;

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

        ph = new PolygonsHandler(map, getApplicationContext(), R.raw.allowed_area, INTERSECTION_BOUNDS);

        //adding given polygon to map
        PolygonOptions polygon = new PolygonOptions();
        polygon.addAll(ph.getBoundaries());
        map.addPolygon(polygon);

        // centering camera on polygon
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ph.getMidCoordinate(), 12));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (customMarker != null) {
                    customMarker.remove();
                }

                customMarker = map.addMarker(new MarkerOptions().position(latLng));

                try {

                    if (ph.isWithin(latLng)) {
                        Toast.makeText(getApplicationContext(), "Marker is within polygon boundaries", Toast.LENGTH_SHORT).show();

                    } else {
                        ClosestPointOnPolygonFinder finder = new ClosestPointOnPolygonFinder(ph.getBoundaries(), latLng);
                        int distance = finder.execute().get().intValue();

                        Toast.makeText(getApplicationContext(), "Distance between marker and polygon: "
                                + distance + " meters", Toast.LENGTH_SHORT).show();
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
