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
    private int intersectionRange;

    BoundariesChecker(KmlLayer kmlLayer, int intersectionRange){
        this.kmlLayer = kmlLayer;
        this.intersectionRange = intersectionRange;

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

        Log.e("maplog", "" + boundaries);
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

    private boolean isIntersect(LatLng startPoint, LatLng endPoint, boolean forLatRange){

        LatLng[] firstLine = {startPoint, endPoint};

        for(int i = 0; i < boundaries.size() - 1; ++i){

            LatLng[] secLine = {boundaries.get(i), boundaries.get(i + 1)};

            if(Intersector.isIntresectWithinRange(firstLine, secLine, forLatRange)) return true;
        }

        return false;
    }

    public boolean isWithin(LatLng latLng) {

        //checking if line from coordinate towards north, east, south and west intersects with polygon
        LatLng latLow = new LatLng(latLng.latitude - intersectionRange, latLng.longitude);
        LatLng latHi = new LatLng(latLng.latitude + intersectionRange, latLng.longitude);

        LatLng lngLow = new LatLng(latLng.latitude, latLng.longitude - intersectionRange);
        LatLng lngHi = new LatLng(latLng.latitude, latLng.longitude + intersectionRange);

        return isIntersect(latLng, latLow, true) && isIntersect(latLng, latHi, true) &&
                isIntersect(latLng, lngLow, false) &&isIntersect(latLng, lngHi, false);
    }

    public LatLng getMidCoordinate() {
        return midCoordinate;
    }
}
