package async.example.com.mappingapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Ramtchka on 09/12/2017.
 */

public class ClosestPointOnPolygonFinder extends AsyncTask<Void, Void, Double> {

    private List<LatLng> polygonBounds;
    private LatLng marker;

    private LatLng closest = null;
    private LatLng secClosest = null;

    public ClosestPointOnPolygonFinder(List<LatLng> polygonBounds, LatLng marker) {
        this.polygonBounds = polygonBounds;
        this.marker = marker;
    }

    @Override
    protected Double doInBackground(Void... voids) {

        //return null in case the polygon has less than two points
        if(polygonBounds.size() < 2) return null;

        findClosestCouple();
        findClosetPointBetweenClosestCouple();

        return DistanceCalculator.coordinatesDistance(marker, closest);
    }

    private void findClosestCouple(){

        //init min point holders to the first two points
        closest = DistanceCalculator.min(marker, polygonBounds.get(0), polygonBounds.get(1));
        secClosest = DistanceCalculator.max(marker, polygonBounds.get(0), polygonBounds.get(1));

        for(int i = 1; i < polygonBounds.size() - 1; ++i){

            //for each couple of point, running on bounds points in order, find their closest couple to marker
            LatLng localMin = DistanceCalculator.min(marker, polygonBounds.get(i), polygonBounds.get(i + 1));
            LatLng localMax = DistanceCalculator.max(marker, polygonBounds.get(i), polygonBounds.get(i + 1));

            if((closest == localMin || localMin == DistanceCalculator.min(marker, localMin, closest))
            && localMax == DistanceCalculator.min(marker, localMax, secClosest)){

                closest = localMin;
                secClosest = localMax;
            }
        }
    }

    private void findClosetPointBetweenClosestCouple(){

        final double PROXIMATION_CYCLES = DistanceCalculator.coordinatesDistance(closest, secClosest) / 100;

        for(int i = 0; i < PROXIMATION_CYCLES; ++i){

            LatLng mid = DistanceCalculator.midPoint(closest, secClosest);

            LatLng prevClosest = new LatLng(closest.latitude, closest.longitude);

            closest = DistanceCalculator.min(marker, prevClosest, mid);
            secClosest = DistanceCalculator.max(marker, prevClosest, mid);
        }
    }

    public LatLng getClosest() {
        return closest;
    }
}
