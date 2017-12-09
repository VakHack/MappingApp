package async.example.com.mappingapp;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 07/12/2017.
 */

public class PolygonsHandler {

    private KmlLayer areaLayer;
    private LatLng midCoordinate;

    private List<LatLng> boundaries;
    private int intersectionRange;

    PolygonsHandler(GoogleMap map, Context context, int areaRawPath, int intersectionRange){
        this.intersectionRange = intersectionRange;

        try {

            areaLayer = new KmlLayer(map, areaRawPath, context);
            areaLayer.addLayerToMap();
            areaLayer.removeLayerFromMap();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boundaries = initBoundaries();
        midCoordinate = findMidCoordinate();
    }

    private List<LatLng> initBoundaries(){

        KmlContainer kmlContainer = areaLayer.getContainers().iterator().next();

        if (kmlContainer == null) return null;

        KmlPlacemark placemark = kmlContainer.getPlacemarks().iterator().next();
        KmlPolygon polygon = (KmlPolygon)placemark.getGeometry();

        return polygon.getOuterBoundaryCoordinates();
    }

    private LatLng findMidCoordinate(){

        int n = boundaries.size();
        float lat = 0;
        float lng = 0;

        for(LatLng latLng : boundaries){

            lat += latLng.latitude;
            lng += latLng.longitude;
        }

        //setting the mid coordinate to be the average point on the polygon
        return new LatLng(lat/ n, lng/ n);
    }

    public LatLng getMidCoordinate() {
        return midCoordinate;
    }

    public boolean isWithin(LatLng latLng) throws ExecutionException, InterruptedException {

        WithinPolygonChecker checker = new WithinPolygonChecker(boundaries, intersectionRange);

        return checker.execute(latLng).get();
    }

    public List<LatLng> getBoundaries() {
        return boundaries;
    }
}
