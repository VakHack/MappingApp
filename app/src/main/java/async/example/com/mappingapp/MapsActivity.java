package async.example.com.mappingapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
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

    final private int INTERSECTION_BOUNDS = 5;
    final private int GOOD_POLY_INDEX = 0;
    final private int BAD_POLY_INDEX = 1;

    private GoogleMap map;
    private Marker customMarker = null;
    private PolygonsHandler[] ph;
    private PolygonOptions[] polygon;
    private int currentActivePoly = BAD_POLY_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void addPolygonToMap(int rawId, int num){

        ph[num] = new PolygonsHandler(map, getApplicationContext(), rawId, INTERSECTION_BOUNDS);

        //adding given polygon to map
        polygon[num] = new PolygonOptions();
        polygon[num].addAll(ph[num].getBoundaries());
        map.addPolygon(polygon[num]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //initialize the two polygons arrays
        polygon = new PolygonOptions[2];
        ph = new PolygonsHandler[2];

        addPolygonToMap(R.raw.allowed_area, GOOD_POLY_INDEX);
        addPolygonToMap(R.raw.bad_sample, BAD_POLY_INDEX);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (customMarker != null) {
                    customMarker.remove();
                }

                customMarker = map.addMarker(new MarkerOptions().position(latLng));

                try {

                    if (ph[currentActivePoly].isWithin(latLng)) {
                        Toast.makeText(getApplicationContext(),
                                "Marker is within polygon boundaries", Toast.LENGTH_SHORT).show();

                    } else {
                        ClosestPointOnPolygonFinder finder =
                                new ClosestPointOnPolygonFinder(ph[currentActivePoly].getBoundaries(), latLng);
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

        findViewById(R.id.good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentActivePoly == BAD_POLY_INDEX){
                    currentActivePoly = GOOD_POLY_INDEX;

                    polygon[BAD_POLY_INDEX].visible(false);
                    polygon[GOOD_POLY_INDEX].visible(true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ph[GOOD_POLY_INDEX].getMidCoordinate(), 14));
                }
            }
        });

        //for start, focusing on the "good" polygon
        findViewById(R.id.good).performClick();

        findViewById(R.id.bad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentActivePoly == GOOD_POLY_INDEX){
                    currentActivePoly = BAD_POLY_INDEX;

                    polygon[BAD_POLY_INDEX].visible(true);
                    polygon[GOOD_POLY_INDEX].visible(false);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ph[BAD_POLY_INDEX].getMidCoordinate(), 7));
                }
            }
        });
    }
}
