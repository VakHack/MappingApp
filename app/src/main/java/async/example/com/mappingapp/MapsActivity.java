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
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private KmlLayer allowedArea;
    private Marker customMarker = null;
    private BoundariesChecker bc;

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

        bc = new BoundariesChecker(allowedArea);

        // centering camera on polygon
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(bc.getMidCoordinate(), 12));
        map.addMarker(new MarkerOptions().position(bc.getMidCoordinate()));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if(customMarker != null){
                    customMarker.remove();
                }

                customMarker = map.addMarker(new MarkerOptions().position(latLng));

                if(bc.isWithin(latLng)){

                    Toast.makeText(getApplicationContext(), "Marker is within polygon boundaries", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
