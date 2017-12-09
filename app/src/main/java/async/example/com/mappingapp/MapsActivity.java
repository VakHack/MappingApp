package async.example.com.mappingapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final private int INTERSECTION_BOUNDS = 5;
    final private int GOOD_POLY_INDEX = 0;
    final private int BAD_POLY_INDEX = 1;

    private GoogleMap map;

    private Marker touchMarker = null;
    private Polyline distanceLine = null;

    private PolygonHandler[] ph;
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

        //setting buttons fonts
        Button good = findViewById(R.id.good);
        good.setTypeface(Typeface.createFromAsset(getAssets(), "Stay_Writer.ttf"));
        Button bad = findViewById(R.id.bad);
        bad.setTypeface(Typeface.createFromAsset(getAssets(), "Stay_Writer.ttf"));
    }

    private void addPolygonToMap(int rawId, int num){

        ph[num] = new PolygonHandler(map, getApplicationContext(), rawId, INTERSECTION_BOUNDS);

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
        ph = new PolygonHandler[2];

        addPolygonToMap(R.raw.allowed_area, GOOD_POLY_INDEX);
        addPolygonToMap(R.raw.bad_sample, BAD_POLY_INDEX);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(touchMarker != null) touchMarker.remove();
                if(distanceLine != null) distanceLine.remove();

                touchMarker = map.addMarker(new MarkerOptions().position(latLng));
                DecimalFormat df = new DecimalFormat("#.###");
                touchMarker.setTitle("" + df.format(latLng.latitude) + "/" + df.format(latLng.longitude));

                try {

                    if (ph[currentActivePoly].isWithin(latLng)) {

                        touchMarker.setSnippet("Within polygon");
                        touchMarker.showInfoWindow();

                    } else {

                        ClosestPointOnPolygonFinder finder =
                                new ClosestPointOnPolygonFinder(ph[currentActivePoly].getBoundaries(), latLng);

                        int distance = finder.execute().get().intValue();

                        touchMarker.setSnippet("Distance to polygon: " + distance + "M");
                        touchMarker.showInfoWindow();

                        distanceLine = map.addPolyline(new PolylineOptions()
                                .add(latLng, finder.getClosest())
                                .width(8)
                                .pattern(Arrays.asList(new Dash(60), new Gap(20)))
                                .color(Color.GRAY));
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

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ph[BAD_POLY_INDEX].getMidCoordinate(), 6));
                }
            }
        });
    }
}
