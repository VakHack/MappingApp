package async.example.com.mappingapp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import java.util.List;

/**
 * Created by Administrator on 07/12/2017.
 */

public class BoundariesChecker {

    private KmlLayer kmlLayer;
    private List<LatLng> boundaries;
    private LatLng midCoordinate;

    BoundariesChecker(KmlLayer kmlLayer){
        this.kmlLayer = kmlLayer;

        initBoundaries();
        findMidCoordinate();
    }

    private void initBoundaries(){

        KmlContainer kmlContainer = kmlLayer.getContainers().iterator().next();

        if (kmlContainer == null){

            boundaries = null;
            return;
        }

        KmlPlacemark placemark = kmlContainer.getPlacemarks().iterator().next();
        KmlPolygon polygon = (KmlPolygon)placemark.getGeometry();

        boundaries = polygon.getOuterBoundaryCoordinates();
    }

    private void findMidCoordinate(){

        int n = boundaries.size();
        float lat = 0;
        float lng = 0;

        for(LatLng latLng : boundaries){

            lat += latLng.latitude;
            lng += latLng.longitude;
        }

        //setting the mid coordinate to be the average point on the polygon
        midCoordinate = new LatLng(lat/ n, lng/ n);
    }

    public boolean isWithin(LatLng latLng) {

        Log.e("maplog", "" + boundaries);

        return true;
    }

    public LatLng getMidCoordinate() {
        return midCoordinate;
    }
}
