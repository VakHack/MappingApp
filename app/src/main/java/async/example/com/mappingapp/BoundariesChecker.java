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

    private boolean isIntersect(LatLng startPoint, LatLng endPoint){

        for(LatLng latLng : boundaries){


        }
        Line2D line1 = new Line2D.Float(100, 100, 200, 200);
        Line2D line2 = new Line2D.Float(150, 150, 150, 200);
        boolean result = line2.intersectsLine(line1);
    }

    public boolean isWithin(LatLng latLng) {

        return true;
    }

    public LatLng getMidCoordinate() {
        return midCoordinate;
    }
}
