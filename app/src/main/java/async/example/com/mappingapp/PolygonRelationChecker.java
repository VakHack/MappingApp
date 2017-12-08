package async.example.com.mappingapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 07/12/2017.
 */

public class PolygonRelationChecker {

    private KmlLayer kmlLayer;
    private LatLng midCoordinate;

    private List<LatLng> boundaries;
    private int intersectionRange;

    PolygonRelationChecker(KmlLayer kmlLayer, int intersectionRange){
        this.kmlLayer = kmlLayer;
        this.intersectionRange = intersectionRange;

        boundaries = initBoundaries();
        midCoordinate = findMidCoordinate(boundaries);
    }

    private List<LatLng> initBoundaries(){

        KmlContainer kmlContainer = kmlLayer.getContainers().iterator().next();

        if (kmlContainer == null) return null;

        KmlPlacemark placemark = kmlContainer.getPlacemarks().iterator().next();
        KmlPolygon polygon = (KmlPolygon)placemark.getGeometry();

        return polygon.getOuterBoundaryCoordinates();
    }

    private LatLng findMidCoordinate(List<LatLng> boundaries){

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

        RelationCheckExecutor executor = new RelationCheckExecutor(boundaries, intersectionRange);

        return executor.execute(latLng).get();
    }
}
